package graphics;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import peasy.PeasyCam;
import processing.core.PApplet;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ListBox;
import controlP5.Slider;
import controlP5.Textarea;
import engine.Coordinate;
import engine.Tank;
import engine.TankSize;
import fish.Fish;
import fish.Guppy;

public class Visual extends PApplet{
	private static final long serialVersionUID = 1L;
	final int fieldX = Toolkit.getDefaultToolkit().getScreenSize().width;
	final int fieldY = Toolkit.getDefaultToolkit().getScreenSize().height;
	final int fieldZ = 700;
	final float zoomPercentage = (float) .85;

	ControlP5 infoPane;
	PeasyCam camera;

	Textarea tankInfo;
	public ListBox fishChoices;
	Textarea fishInfo;
	Button fishImage;
	Slider fishHappiness;
	Slider fishFullness;
	Slider fishHealth;
	Textarea helpText;

	int updateCount = 60; //cause text to be updated less frequently
	public int fishChoice = -1;
	public int activeTab = 0;


	public Tank tank;

	/**************************************************
	 * ESSENTIAL FUNCTIONS *
	 *************************************************/

	public void setup(){		
		/**************************************************
		 * PROCESSING SETTINGS *
		 *************************************************/
		size(fieldX, fieldY, P3D);
		camera = new PeasyCam(this, fieldX/2, fieldY/2, 0, fieldZ/2); //initialize the peasycam
		camera.setActive(false);
		frameRate(30); //causes draw() to be called 30 times per second

		/**************************************************
		 * TANK INITIALIZATION *
		 *************************************************/

		tank = new Tank(TankSize.MEDIUM);
		tank.addFish(new Guppy(this));

		/**************************************************
		 * CONTROLP5 SETTINGS *
		 *************************************************/

		infoPane = new ControlP5(this);
		infoPane.setAutoDraw(false);

		/**************************************************
		 * TAB INITIALIZATION *
		 *************************************************/

		infoPane.window().setPositionOfTabs(fieldX-320, 60);

		infoPane.addTab("tankinfo")
		.setTitle("tank info")
		.setId(1)
		.setColorBackground(color(50, 50, 50))
		.setColorLabel(color(255))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(35, 35, 35))
		.activateEvent(true);

		infoPane.getTab("default")
		.setId(0)
		.setTitle("options")
		.setColorBackground(color(50, 50, 50))
		.setColorLabel(color(255))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(35, 35, 35))
		.activateEvent(true);

		infoPane.addTab("fishinfo")
		.setTitle("fish info")
		.setId(2)
		.setColorBackground(color(50, 50, 50))
		.setColorLabel(color(255))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(35, 35, 35))
		.activateEvent(true);

		infoPane.addTab("save")
		.setId(3)
		.setColorBackground(color(50, 50, 50))
		.setColorLabel(color(255))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(35, 35, 35))
		.activateEvent(true);

		infoPane.addTab("help")
		.setId(4)
		.setColorBackground(color(50, 50, 50))
		.setColorLabel(color(255))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(35, 35, 35))
		.activateEvent(true);

		infoPane.addButton("")
		.setColorBackground(color(90, 90, 90))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(90, 90, 90))
		.setSize(320, fieldY-136)
		.setPosition(fieldX-320, 76);

		infoPane.addButton(" ")
		.setColorBackground(color(90, 90, 90))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(90, 90, 90))
		.setSize(320, fieldY-136)
		.setPosition(fieldX-320, 76);

		infoPane.addButton("  ")
		.setColorBackground(color(90, 90, 90))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(90, 90, 90))
		.setSize(320, fieldY-136)
		.setPosition(fieldX-320, 76);

		infoPane.addButton("   ")
		.setColorBackground(color(90, 90, 90))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(90, 90, 90))
		.setSize(320, fieldY-136)
		.setPosition(fieldX-320, 76);

		infoPane.addButton("    ")
		.setColorBackground(color(90, 90, 90))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(90, 90, 90))
		.setSize(320, fieldY-136)
		.setPosition(fieldX-320, 76);

		infoPane.getController(" ").moveTo("tankinfo");
		infoPane.getController("  ").moveTo("fishinfo");
		infoPane.getController("   ").moveTo("save");
		infoPane.getController("    ").moveTo("help");

		/**************************************************
		 * OPTIONS TAB INITIALIZATION *
		 *************************************************/

		infoPane.addButton("addFish")
		.setLabel("add fish")
		.setPosition(fieldX-315, 85)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0));

		infoPane.addButton("feedFish")
		.setLabel("feed fish")
		.setPosition(fieldX-240, 85)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0));

		infoPane.addButton("cleanTank")
		.setLabel("clean tank")
		.setPosition(fieldX-165, 85)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0));

		infoPane.addButton("changeWater")
		.setLabel("change water")
		.setPosition(fieldX-90, 85)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0));

		/**************************************************
		 * TANKINFO TAB INITIALIZATION *
		 *************************************************/

		new Textarea(infoPane, "tankLabels")
		.setSize(320, fieldY-136)
		.setPosition(fieldX-315, 85)
		.setFont(createFont("arial", 12))
		.setText("pH: "
			+ "\n\nTemperature: "
			+ "\n\nHardness: "
			+ "\n\nAmmonia: "
			+ "\n\nNitrite: "
			+ "\n\nNitrate: "
			+ "\n\nDissolved O2: "
			+ "\n\nDissolved CO2: "
			+ "\n\nNitrosomonas bacteria: "
			+ "\n\nNitrobacter bacteria: "
			+ "\n\nFood: "
			+ "\n\nWaste: ")
		.moveTo("tankinfo");
		
		tankInfo = new Textarea(infoPane, "tankInfo")
		.setSize(320, fieldY-136)
		.setPosition(fieldX-150, 85)
		.setFont(createFont("arial", 12))
		.moveTo("tankinfo");

		/**************************************************
		 * FISHINFO TAB INITIALIZATION *
		 *************************************************/

		fishChoices = new ListBox(infoPane, "fishChoices")
		.setSize(310, fieldY-480)
		.setPosition(fieldX-315, 420)
		.setLabel("fish choices")
		.setId(0)
		.disableCollapse()
		.moveTo("fishinfo");

		for(int i = 0; i<tank.fish.size(); i++){
			Fish f = tank.fish.get(i);
			fishChoices.addItem(f.nickname + ": " + f.name, i); //TODO: enforce that no two fish of the same species can have the same nickname
		}

		fishInfo = new Textarea(infoPane, "fishInfo")
		.setSize(320, 300)
		.setPosition(fieldX-315, 85)
		.setFont(createFont("arial", 12))
		.moveTo("fishinfo");

		fishHappiness = new Slider(infoPane, "happiness")
		.setSize(100, 20)
		.setPosition(fieldX-315, 310)
		.setColorBackground(color(20, 20, 20))
		.setColorActive(color(60, 60, 60))
		.setColorForeground(color(60, 60, 60))
		.setLock(true)
		.moveTo("fishinfo")
		.hide();
		fishHappiness.valueLabel().hide();
		
		fishFullness = new Slider(infoPane, "hunger")
		.setSize(100, 20)
		.setPosition(fieldX-315, 340)
		.setColorBackground(color(20, 20, 20))
		.setColorActive(color(60, 60, 60))
		.setColorForeground(color(60, 60, 60))
		.setLock(true)
		.moveTo("fishinfo")
		.hide();
		fishFullness.valueLabel().hide();
		
		fishHealth = new Slider(infoPane, "health")
		.setSize(100, 20)
		.setPosition(fieldX-315, 370)
		.setColorBackground(color(20, 20, 20))
		.setColorActive(color(60, 60, 60))
		.setColorForeground(color(60, 60, 60))
		.setLock(true)
		.moveTo("fishinfo")
		.hide();
		fishHealth.valueLabel().hide();
		
		fishImage = new Button(infoPane, "fishImage")
		.setPosition(fieldX-135, 330)
		.moveTo("fishinfo")
		.hide();

		/**************************************************
		 * SAVE TAB INITIALIZATION *
		 *************************************************/

		infoPane.addButton("save")
		.setPosition(fieldX-260, 85)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0));

		infoPane.addButton("load")
		.setPosition(fieldX-145, 85)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0));

		infoPane.getController("save").moveTo("save");
		infoPane.getController("load").moveTo("save");

		/**************************************************
		 * HELP TAB INITIALIZATION *
		 *************************************************/

		infoPane.addButton("pH")
		.setPosition(fieldX-315, 85)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0));

		infoPane.addButton("temperature")
		.setPosition(fieldX-240, 85)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0));

		infoPane.addButton("hardness")
		.setPosition(fieldX-165, 85)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0));

		infoPane.addButton("nitrogenCycle")
		.setLabel("nitrogen cycle")
		.setPosition(fieldX-90, 85)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0));

		infoPane.addButton("bacteria")
		.setPosition(fieldX-315, 110)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0));

		infoPane.addButton("gases")
		.setPosition(fieldX-240, 110)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0));

		infoPane.addButton("waterChanges")
		.setLabel("water changes")
		.setPosition(fieldX-165, 110)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0));

		infoPane.addButton("fishHealth")
		.setLabel("fish health")
		.setPosition(fieldX-90, 110)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0));

		infoPane.getController("pH").moveTo("help");
		infoPane.getController("temperature").moveTo("help");
		infoPane.getController("hardness").moveTo("help");
		infoPane.getController("nitrogenCycle").moveTo("help");
		infoPane.getController("bacteria").moveTo("help");
		infoPane.getController("gases").moveTo("help");
		infoPane.getController("waterChanges").moveTo("help");
		infoPane.getController("fishHealth").moveTo("help");

		helpText = new Textarea(infoPane, "helpText")
		.setSize(320, fieldY-136)
		.setPosition(fieldX-315, 135)
		.setFont(createFont("arial", 12))
		.moveTo("help");	
	}

	public void draw(){
		Coordinate bcolor = backgroundColor();
		background(bcolor.x, bcolor.y, bcolor.z);
		int color = spotlightColor();
		spotLight(color, color, color, fieldX/4, 0, 400, 0, 0, -1, PI/4, 0);
		drawAllFish();
		drawTank();
		if(updateCount > 150){ //operations to happen every 5 seconds
			tank.progress(this);
			updateCount = 0;
		}
		updateCount++;
		interfaceUpdates();
		gui();
	}

	public void gui(){
		hint(DISABLE_DEPTH_TEST);
		camera.beginHUD();
		noLights();
		infoPane.draw();
		camera.endHUD();
		hint(ENABLE_DEPTH_TEST);
	}

	public void keyReleased(){
		if(keyCode == ESC) exit();
		if(key == ' '){
			System.out.println("how many minutes?");
			try{
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String input = br.readLine();
				System.out.println("skipping ahead " + input + " minutes");
				tank.skipAhead(this, Integer.parseInt(input));
			}catch(IOException io){
				io.printStackTrace();
			}
		}
		else if(key == 'q'){
			tank.fish.remove(0);
		}
	}

	public static void main(String args[]){
		PApplet.main(new String[] { "--present", "graphics.Visual" });
	}


	/**************************************************
	 * DRAWING *
	 *************************************************/

	public void interfaceUpdates(){
		switch(activeTab){
		case 0:
			break;
		case 1:
			updateTankInfo();
			break;
		case 2:
			updateFishInfo();
			break;
		case 3:
			break;
		case 4:
			break;
		}
	}

	public void restoreDefaults(){
		helpText.setText("What topic would you like to know more about?");
		fishChoice = -1;
		fishHappiness.hide();
		fishFullness.hide();
		fishHealth.hide();
		fishImage.hide();
	}

	public void updateTankInfo(){
		DecimalFormat format = new DecimalFormat("0.00");
		tankInfo.setText(format.format(tank.pH)
				+ "\n\n" + format.format(tank.temp) + " C"
				+ "\n\n" + format.format(tank.hardness) + " ppm"
				+ "\n\n" + format.format(tank.ammonia) + " ppm"
				+ "\n\n" + format.format(tank.nitrite) + " ppm"
				+ "\n\n" + format.format(tank.nitrate) + " ppm"
				+ "\n\n" + format.format(tank.o2) + "%"
				+ "\n\n" + format.format(tank.co2) + "%"
				+ "\n\n" + format.format(tank.nitrosomonas) + " bacteria"
				+ "\n\n" + format.format(tank.nitrobacter) + " bactera"
				+ "\n\n" + format.format(tank.food) + " noms"
				+ "\n\n" + format.format(tank.waste) + " poops");
	}

	public void updateFishInfo(){
		if(fishChoice == -1){
			fishInfo.setText("Select a fish below to see how it's doing.");
		}
		else{
			Fish f = tank.fish.get(fishChoice);
			fishInfo.setText(f.nickname + ": " + f.name
					+ "\n\nStatus: " + f.status.stringify()
					+ "\n\nAmmonia ranges tolerated: 0-" + f.ammonia + "ppm"
					+ "\nNitrite ranges tolerated: 0-" + f.nitrite + "ppm"
					+ "\nNitrite ranges tolerated: 0-" + f.nitrate + "ppm"
					+ "\npH ranges tolerated: " + f.minPH + "-" + f.maxPH
					+ "\nTemperature ranges tolerated: " + f.minTemp + "-" + f.maxTemp + " C"
					+ "\nHardness ranges tolerated: " + f.minHard + "-" + f.maxHard + " ppm"
					+ "\n\nHealth reduction will occur if happiness bar becomes empty."
					+ " Death will occur if health bar becomes empty."
					+ " Health can be regained if fish are kept consistently happy for a long time.");
			fishHappiness.setValue(f.happiness);
			fishFullness.setValue(Math.max(0, f.fullness));
			fishHealth.setValue(f.health);
		}
	}

	public void drawTank(){
		noStroke();
		pushMatrix();
		translate((int)(.4*fieldX), (int)(.8*fieldY), (int)(-fieldZ));
		translate(0, (int)(.5*fieldY),-1);
		fill(200, 180, 100);
		box(2*fieldX, fieldY, 0);
		translate(0, (int)(-.8*fieldY), 1);
		fill(255);
		box((int)(zoomPercentage*.8*fieldX), (int)(zoomPercentage*fieldY), 0);
		translate((int)(zoomPercentage*.4*fieldX), 0, (int)(zoomPercentage*.25*fieldZ));
		box(0, (int)(zoomPercentage*fieldY), (int)(zoomPercentage*.5*fieldZ));
		translate((int)(-zoomPercentage*.8*fieldX), 0, 0);
		box(0, (int)(zoomPercentage*fieldY), (int)(zoomPercentage*.5*fieldZ));
		translate((int)(zoomPercentage*.4*fieldX), (int)(zoomPercentage*.5*fieldY), 0);
		fill(200);
		box((int)(zoomPercentage*.8*fieldX), 0, (int)(zoomPercentage*.5*fieldZ));
		fill(0, 0, 255, 30);
		translate(0, (int)(-zoomPercentage*.5*fieldY) + (int)(zoomPercentage*fieldY*.5*(1-tank.waterLevel)), 0);
		box((int)(zoomPercentage*.8*fieldX), (int)(zoomPercentage*fieldY*tank.waterLevel), (int)(zoomPercentage*.5*fieldZ));
		popMatrix();
	}

	void drawFish(Fish fish){
		noStroke();
		pushMatrix();
		translate(3*fieldX/8, fieldY/2, -fieldZ+zoomPercentage*fieldZ/4);
		translate(fish.position.x, fish.position.y, fish.position.z);
		fish.model.draw();
		popMatrix();
	}

	void drawAllFish(){
		for(Fish f: this.tank.fish){
			drawFish(f);
		}
	}

	/**************************************************
	 * ACTION LISTENERS *
	 *************************************************/

	/*****CHANGE TAB OR LISTBOX ITEM*****/

	public void controlEvent(ControlEvent theControlEvent) {
		if (theControlEvent.isTab()) {
			restoreDefaults();
			switch(theControlEvent.getTab().getId()){
			case 0:
				activeTab = 0;
				break;
			case 1:
				activeTab = 1;
				break;
			case 2:
				activeTab = 2;
				break;
			case 3:
				activeTab = 3;
				break;
			case 4:
				activeTab = 4;
				break;
			}
		}
		if(theControlEvent.isGroup()){
			if(theControlEvent.group().id() == 0){
				fishChoice = (int) fishChoices.value();
				Fish choice = tank.fish.get(fishChoice);
				
				fishHappiness.show()
				.setMax(choice.maxHappyFull)
				.setValue(choice.happiness);
				
				fishFullness.show()
				.setMax(choice.maxHappyFull)
				.setValue(Math.max(0, choice.fullness));
				
				fishHealth.show()
				.setMax(choice.ease)
				.setValue(Math.max(0, choice.health));
				
				fishImage.show()
				.setImage(loadImage(choice.sprite));
			}
		}
	} 

	/*****OPTIONS MENU BUTTONS*****/
	
	void addFish(float theValue){

	}

	void feedFish(float theValue){

	}

	void cleanTank(float theValue){

	}

	void changeWater(float theValue){

	}
	
	/*****SAVE MENU BUTTONS*****/

	void save(float theValue){

	}

	void load(float theValue){

	}
	
	/*****HELP MENU BUTTONS*****/

	void pH(float theValue){
		helpText.setText("pH refers to the acidity of the tank water. "
				+ "Each fish has a range of pH's in which it can survive. "
				+ "pH will usually tend to rise naturally over time, but "
				+ "is lowered by the presence of waste and food in the "
				+ "water.");
	}

	void temperature(float theValue){
		helpText.setText("Water temperature is initially 24 degrees "
				+ "Celsius but will tend toward room temperature, which "
				+ "is set at 22 degrees. Each fish has a range of "
				+ "temperatures in which it can survive. If you need "
				+ "warmer water, try a water change. The added water "
				+ "is 24 degrees.");
	}

	void hardness(float theValue){
		helpText.setText("Water hardness is a measure of how many "
				+ "minerals are dissolved in the water. Each fish has "
				+ "a range of hardnesses that it prefers. Because the "
				+ "floor of the aquarium is covered in rocks, "
				+ "hardness tends to be proportionate to the size "
				+ "of the aquarium floor. It will fluctuate natrually.");
	}

	void nitrogenCycle(float theValue){
		helpText.setText("Waste and excess food break down into ammonia, "
				+ "which is toxic to fish. Bacteria will grow to convert "
				+ "ammonia into a less deadly substance, nitrite. A different "
				+ "sort of bacteria will convert nitrite into mostly harmless "
				+ "nitrates. Some kinds of fish tolerate these compounds better "
				+ "than others. Cut down on all of these compounds by keeping "
				+ "your tank clean!");
	}

	void bacteria(float theValue){
		helpText.setText("Nitrosomonas bacteria convert ammonia to nitrite. "
				+ "Nitrobacter bacteria convert nitrite to nitrate. The "
				+ "bacteria are harmless, and in fact their work is beneficial "
				+ "to your tank chemistry. Their populations rise when they have a "
				+ "lot of their corresponding chemicals to 'eat!'");
	}

	void gases(float theValue){
		helpText.setText("Dissolved O2 and CO2 are influenced by the ratio "
				+ "of fish to plants and the surface area of the tank's top. "
				+ "In the current version of the game, each tank has 1 plant. "
				+ "The fish are not directly affected by these levels, but their "
				+ "values do impact other aspects of tank chemistry. Increase "
				+ "the O2:CO2 ratio by keeping fewer fish or using a tank with "
				+ "a larger surface.");
	}

	void waterChanges(float theValue){
		helpText.setText("Water changes replace some percentage of the tank "
				+ "water with water that is the same as the beginning water. "
				+ "That is, a 100% water change will restore the water chemistry "
				+ "to its original state. This isn't necessarily a good thing! "
				+ "In its original state, water lacks the beneficial bacteria "
				+ "that fuel the nitrogen cycle. Also, some fish don't like water "
				+ "that is too 'neutral.' But smaller water changes can help keep "
				+ "a tank healthy.");
	}

	void fishHealth(float theValue){
		helpText.setText("This game has an added layer of complexity over most "
				+ "aquarium games in that it attempts to simulate real "
				+ "fish tank chemistry. For tips on keeping your fish "
				+ "happy and healthy, read this guide!\n\n"
				+ "Fish become unhappy when any aspect of the tank's water "
				+ "chemistry is outside their preferred ranges. They also "
				+ "become steadily hungrier with time. Hunger begins to affect "
				+ "a fish's wellbeing when its hunger bar reaches halfway. "
				+ "If a fish becomes too unhappy or hungry, it will lose some "
				+ "health. It can also lose health or die if placed in a tank "
				+ "with fish that bully it!\n\n"
				+ "If a fish is full and happy for an entire day, it will "
				+ "regain some health.");
	}

	/**************************************************
	 * HELPER FUNCTIONS *
	 *************************************************/

	public Coordinate backgroundColor(){
		int time = tank.time;
		return new Coordinate((int) ((160.0/1020.0)*(720-Math.abs(720-time) + 300)), (int) ((180.0/1020.0)*(720-Math.abs(720-time) + 300)), (int) ((200.0/1020.0)*(720-Math.abs(720-time) + 300)));
	}

	public int spotlightColor(){
		int time = tank.time;
		return (int) ((255.0/1020.0)*(720-Math.abs(720-time) + 300));
	}

}

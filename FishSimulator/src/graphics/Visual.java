package graphics;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;

import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.opengl.PGraphics3D;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ListBox;
import controlP5.Slider;
import controlP5.Textarea;
import controlP5.Textfield;
import engine.ClickMode;
import engine.CorruptedSaveFileException;
import engine.Food;
import engine.HappinessStatus;
import engine.Poop;
import engine.Sinkers;
import engine.Tank;
import engine.TankSize;
import engine.Vector3D;
import fish.Fish;
import fish.Guppy;

public class Visual extends PApplet{
	private static final long serialVersionUID = 1L;
	public final int fieldX = Toolkit.getDefaultToolkit().getScreenSize().width;
	public final int fieldY = Toolkit.getDefaultToolkit().getScreenSize().height;
	public final int fieldZ = 700;
	public final float zoomPercentage = (float) .85;
	final Fish[] speciesList = {new Guppy(this, "Swimmy")};

	ControlP5 infoPane;
	PeasyCam camera;
	PGraphics3D p3d;
	Selection_in_P3D_OPENGL_A3D picker;

	ListBox fishSpecies;
	Button speciesImage;
	Textarea speciesInfo;
	Textfield nicknameInput;
	Button confirmAdd;
	Textarea foodCleanInfo;
	Textarea waterChangeInfo;
	Slider percentWater;
	Button confirmWaterChange;
	Textarea tankInfo;
	public ListBox fishChoices;
	Textarea fishInfo;
	Button fishImage;
	Slider fishHappiness;
	Slider fishFullness;
	Slider fishHealth;
	Textfield fileNameInput;
	Button confirmSave;
	ListBox savedTanks;
	Button confirmLoad;
	Textarea newText;
	Button confirmNew;
	Textarea helpText;

	int updateCount = 60; //cause text to be updated less frequently
	public int fishChoice = -1;
	public int activeTab = 0;
	public ClickMode clickMode = ClickMode.INFO;

	public int tankMinX = 99999;
	public int tankMaxX = -1;
	public int tankMinY = 99999;
	public int tankMaxY = -1;

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

		/**************************************************
		 * CONTROLP5 SETTINGS *
		 *************************************************/

		infoPane = new ControlP5(this);
		infoPane.setAutoDraw(false);
		p3d = (PGraphics3D)g;
		picker = new Selection_in_P3D_OPENGL_A3D();

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

		/*****BUTTONS*****/

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

		/*****ADD FISH UI*****/

		fishSpecies = new ListBox(infoPane, "fishspecies")
		.setPosition(fieldX-315, 120)
		.setSize(295, 360)
		.setLabel("fish species")
		.setId(1)
		.disableCollapse()
		.moveTo("default")
		.hide();

		for(int i = 0; i < speciesList.length; i++){
			fishSpecies.addItem(speciesList[i].name, i);
		}

		speciesInfo = new Textarea(infoPane, "speciesInfo")
		.setSize(295, 150)
		.setPosition(fieldX-315, fieldY-380)
		.setFont(createFont("arial", 12))
		.moveTo("default")
		.hide();

		speciesImage = new Button(infoPane, "speciesImage")
		.setPosition(fieldX-135, fieldY-390)
		.moveTo("default")
		.hide();

		nicknameInput = new Textfield(infoPane, "nicknameInput")
		.setPosition(fieldX-315, fieldY-220)
		.setSize(310, 20)
		.hide();

		confirmAdd = new Button(infoPane, "confirmAdd")
		.setPosition(fieldX-315, fieldY-170)
		.setSize(310, 20)
		.align(CENTER, CENTER, CENTER, CENTER)
		.hide();

		/*****FEED / CLEAN UI*****/
		foodCleanInfo = new Textarea(infoPane, "foodCleanInfo")
		.setSize(295, 150)
		.setPosition(fieldX-315, 120)
		.setFont(createFont("arial", 12))
		.moveTo("default")
		.hide();

		/*****WATER CHANGE UI*****/

		waterChangeInfo = new Textarea(infoPane, "waterChangeInfo")
		.setSize(295, 100)
		.setPosition(fieldX-315, 120)
		.setFont(createFont("arial", 12))
		.setText("Changing the water can help bring chemistry back to neutral values,"
				+ " but be careful of over-changing since this will also get rid of some of"
				+ " the helpful bacteria that live in the water.")
				.moveTo("default")
				.hide();

		percentWater = new Slider(infoPane, "percentWater")
		.setSize(220, 20)
		.setPosition(fieldX-315, 200)
		.setColorBackground(color(20, 20, 20))
		.setColorActive(color(60, 60, 60))
		.setColorForeground(color(60, 60, 60))
		.moveTo("default")
		.setMax(100)
		.setMin(0)
		.setValue(50)
		.setLabel("")
		.setValueLabel("Change what percent of the water?")
		.hide();

		confirmWaterChange = new Button(infoPane, "confirmWaterChange")
		.setPosition(fieldX-80, 200)
		.setLabel("OK")
		.hide();

		/**************************************************
		 * TANKINFO TAB INITIALIZATION *
		 *************************************************/

		new Textarea(infoPane, "tankLabels")
		.setSize(295, fieldY-136)
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
		.setSize(295, fieldY-136)
		.setPosition(fieldX-150, 85)
		.setFont(createFont("arial", 12))
		.moveTo("tankinfo");

		/**************************************************
		 * FISHINFO TAB INITIALIZATION *
		 *************************************************/

		fishChoices = new ListBox(infoPane, "fishChoices")
		.setSize(295, fieldY-480)
		.setPosition(fieldX-315, 420)
		.setLabel("your fish")
		.setId(0)
		.disableCollapse()
		.moveTo("fishinfo");

		fishInfo = new Textarea(infoPane, "fishInfo")
		.setSize(295, 300)
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
		.setMin(0)
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
		.setMin(0)
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
		.setMin(0)
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
		.setPosition(fieldX-280, 85)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0))
		.moveTo("save");

		infoPane.addButton("load")
		.setPosition(fieldX-200, 85)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0))
		.moveTo("save");

		infoPane.addButton("newTank")
		.setPosition(fieldX-120, 85)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0))
		.setLabel("new")
		.moveTo("save");

		fileNameInput = new Textfield(infoPane, "fileNameInput")
		.setPosition(fieldX-315, 120)
		.setSize(210, 20)
		.moveTo("save")
		.hide();

		confirmSave = new Button(infoPane, "confirmSave")
		.setPosition(fieldX-90, 120)
		.setLabel("save")
		.moveTo("save")
		.hide();

		savedTanks = new ListBox(infoPane, "savedTanks")
		.setPosition(fieldX-315, 120)
		.setSize(295, 300)
		.setLabel("Your saved tanks")
		.setId(2)
		.disableCollapse()
		.moveTo("save")
		.hide();

		confirmLoad = new Button(infoPane, "confirmLoad")
		.setPosition(fieldX-315, 320)
		.setSize(295, 20)
		.setLabel("load")
		.align(CENTER, CENTER, CENTER, CENTER)
		.moveTo("save")
		.hide();

		newText = new Textarea(infoPane, "newText")
		.setSize(295, 60)
		.setPosition(fieldX-315, 120)
		.setFont(createFont("arial", 12))
		.setText("Are you sure you want to abandon this tank and start a new one? All unsaved changes to this tank will be lost.")
		.moveTo("save")
		.hide();

		confirmNew = new Button(infoPane, "confirmNew")
		.setPosition(fieldX-315, 180)
		.setSize(295, 20)
		.setLabel("OK")
		.align(CENTER, CENTER, CENTER, CENTER)
		.moveTo("save")
		.hide();

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
		.setSize(295, fieldY-136)
		.setPosition(fieldX-315, 135)
		.setFont(createFont("arial", 12))
		.moveTo("help");	

		determineBounds();
	}

	public void draw(){
		Vector3D bcolor = backgroundColor();
		background(bcolor.x, bcolor.y, bcolor.z);
		int color = spotlightColor();
		spotLight(color, color, color, fieldX/4, 0, 400, 0, 0, -1, PI/4, 0);
		drawAllFish();
		drawSinkers();
		drawTank();
		allEat();
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
				System.out.println("done calculating");
			}catch(IOException io){
				io.printStackTrace();
			}
		}
		else if(key == 'q'){
			tank.addPoop(this, this.tank.fish.getFirst());
		}
	}

	public void mouseReleased(){
		switch(clickMode){
		case FEED:
			picker.captureViewMatrix(p3d);
			picker.calculatePickPoints(mouseX,height-mouseY);
			PVector start = picker.ptStartPos;
			PVector end = picker.ptEndPos;
			if(mouseX >= tankMinX && mouseX <= tankMaxX && mouseY >= tankMinY && mouseY <= tankMaxY){
				tank.addFood(new Food(this, new Vector3D(start.x, start.y, start.z), new Vector3D(end.x, end.y, end.z)));
			}
			break;
		case CLEAN:
			picker.captureViewMatrix(p3d);
			picker.calculatePickPoints(mouseX,height-mouseY);
			start = picker.ptStartPos;
			end = picker.ptEndPos;
			removeWaste(new Vector3D(start.x, start.y, start.z), new Vector3D(end.x, end.y, end.z));
			break;
		case INFO:
			break;
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
		fishSpecies.hide();
		speciesImage.hide();
		speciesInfo.hide();
		nicknameInput.clear();
		nicknameInput.hide();
		confirmAdd.hide();
		waterChangeInfo.hide();
		percentWater.setValue(50)
		.setValueLabel("Change what percent of the water?")
		.hide();
		confirmWaterChange.hide();
		foodCleanInfo.hide();
		clickMode = ClickMode.INFO;
		fileNameInput.hide();
		fileNameInput.clear();
		confirmSave.hide();
		confirmLoad.hide();
		savedTanks.hide();
		confirmNew.hide();
		newText.hide();
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
				+ "\n\n" + format.format(tank.food.size()) + " noms"
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
					+ "\n\nAmmonia ranges tolerated: 0-" + f.ammonia + " ppm"
					+ "\nNitrite ranges tolerated: 0-" + f.nitrite + " ppm"
					+ "\nNitrite ranges tolerated: 0-" + f.nitrate + " ppm"
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
		fill(254);
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
		translate((int)(.4*fieldX), (int)(.5*fieldY)+(int)(zoomPercentage*fieldY*.5*(1-tank.waterLevel)), (int)(-fieldZ)+(int)(zoomPercentage*.25*fieldZ));
		translate(fish.position.x, fish.position.y, fish.position.z);
		rotateX(fish.orientation.x);
		rotateY(fish.orientation.y);
		rotateZ(fish.orientation.z);
		fish.model.draw();
		popMatrix();
		fish.updatePosition(this);
	}

	void drawAllFish(){
		for(Fish f: this.tank.fish){
			drawFish(f);
		}
	}

	public void drawSinkers(){
		for(Poop p: this.tank.poops){
			drawWaste(p);
			p.updatePosition();
		}
		for(Food f: this.tank.food){
			drawWaste(f);
			f.updatePosition();
		}
	}

	public void drawWaste(Sinkers s){
		noStroke();
		pushMatrix();
//		translate((int)(.4*fieldX), (int)(.5*fieldY)+(int)(zoomPercentage*fieldY*.5*(1-tank.waterLevel)), (int)(-fieldZ)+(int)(zoomPercentage*.25*fieldZ));
		translate(s.absolutePosition.x, s.absolutePosition.y, s.absolutePosition.z);
		fill(s.color.x, s.color.y, s.color.z);
		sphere(s.dimensions.x);
		popMatrix();		
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
			// fishinfo listbox
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
			// speciesinfo listbox
			if(theControlEvent.group().id() == 1){
				int species = (int) fishSpecies.value();
				Fish choice = speciesList[species];

				speciesInfo.show()
				.setText("Species: " + choice.name
						+ "\nEase of care: " + choice.ease + "/5"
						+ "\nSize: " + choice.size + "cm"
						+ "\nAmmonia ranges tolerated: 0-" + choice.ammonia + " ppm"
						+ "\nNitrite ranges tolerated: 0-" + choice.nitrite + " ppm"
						+ "\nNitrite ranges tolerated: 0-" + choice.nitrate + " ppm"
						+ "\npH ranges tolerated: " + choice.minPH + "-" + choice.maxPH
						+ "\nTemperature ranges tolerated: " + choice.minTemp + "-" + choice.maxTemp + " C"
						+ "\nHardness ranges tolerated: " + choice.minHard + "-" + choice.maxHard + " ppm");

				speciesImage.show()
				.setImage(loadImage(choice.sprite));

				nicknameInput.show()
				.setCaptionLabel("Choose a nickname!");	

				confirmAdd.show()
				.setCaptionLabel("Add " + choice.name);
			}
			// savedTanks listbox
			if(theControlEvent.group().id() == 2){
				String filename = compileFileList().get((int) savedTanks.value());
				try{
					confirmLoad.setLabel("Tank " + filename + " last saved: " + new Date(getDateSaved(filename)).toLocaleString());
				}
				catch(CorruptedSaveFileException e){
					confirmLoad.setLabel("Tank " + filename + " seems to be corrupted.");
				}
			}
		}
	} 

	/*****OPTIONS MENU BUTTONS*****/

	void addFish(float theValue){
		restoreDefaults();
		fishSpecies.show();
	}

	void feedFish(float theValue){
		restoreDefaults();
		foodCleanInfo.show()
		.setText("Click inside the tank to drop food pellets.");
		clickMode = ClickMode.FEED;
	}

	void cleanTank(float theValue){
		restoreDefaults();
		foodCleanInfo.show()
		.setText("Click on visible waste to remove it from the tank.");
		clickMode = ClickMode.CLEAN;
	}

	void changeWater(float theValue){
		restoreDefaults();
		waterChangeInfo.show();
		percentWater.show();
		confirmWaterChange.show();
	}

	void confirmAdd(float theValue){
		String nickname = nicknameInput.getText();
		if(nickname.equals("")){
			nicknameInput.setCaptionLabel("Please input a nickname for your fish!");	
		}
		else if(nickname.length() > 20){
			nicknameInput.setCaptionLabel("Name too long! Please choose another a name under 20 characters.");
		}
		else if(!tank.validateNickname(nickname)){
			nicknameInput.setCaptionLabel("You already have a fish with that name! Please choose another name.");			
		}
		else{
			String species = speciesList[(int) fishSpecies.value()].name;
			addFishToTank(species, nickname);
			nicknameInput.clear();
			nicknameInput.setCaptionLabel(species + " named " +  nickname + " added!");
		}
	}

	void confirmWaterChange(float theValue){
		percentWater.setValueLabel(percentWater.getValue() + "% water change performed");
		tank.waterChange(percentWater.getValue());
	}

	/*****SAVE MENU BUTTONS*****/

	void save(float theValue){
		restoreDefaults();
		fileNameInput.show()
		.setCaptionLabel("Give this tank a name!");
		confirmSave.show();
	}

	void load(float theValue){
		restoreDefaults();
		savedTanks.show()
		.clear();
		confirmLoad.show();
		LinkedList<String> files = compileFileList();
		for(int i = 0; i < files.size(); i++){
			savedTanks.addItem(files.get(i), i);
		}
	}

	void newTank(float theValue){
		restoreDefaults();
		newText.show();
		confirmNew.show();
	}

	void confirmNew(float theValue){
		this.tank = new Tank(TankSize.MEDIUM);
		confirmNew.setLabel("New tank created!");
	}

	void confirmSave(float theValue){
		try{
			String fileName = fileNameInput.getText();
			if(compileFileList().contains(fileName) &&
					fileNameInput.getCaptionLabel().getText().equals("You already have a tank with this name. Click again to overwrite.")){
				createSaveFile(fileNameInput.getText());
				fileNameInput.setCaptionLabel("Tank saved!");
			}
			else if(fileName.equals("")){
				fileNameInput.setCaptionLabel("Please enter a name for the tank.");
			}
			else if(fileName.length() > 20){
				fileNameInput.setCaptionLabel("Name too long! Please choose another a name under 20 characters.");
			}
			else if(compileFileList().contains(fileName)){
				fileNameInput.setCaptionLabel("You already have a tank with this name. Click again to overwrite.");
			}
			else{
				createSaveFile(fileNameInput.getText());
				fileNameInput.setCaptionLabel("Tank saved!");
			}
		}
		catch(CorruptedSaveFileException e){
			fileNameInput.setCaptionLabel("Problem saving tank.");
		}
	}

	void confirmLoad(float theValue){
		String filename = compileFileList().get((int) savedTanks.getValue());
		try{
			confirmLoad.setLabel("Loading...");
			draw();
			this.tank = parseFile(getFileText(filename));
			Long elapsed = System.currentTimeMillis() - getDateSaved(filename);
			int iterations = (int) (elapsed/5000.0);
			this.tank.skipAhead(this, iterations);
			confirmLoad.setLabel("Tank " + filename + " loaded!");
		}
		catch(CorruptedSaveFileException e){
			confirmLoad.setLabel("Tank " + filename + " seems to be corrupted.");
		}
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

	public Vector3D backgroundColor(){
		int time = tank.time;
		return new Vector3D((int) ((160.0/1020.0)*(720-Math.abs(720-time) + 300)), (int) ((180.0/1020.0)*(720-Math.abs(720-time) + 300)), (int) ((200.0/1020.0)*(720-Math.abs(720-time) + 300)));
	}

	public int spotlightColor(){
		int time = tank.time;
		return (int) ((255.0/1020.0)*(720-Math.abs(720-time) + 300));
	}

	public void addFishToTank(String speciesName, String nickname){
		Fish toAdd = null;
		switch(speciesName){
		case "Guppy":
			toAdd = new Guppy(this, nickname);
			tank.addFish(toAdd);
			break;
		}
		fishChoices.addItem(toAdd.nickname + ": " + toAdd.name, tank.fish.size()-1);
	}

	public void createSaveFile(String name) throws CorruptedSaveFileException{
		BufferedWriter writer = null;
		File txt = new File(name + ".txt");		
		try {
			writer = new BufferedWriter(new FileWriter(txt));
			writer.write(compileSaveText());
			writer.close();
		} catch (IOException e) {
			throw new CorruptedSaveFileException();
		}
	}

	public String compileSaveText(){
		String saveText = "Start tank info\n";
		saveText += System.currentTimeMillis() + "\n";
		saveText += tank.plants + "\n";
		saveText += tank.length + "\n";
		saveText += tank.width + "\n";
		saveText += tank.height + "\n";
		saveText += tank.pH + "\n";
		saveText += tank.temp + "\n";
		saveText += tank.hardness + "\n";
		saveText += tank.o2 + "\n";
		saveText += tank.co2 + "\n";
		saveText += tank.ammonia + "\n";
		saveText += tank.nitrite + "\n";
		saveText += tank.nitrate + "\n";
		saveText += tank.nitrosomonas + "\n";
		saveText += tank.nitrobacter + "\n";
		saveText += tank.waste + "\n";
		saveText += "Start poops info\n";
		for(int i = 0; i < tank.poops.size(); i++){
			Poop p = tank.poops.get(i);
			saveText += p.position.toCommaSeparated();
			saveText += p.dimensions.toCommaSeparated();
		}
		saveText += "Start food info\n";
		for(int i = 0; i < tank.food.size(); i++){
			Food f = tank.food.get(i);
			saveText += f.position.toCommaSeparated();
		}
		saveText += "Start fish info\n";
		for(int i = 0; i < tank.fish.size(); i++){
			Fish f = tank.fish.get(i);
			saveText += f.compileSaveText();
		}
		return saveText;
	}

	public LinkedList<String> compileFileList(){
		LinkedList<String> saveFiles = new LinkedList<String>();
		File folder = new File(System.getProperty("user.dir"));
		for(File f: folder.listFiles()){
			if(f.getName().endsWith(".txt")){
				saveFiles.add(f.getName().substring(0, f.getName().length()-4));
			}
		}
		return saveFiles;
	}

	public int indexOf(String[] array, String seeking) throws CorruptedSaveFileException{
		for(int i = 0; i < array.length; i++){
			if(array[i].equals(seeking)) return i;
		}
		throw new CorruptedSaveFileException();
	}

	public HappinessStatus parseStatus(String string){
		switch(string){
		case "HAPPY":
			return HappinessStatus.HAPPY;
		case "HUNGRY":
			return HappinessStatus.HUNGRY;
		case "AMMONIA":
			return HappinessStatus.AMMONIA;
		case "NITRITE":
			return HappinessStatus.NITRITE;
		case "NITRATE":
			return HappinessStatus.NITRATE;
		case "PHLOW":
			return HappinessStatus.PHLOW;
		case "PHHIGH":
			return HappinessStatus.PHHIGH;
		case "TEMPLOW":
			return HappinessStatus.TEMPLOW;
		case "TEMPHIGH":
			return HappinessStatus.TEMPHIGH;
		case "HARDLOW":
			return HappinessStatus.HARDLOW;
		default:
			return HappinessStatus.HARDHIGH;
		}
	}

	public String getFileText(String filename) throws CorruptedSaveFileException{
		filename += ".txt";
		String text = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			while(line != null){
				text += line + "\n";
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			throw new CorruptedSaveFileException();
		}
		return text;
	}

	public long getDateSaved(String filename) throws CorruptedSaveFileException{
		filename += ".txt";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			line = reader.readLine();
			line = reader.readLine();
			reader.close();
			return Long.parseLong(line);
		} catch (Exception e) {
			throw new CorruptedSaveFileException();
		}		
	}

	public Fish makeFish(String[] array, int start) throws CorruptedSaveFileException{
		switch(array[start]){
		case "Guppy":
			return new Guppy(this,
					array[start+1],
					parseStatus(array[start+2]),
					Long.parseLong(array[start+3]),
					Long.parseLong(array[start+4]),
					Long.parseLong(array[start+5]),
					Integer.parseInt(array[start+6]),
					new Vector3D(array[start+7]),
					new Vector3D(array[start+8]));
		default:
			throw new CorruptedSaveFileException();
		}
	}

	public Tank parseFile(String text) throws CorruptedSaveFileException{
		try{
			String[] lines = text.split("\n");
			int start = indexOf(lines, "Start poops info")+1;
			int end = indexOf(lines, "Start food info");
			LinkedList<Poop> poops = new LinkedList<Poop>();
			for(int i = start; i < end; i+=2){
				poops.add(new Poop(this, new Vector3D(lines[i]), new Vector3D(lines[i+1])));
			}
			start = indexOf(lines, "Start food info")+1;
			end = indexOf(lines, "Start fish info");
			LinkedList<Food> food = new LinkedList<Food>();
			for(int i = start; i < end; i++){
				food.add(new Food(this, new Vector3D(lines[i])));
			}
			start = indexOf(lines, "Start fish info")+1;
			end = lines.length;
			LinkedList<Fish> fish = new LinkedList<Fish>();
			for(int i = start; i < end; i+=9){
				fish.add(makeFish(lines, i));
			}
			Tank tank = new Tank(Double.parseDouble(lines[2]),
					Integer.parseInt(lines[3]),
					Integer.parseInt(lines[4]),
					Integer.parseInt(lines[5]),
					Double.parseDouble(lines[6]),
					Double.parseDouble(lines[7]),
					Double.parseDouble(lines[8]),
					Double.parseDouble(lines[9]),
					Double.parseDouble(lines[10]),
					Double.parseDouble(lines[11]),
					Double.parseDouble(lines[12]),
					Double.parseDouble(lines[13]),
					Double.parseDouble(lines[14]),
					Double.parseDouble(lines[15]),
					Integer.parseInt(lines[16]),
					poops, food, fish);
			return tank;
		}
		catch(CorruptedSaveFileException e){
			throw new CorruptedSaveFileException();
		}
	}
	
	public void allEat(){
		for(Fish fish: this.tank.fish){
			for(Food food: this.tank.food){
				this.tank.eat(fish, food);
			}
		}
	}

	public void determineBounds(){
		noLights();
		drawTank();
		loadPixels();
		draw();
		int x = 0;
		int y = 0;
		for(int i: pixels){
			if(i == -438181377){
				if(x < tankMinX) tankMinX = x;
				if(x > tankMaxX) tankMaxX = x;
				if(y < tankMinY) tankMinY = y;
				if(y > tankMaxY) tankMaxY = y;
			}
			x++;
			if(x >= fieldX){
				x = 0;
				y++;
			}
		}
	}

	public void removeWaste(Vector3D start, Vector3D end){
		Vector3D normal = end.addVector(start.multiplyScalar(-1)).normalize();
		Sinkers closest = null;
		float z = -1000;
		for(Poop p: tank.poops){
			if(raySphereIntersect(start, normal, p.absolutePosition, p.dimensions.x)){
				if(p.absolutePosition.z > z){
					z = p.absolutePosition.z;
					closest = p;
				} 
			}
		}
		for(Food f: tank.food){
			if(raySphereIntersect(start, normal, f.absolutePosition, f.dimensions.x)){
				if(f.absolutePosition.z > z){
					z = f.absolutePosition.z;
					closest = f;
				}
			}
		}
		if(closest != null){
			closest.removeFromTank(this.tank);
		}
	}

	public boolean raySphereIntersect(Vector3D rayOrigin, Vector3D rayNormal, Vector3D sphereCenter, float sphereRadius){
		double determinant = Math.pow(rayNormal.dotProduct(rayOrigin.addVector(sphereCenter.multiplyScalar(-1))), 2) - Math.pow(rayOrigin.addVector(sphereCenter.multiplyScalar(-1)).magnitude(), 2) + Math.pow(sphereRadius, 2);
		if(determinant < 0) return false;
		else return true;
	}

	public class Selection_in_P3D_OPENGL_A3D
	{

		// True if near and far points calculated.
		public boolean isValid() { return m_bValid; }
		private boolean m_bValid = false;

		// Maintain own projection matrix.
		public PMatrix3D getMatrix() { return m_pMatrix; }
		private PMatrix3D m_pMatrix = new PMatrix3D();

		// Maintain own viewport data.
		public int[] getViewport() { return m_aiViewport; }
		private int[] m_aiViewport = new int[4];

		// Store the near and far ray positions.
		public PVector ptStartPos = new PVector();
		public PVector ptEndPos = new PVector();

		// -------------------------

		public void captureViewMatrix(PGraphics3D g3d)
		{ // Call this to capture the selection matrix after 
			// you have called perspective() or ortho() and applied your
			// pan, zoom and camera angles - but before you start drawing
			// or playing with the matrices any further.

			if (g3d == null)
			{ // Use main canvas if it is P3D, OPENGL or A3D.
				g3d = (PGraphics3D)g;
			}

			if (g3d != null)
			{ // Check for a valid 3D canvas.

				// Capture current projection matrix.
				m_pMatrix.set(g3d.projection);

				// Multiply by current modelview matrix.
				m_pMatrix.apply(g3d.modelview);

				// Invert the resultant matrix.
				m_pMatrix.invert();

				// Store the viewport.
				m_aiViewport[0] = 0;
				m_aiViewport[1] = 0;
				m_aiViewport[2] = g3d.width;
				m_aiViewport[3] = g3d.height;

			}

		}

		// -------------------------

		public boolean gluUnProject(float winx, float winy, float winz, PVector result)
		{

			float[] in = new float[4];
			float[] out = new float[4];

			// Transform to normalized screen coordinates (-1 to 1).
			in[0] = ((winx - (float)m_aiViewport[0]) / (float)m_aiViewport[2]) * 2.0f - 1.0f;
			in[1] = ((winy - (float)m_aiViewport[1]) / (float)m_aiViewport[3]) * 2.0f - 1.0f;
			in[2] = constrain(winz, 0f, 1f) * 2.0f - 1.0f;
			in[3] = 1.0f;

			// Calculate homogeneous coordinates.
			out[0] = m_pMatrix.m00 * in[0]
					+ m_pMatrix.m01 * in[1]
							+ m_pMatrix.m02 * in[2]
									+ m_pMatrix.m03 * in[3];
			out[1] = m_pMatrix.m10 * in[0]
					+ m_pMatrix.m11 * in[1]
							+ m_pMatrix.m12 * in[2]
									+ m_pMatrix.m13 * in[3];
			out[2] = m_pMatrix.m20 * in[0]
					+ m_pMatrix.m21 * in[1]
							+ m_pMatrix.m22 * in[2]
									+ m_pMatrix.m23 * in[3];
			out[3] = m_pMatrix.m30 * in[0]
					+ m_pMatrix.m31 * in[1]
							+ m_pMatrix.m32 * in[2]
									+ m_pMatrix.m33 * in[3];

			if (out[3] == 0.0f)
			{ // Check for an invalid result.
				result.x = 0.0f;
				result.y = 0.0f;
				result.z = 0.0f;
				return false;
			}

			// Scale to world coordinates.
			out[3] = 1.0f / out[3];
			result.x = out[0] * out[3];
			result.y = out[1] * out[3];
			result.z = out[2] * out[3];
			return true;

		}

		public boolean calculatePickPoints(int x, int y)
		{ // Calculate positions on the near and far 3D frustum planes.
			m_bValid = true; // Have to do both in order to reset PVector on error.
			if (!gluUnProject((float)x, (float)y, 0.0f, ptStartPos)) m_bValid = false;
			if (!gluUnProject((float)x, (float)y, 1.0f, ptEndPos)) m_bValid = false;
			return m_bValid;
		}

	}

}

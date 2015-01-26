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
import engine.DeadFish;
import engine.Food;
import engine.HappinessStatus;
import engine.Poop;
import engine.Tank;
import engine.TankSize;
import engine.Vector3D;
import engine.Waste;
import fish.CherryBarb;
import fish.CherryShrimp;
import fish.DwarfPuffer;
import fish.Fish;
import fish.Guppy;
import fish.IncaSnail;
import fish.Plant;
import fish.TallPlant;
import fish.WhiteCloudMountainMinnow;

public class Visual extends PApplet{
	private static final long serialVersionUID = 1L;
	public final static int fieldX = Toolkit.getDefaultToolkit().getScreenSize().width;
	public final static int fieldY = Toolkit.getDefaultToolkit().getScreenSize().height;
	public final static int fieldZ = (int)(1200-fieldX*.2-fieldY*.2);
	public final static float zoomPercentage = (float) .85;

	ControlP5 infoPane;
	PeasyCam camera;
	PGraphics3D p3d;
	Selection_in_P3D_OPENGL_A3D picker;

	ListBox fishSpecies;
	ListBox plantSpecies;
	Button speciesImage;
	Textarea speciesInfo;
	Textfield nicknameInput;
	Button confirmAdd;
	Button confirmPlant;
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
	boolean loading = false;
	Plant previewPlant = null;

	public int backMinX = 99999;
	public int backMaxX = -1;
	public int backMaxY = -1;
	public int leftMinX = 99999;
	public int rightMaxX = -1;
	public int sidesMaxY = -1;

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

		infoPane.window().setPositionOfTabs(fieldX-320, 0);

		infoPane.addTab("fishinfo")
		.setTitle("fish info")
		.setId(1)
		.setColorBackground(color(50, 50, 50))
		.setColorLabel(color(255))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(35, 35, 35))
		.activateEvent(true);

		infoPane.getTab("default")
		.setId(0)
		.setTitle("tank info")
		.setColorBackground(color(50, 50, 50))
		.setColorLabel(color(255))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(35, 35, 35))
		.activateEvent(true);

		infoPane.addTab("add")
		.setTitle("add")
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
		.setSize(320, fieldY)
		.setPosition(fieldX-320, 16);

		infoPane.addButton(" ")
		.setColorBackground(color(90, 90, 90))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(90, 90, 90))
		.setSize(320, fieldY)
		.setPosition(fieldX-320, 16);

		infoPane.addButton("  ")
		.setColorBackground(color(90, 90, 90))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(90, 90, 90))
		.setSize(320, fieldY)
		.setPosition(fieldX-320, 16);

		infoPane.addButton("   ")
		.setColorBackground(color(90, 90, 90))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(90, 90, 90))
		.setSize(320, fieldY)
		.setPosition(fieldX-320, 16);

		infoPane.addButton("    ")
		.setColorBackground(color(90, 90, 90))
		.setColorActive(color(90, 90, 90))
		.setColorForeground(color(90, 90, 90))
		.setSize(320, fieldY)
		.setPosition(fieldX-320, 16);

		infoPane.getController(" ").moveTo("fishinfo");
		infoPane.getController("  ").moveTo("add");
		infoPane.getController("   ").moveTo("save");
		infoPane.getController("    ").moveTo("help");

		/**************************************************
		 * TANKINFO TAB INITIALIZATION *
		 *************************************************/

		new Button(infoPane, "tankInfoBanner")
		.setSize(295, 20)
		.setPosition(fieldX-315, 25)
		.setColorBackground(color(50, 50, 50))
		.setColorActive(color(50, 50, 50))
		.setColorForeground(color(50, 50, 50))
		.setLabel("Tank Status");

		new Textarea(infoPane, "tankLabels")
		.setSize(295, 400)
		.setPosition(fieldX-315, 60)
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
				.moveTo("default");

		tankInfo = new Textarea(infoPane, "tankInfo")
		.setSize(295, 400)
		.setPosition(fieldX-150, 60)
		.setFont(createFont("arial", 12))
		.moveTo("default");

		/*****CLEAN UI*****/

		new Button(infoPane, "cleanBanner")
		.setSize(295, 20)
		.setPosition(fieldX-315, 370)
		.setColorBackground(color(50, 50, 50))
		.setColorActive(color(50, 50, 50))
		.setColorForeground(color(50, 50, 50))
		.setLabel("Cleaning the tank");

		new Textarea(infoPane, "cleanInfo")
		.setSize(295, 150)
		.setPosition(fieldX-315, 400)
		.setFont(createFont("arial", 12))
		.setText("On this screen, click on visible waste to remove it from the tank.")
		.moveTo("default");

		/*****WATER CHANGE UI*****/

		new Button(infoPane, "waterChangeBanner")
		.setSize(295, 20)
		.setPosition(fieldX-315, 440)
		.setColorBackground(color(50, 50, 50))
		.setColorActive(color(50, 50, 50))
		.setColorForeground(color(50, 50, 50))
		.setLabel("Water changes");

		waterChangeInfo = new Textarea(infoPane, "waterChangeInfo")
		.setSize(295, 100)
		.setPosition(fieldX-315, 470)
		.setFont(createFont("arial", 12))
		.setText("Changing the water can help bring chemistry back to neutral values,"
				+ " but be careful of over-changing since this will also get rid of some of"
				+ " the helpful bacteria that live in the water.")
				.moveTo("default");

		percentWater = new Slider(infoPane, "percentWater")
		.setSize(220, 20)
		.setPosition(fieldX-315, 540)
		.setColorBackground(color(20, 20, 20))
		.setColorActive(color(60, 60, 60))
		.setColorForeground(color(60, 60, 60))
		.moveTo("default")
		.setMax(100)
		.setMin(0)
		.setValue(50)
		.setLabel("")
		.setValueLabel("Change what percent of the water?");

		confirmWaterChange = new Button(infoPane, "confirmWaterChange")
		.setPosition(fieldX-80, 540)
		.setLabel("OK");

		/**************************************************
		 * FISHINFO TAB INITIALIZATION *
		 *************************************************/

		/*****CHOICES UI*****/

		new Button(infoPane, "fishInfoBanner")
		.setSize(295, 20)
		.setPosition(fieldX-315, 25)
		.setLabel("Fish Status")
		.setColorBackground(color(50, 50, 50))
		.setColorActive(color(50, 50, 50))
		.setColorForeground(color(50, 50, 50))
		.moveTo("fishinfo");

		fishChoices = new ListBox(infoPane, "fishChoices")
		.setSize(295, 120)
		.setPosition(fieldX-315, 90)
		.setLabel("your fish")
		.setId(0)
		.disableCollapse()
		.moveTo("fishinfo");

		new Textarea(infoPane, "fishInstructions")
		.setSize(295, 30)
		.setPosition(fieldX-315, 50)
		.setFont(createFont("arial", 12))
		.setText("Select a fish below to see how it's doing.")
		.moveTo("fishinfo");

		/*****STATUS UI*****/

		fishInfo = new Textarea(infoPane, "fishInfo")
		.setSize(295, 300)
		.setPosition(fieldX-315, 340)
		.setFont(createFont("arial", 12))
		.setText("")
		.moveTo("fishinfo");

		fishHappiness = new Slider(infoPane, "happiness")
		.setSize(100, 20)
		.setPosition(fieldX-315, 250)
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
		.setPosition(fieldX-315, 280)
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
		.setPosition(fieldX-315, 310)
		.setColorBackground(color(20, 20, 20))
		.setColorActive(color(60, 60, 60))
		.setColorForeground(color(60, 60, 60))
		.setLock(true)
		.setMin(0)
		.moveTo("fishinfo")
		.hide();
		fishHealth.valueLabel().hide();

		fishImage = new Button(infoPane, "fishImage")
		.setPosition(fieldX-155, 260)
		.moveTo("fishinfo")
		.hide();

		/*****FEEDING UI*****/

		new Button(infoPane, "feedBanner")
		.setSize(295, 20)
		.setPosition(fieldX-315, 560)
		.setLabel("Feeding your fish")
		.setColorBackground(color(50, 50, 50))
		.setColorActive(color(50, 50, 50))
		.setColorForeground(color(50, 50, 50))
		.moveTo("fishinfo");

		new Textarea(infoPane, "feedInfo")
		.setSize(295, 150)
		.setPosition(fieldX-315, 590)
		.setFont(createFont("arial", 12))
		.setText("On this screen, click anywhere in the tank to drop food.")
		.moveTo("fishinfo");

		/**************************************************
		 * ADD TAB INITIALIZATION *
		 *************************************************/

		/*****ADD FISH UI*****/

		new Button(infoPane, "addFishBanner")
		.setSize(295, 20)
		.setPosition(fieldX-315, 25)
		.setLabel("Add fish")
		.setColorBackground(color(50, 50, 50))
		.setColorActive(color(50, 50, 50))
		.setColorForeground(color(50, 50, 50))
		.moveTo("add");

		fishSpecies = new ListBox(infoPane, "fishspecies")
		.setPosition(fieldX-315, 60)
		.setSize(295, 120)
		.setLabel("fish species")
		.setId(1)
		.disableCollapse()
		.moveTo("add");

		Fish[] speciesList = getFishSpeciesList();
		for(int i = 0; i < speciesList.length; i++){
			fishSpecies.addItem(speciesList[i].name, i);
		}

		/*****ADD PLANT UI*****/

		new Button(infoPane, "addPlantBanner")
		.setSize(295, 20)
		.setPosition(fieldX-315, 210)
		.setLabel("Add Plant")
		.setColorBackground(color(50, 50, 50))
		.setColorActive(color(50, 50, 50))
		.setColorForeground(color(50, 50, 50))
		.moveTo("add");

		plantSpecies = new ListBox(infoPane, "plantSpecies")
		.setPosition(fieldX-315, 245)
		.setSize(295, 120)
		.setLabel("plant species")
		.setId(3)
		.disableCollapse()
		.moveTo("add");

		Plant[] plantList = getPlantSpeciesList();
		for(int i = 0; i < plantList.length; i++){
			plantSpecies.addItem(plantList[i].name, i);
		}
		
		confirmPlant = new Button(infoPane, "confirmPlant")
		.setPosition(fieldX-315, 640)
		.setSize(310, 20)
		.align(CENTER, CENTER, CENTER, CENTER)
		.moveTo("add")
		.hide();

		/*****PREVIEW UI*****/

		speciesInfo = new Textarea(infoPane, "speciesInfo")
		.setSize(295, 150)
		.setPosition(fieldX-315, 460)
		.setFont(createFont("arial", 12))
		.moveTo("add")
		.hide();

		speciesImage = new Button(infoPane, "speciesImage")
		.setPosition(fieldX-245, 380)
		.moveTo("add")
		.hide();

		nicknameInput = new Textfield(infoPane, "nicknameInput")
		.setPosition(fieldX-315, 590)
		.setSize(310, 20)
		.moveTo("add")
		.hide();

		confirmAdd = new Button(infoPane, "confirmAdd")
		.setPosition(fieldX-315, 640)
		.setSize(310, 20)
		.align(CENTER, CENTER, CENTER, CENTER)
		.moveTo("add")
		.hide();


		/**************************************************
		 * SAVE TAB INITIALIZATION *
		 *************************************************/

		/*****SAVING UI*****/

		new Button(infoPane, "saveBanner")
		.setSize(295, 20)
		.setPosition(fieldX-315, 25)
		.setLabel("Save your tank")
		.setColorBackground(color(50, 50, 50))
		.setColorActive(color(50, 50, 50))
		.setColorForeground(color(50, 50, 50))
		.moveTo("save");

		fileNameInput = new Textfield(infoPane, "fileNameInput")
		.setPosition(fieldX-315, 60)
		.setSize(210, 20)
		.setCaptionLabel("Give this tank a name!")
		.setText(this.tank.name)
		.moveTo("save");

		confirmSave = new Button(infoPane, "confirmSave")
		.setPosition(fieldX-90, 60)
		.setLabel("save")
		.moveTo("save");
		
		new Textarea(infoPane, "saveInfo")
		.setSize(295, 150)
		.setPosition(fieldX-315, 105)
		.setFont(createFont("arial", 12))
		.setText("Make sure to check in on saved tanks frequently! "
				+ "Time passes in the tank even when you're not playing.")
		.moveTo("save");

		/*****LOADING UI******/

		new Button(infoPane, "loadBanner")
		.setSize(295, 20)
		.setPosition(fieldX-315, 160)
		.setLabel("Load a tank")
		.setColorBackground(color(50, 50, 50))
		.setColorActive(color(50, 50, 50))
		.setColorForeground(color(50, 50, 50))
		.moveTo("save");

		savedTanks = new ListBox(infoPane, "savedTanks")
		.setPosition(fieldX-315, 200)
		.setSize(295, 150)
		.setLabel("Your saved tanks")
		.setId(2)
		.disableCollapse()
		.moveTo("save");

		confirmLoad = new Button(infoPane, "confirmLoad")
		.setPosition(fieldX-315, 360)
		.setSize(295, 20)
		.setLabel("load")
		.align(CENTER, CENTER, CENTER, CENTER)
		.moveTo("save");

		/*****NEW TANK UI*****/

		new Button(infoPane, "newBanner")
		.setSize(295, 20)
		.setPosition(fieldX-315, 400)
		.setLabel("Make a new tank")
		.setColorBackground(color(50, 50, 50))
		.setColorActive(color(50, 50, 50))
		.setColorForeground(color(50, 50, 50))
		.moveTo("save");

		new Textarea(infoPane, "newInfo")
		.setSize(295, 150)
		.setPosition(fieldX-315, 430)
		.setFont(createFont("arial", 12))
		.setText("Creating a new tank will cause any unsaved changes on this tank to be lost.")
		.moveTo("save");

		confirmNew = new Button(infoPane, "confirmNew")
		.setPosition(fieldX-315, 470)
		.setSize(295, 20)
		.setLabel("OK")
		.align(CENTER, CENTER, CENTER, CENTER)
		.moveTo("save");

		/**************************************************
		 * HELP TAB INITIALIZATION *
		 *************************************************/

		infoPane.addButton("pH")
		.setPosition(fieldX-315, 25)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0))
		.moveTo("help");

		infoPane.addButton("temperature")
		.setPosition(fieldX-240, 25)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0))
		.moveTo("help");

		infoPane.addButton("hardness")
		.setPosition(fieldX-165, 25)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0))
		.moveTo("help");

		infoPane.addButton("nitrogenCycle")
		.setLabel("nitrogen cycle")
		.setPosition(fieldX-90, 25)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0))
		.moveTo("help");

		infoPane.addButton("bacteria")
		.setPosition(fieldX-315, 50)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0))
		.moveTo("help");

		infoPane.addButton("gases")
		.setPosition(fieldX-240, 50)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0))
		.moveTo("help");

		infoPane.addButton("waterChanges")
		.setLabel("water changes")
		.setPosition(fieldX-165, 50)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0))
		.moveTo("help");

		infoPane.addButton("fishHealth")
		.setLabel("fish health")
		.setPosition(fieldX-90, 50)
		.setColorForeground(color(35, 35, 35))
		.setColorActive(color(0, 0, 0))
		.moveTo("help");

		helpText = new Textarea(infoPane, "helpText")
		.setSize(295, fieldY)
		.setPosition(fieldX-315, 75)
		.setFont(createFont("arial", 12))
		.moveTo("help");	

		/**************************************************
		 * FINAL INITIALIZATION *
		 *************************************************/
		
		frame.setTitle("AquiSim"); 
		determineBounds();
		infoPane.getTab("default").setActive(false);
		infoPane.getTab("add").setActive(false);
		infoPane.getTab("save").setActive(false);
		infoPane.getTab("help").setActive(true);
		infoPane.getTab("fishinfo").setActive(false);
		activeTab = 4; //open with the help menu
		restoreDefaults();
	}

	public void draw(){
		Vector3D bcolor = backgroundColor();
		background(bcolor.x, bcolor.y, bcolor.z);
		int color = spotlightColor();
		spotLight(color, color, color, fieldX/4, 0, 400, 0, 0, -1, PI/4, 0);
		if(!loading){
			drawTank();
			drawAllFish();
			drawSinkers();
			drawAllPlants();
			drawPreviewPlant();
			tank.allEat();
			if(updateCount > 150){ //operations to happen every 5 seconds
				tank.progress(this);
				updateCount = 0;
			}
			updateCount++;
			interfaceUpdates();
		}
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
			tank.fish.getFirst().health = 0;
			tank.fish.getFirst().handleDeceased(this);
		}
	}

	public void mouseReleased(){
		switch(clickMode){
		case FEED:
			picker.captureViewMatrix(p3d);
			picker.calculatePickPoints(mouseX,height-mouseY);
			PVector start = picker.ptStartPos;
			PVector end = picker.ptEndPos;
			if(mouseX >= backMinX && mouseX <= backMaxX && mouseY <= backMaxY){
				tank.addFood(new Food(this, new Vector3D(start.x, start.y, start.z), new Vector3D(end.x, end.y, end.z)));
			}
			else if(onLeftSide(mouseX, mouseY)){
				tank.addFood(new Food(this, new Vector3D(start.x, start.y, start.z), new Vector3D(end.x, end.y, end.z), true));
			}
			else if(onRightSide(mouseX, mouseY)){
				tank.addFood(new Food(this, new Vector3D(start.x, start.y, start.z), new Vector3D(end.x, end.y, end.z), false));
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
			picker.captureViewMatrix(p3d);
			picker.calculatePickPoints(mouseX,height-mouseY);
			start = picker.ptStartPos;
			end = picker.ptEndPos;
			selectFish(new Vector3D(start.x, start.y, start.z), new Vector3D(end.x, end.y, end.z));
			break;
		case PLACEPLANT:
			picker.captureViewMatrix(p3d);
			picker.calculatePickPoints(mouseX,height-mouseY);
			start = picker.ptStartPos;
			end = picker.ptEndPos;
			if(onBottom(mouseX, mouseY)){
				for(Plant p: getPlantSpeciesList()){
					if(p.name.equals(previewPlant.name)){
						tank.plants.add(previewPlant.setChosenPosition(this, new Vector3D(start.x, start.y, start.z), new Vector3D(end.x, end.y, end.z)));
						confirmPlant.setCaptionLabel("Add " + previewPlant.name);
						previewPlant = null;
						clickMode = ClickMode.INFO;
					}
				}
			}
			break;
		}
	}

	public static void main(String args[]){
		PApplet.main(new String[] { "--present", "graphics.Visual" });
	}


	/**************************************************
	 * DRAWING *
	 *************************************************/

	/*****MENU*****/

	public void interfaceUpdates(){
		switch(activeTab){
		case 0:
			updateTankInfo();
			break;
		case 1:
			updateFishInfo();
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		}
	}

	public void restoreDefaults(){
		helpText.setText("Welcome to AquiSim!"
				+ "\n\nThis is not your standard aquarium game. This game "
				+ "simulates the water chemistry of real fish tanks. "
				+ "Careful - your fish can lose health if the water "
				+ "conditions are unhealthy!"
				+ "\n\nEach species of fish has a certain range of "
				+ "conditions in which it thrives. Go to the add tab "
				+ "to explore the available species of fish and "
				+ "add some to your tank."
				+ "\n\nBrowse the topics above to learn how to keep your tank "
				+ "and fish healthy.");
		nicknameInput.clear();
		percentWater.setValue(50)
		.setValueLabel("Change what percent of the water?");
		fileNameInput.setText(this.tank.name);
		confirmNew.setCaptionLabel("OK");
		clickMode = ClickMode.INFO;
		previewPlant = null;
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
			fishInfo.setText("");
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

	/*****TANK*****/

	public void drawDummyBox(){
		noLights();
		noStroke();
		fill(255, 0, 0);
		pushMatrix();
		translate((int)(.4*fieldX), (int)(.5*fieldY), (int)(-fieldZ));
		box((int)(zoomPercentage*.8*fieldX), (int)(zoomPercentage*fieldY), 0); //back
		fill(255, 255, 0);
		translate((int)(zoomPercentage*.4*fieldX), 0, (int)(zoomPercentage*.25*fieldZ));
		box(0, (int)(zoomPercentage*fieldY), (int)(zoomPercentage*.5*fieldZ)); //right
		translate((int)(-zoomPercentage*.8*fieldX), 0, 0);
		box(0, (int)(zoomPercentage*fieldY), (int)(zoomPercentage*.5*fieldZ)); //left
		popMatrix();
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
		box((int)(zoomPercentage*.8*fieldX), (int)(zoomPercentage*fieldY), 0); //back
		translate((int)(zoomPercentage*.4*fieldX), 0, (int)(zoomPercentage*.25*fieldZ));
		box(0, (int)(zoomPercentage*fieldY), (int)(zoomPercentage*.5*fieldZ)); //right
		translate((int)(-zoomPercentage*.8*fieldX), 0, 0);
		box(0, (int)(zoomPercentage*fieldY), (int)(zoomPercentage*.5*fieldZ)); //left
		translate((int)(zoomPercentage*.4*fieldX), (int)(zoomPercentage*.5*fieldY), 0);
		fill(200);
		box((int)(zoomPercentage*.8*fieldX), 0, (int)(zoomPercentage*.5*fieldZ)); //bottom
		fill(0, 0, 255, 20);
		translate(0, (int)(-zoomPercentage*.5*fieldY) + (int)(zoomPercentage*fieldY*.5*(1-tank.waterLevel)), 0);
		stroke(0, 0, 100, 30);
		hint(DISABLE_DEPTH_TEST);
		box((int)(zoomPercentage*.8*fieldX), (int)(zoomPercentage*fieldY*tank.waterLevel), (int)(zoomPercentage*.5*fieldZ)); //water
		popMatrix();
	}

	void drawAllPlants(){
		hint(ENABLE_DEPTH_TEST);
		for(Plant p: this.tank.plants){
			p.drawPlant(this);
		}
	}

	void drawAllFish(){
		hint(ENABLE_DEPTH_TEST);
		for(Fish f: this.tank.fish){
			f.drawFish(this);
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
		for(DeadFish d: this.tank.deadFish){
			drawDeadFish(d);
			d.updatePosition();
		}
	}

	public void drawDeadFish(DeadFish d){
		noStroke();
		pushMatrix();
		translate(d.absolutePosition.x, d.absolutePosition.y, d.absolutePosition.z);
		rotateX(d.orientation.x);
		rotateY(d.orientation.y);
		rotateZ(d.orientation.z);
		d.model.draw();
		popMatrix();
	}

	public void drawWaste(Waste s){
		noStroke();
		pushMatrix();
		translate(s.absolutePosition.x, s.absolutePosition.y, s.absolutePosition.z);
		fill(s.color.x, s.color.y, s.color.z);
		sphere(s.dimensions.x);
		popMatrix();		
	}

	public void drawPreviewPlant(){
		if(previewPlant != null && onBottom(mouseX, mouseY)){
			picker.captureViewMatrix(p3d);
			picker.calculatePickPoints(mouseX,height-mouseY);
			PVector start = picker.ptStartPos;
			PVector end = picker.ptEndPos;
			previewPlant.setChosenPosition(this, new Vector3D(start.x, start.y, start.z), new Vector3D(end.x, end.y, end.z));
			previewPlant.drawPlant(this);
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
				clickMode = ClickMode.CLEAN;
				break;
			case 1:
				activeTab = 1;
				clickMode = ClickMode.FEED;
				break;
			case 2:
				activeTab = 2;
				clickMode = ClickMode.INFO;
				break;
			case 3:
				activeTab = 3;
				clickMode = ClickMode.INFO;
				updateSavedTanksList();
				break;
			case 4:
				activeTab = 4;
				clickMode = ClickMode.INFO;
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
				
				updateFishInfo();
			}
			// speciesinfo listbox
			if(theControlEvent.group().id() == 1){
				restoreDefaults();
				int species = (int) fishSpecies.value();
				Fish choice = getFishSpeciesList()[species];

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
				.setImage(loadImage(choice.sprite))
				.setPosition(fieldX-245, 380);

				nicknameInput.show()
				.setCaptionLabel("Choose a nickname!");	

				confirmPlant.hide();
				
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
			//plantSpecies listbox
			if(theControlEvent.group().id() == 3){
				restoreDefaults();
				Plant previewPlant = getPlantSpeciesList()[(int) plantSpecies.value()];

				speciesInfo.show()
				.setText("Species: " + previewPlant.name);

				speciesImage.show()
				.setImage(loadImage(previewPlant.sprite))
				.setPosition(fieldX-80, 440);

				nicknameInput.hide();
				confirmAdd.hide();

				confirmPlant.show()
				.setCaptionLabel("Add " + previewPlant.name);
			}
		}
	} 

	/*****TANK INFO BUTTONS*****/

	void confirmWaterChange(float theValue){
		percentWater.setValueLabel(percentWater.getValue() + "% water change performed");
		tank.waterChange(percentWater.getValue());
	}

	/*****ADD MENU BUTTONS*****/

	void confirmPlant(float theValue){
		previewPlant = getPlantSpeciesList()[(int) plantSpecies.value()];
		confirmPlant.setCaptionLabel("Click on the tank floor to place " + previewPlant.name);
		clickMode = ClickMode.PLACEPLANT;
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
			String species = getFishSpeciesList()[(int) fishSpecies.value()].name;
			addFishToTank(species, nickname);
			nicknameInput.clear();
			nicknameInput.setCaptionLabel(species + " named " +  nickname + " added!");
		}
	}

	/*****SAVE MENU BUTTONS*****/

	void confirmNew(float theValue){
		if(confirmNew.getLabel().equals("OK")){
			confirmNew.setCaptionLabel("Are you sure?");
		}
		else{
			this.tank = new Tank(TankSize.MEDIUM);
			confirmNew.setLabel("New tank created!");	
		}
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
			else if(fileName.equals(this.tank.name)){
				createSaveFile(fileNameInput.getText());
				fileNameInput.setCaptionLabel("Tank saved!");
			}
			else if(compileFileList().contains(fileName)){
				fileNameInput.setCaptionLabel("You already have a tank with this name. Click again to overwrite.");
			}
			else{
				this.tank.name = fileName;
				createSaveFile(fileNameInput.getText());
				fileNameInput.setCaptionLabel("Tank saved!");
				updateSavedTanksList();
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
			loading = true;
			draw();
			this.tank = parseFile(getFileText(filename));
			Long elapsed = System.currentTimeMillis() - getDateSaved(filename);
			int iterations = (int) (elapsed/5000.0);
			this.tank.skipAhead(this, iterations);
			loading = false;
			confirmLoad.setLabel("Tank " + filename + " loaded!");
			fileNameInput.setText(this.tank.name);
			System.out.println("loaded " + this.tank.plants.size());
		}
		catch(CorruptedSaveFileException e){
			confirmLoad.setLabel("Tank " + filename + " seems to be corrupted.");
			loading = false;
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
				+ "a range of hardnesses that it prefers. Hardness cannot "
				+ "directly be controlled in this game, but "
				+ "will fluctuate natrually.");
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
				+ "of fish to plants. The fish are not directly affected by "
				+ "these levels, but their values do impact other aspects of "
				+ "tank chemistry. Increase the O2:CO2 ratio by keeping fewer "
				+ "fish or more plants!");
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
				+ "become steadily hungrier with time. Hunger causes "
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
		for(Fish f: getFishSpeciesList()){
			if(f.name.equals(speciesName)){
				toAdd = f.createFromNickname(nickname);
				tank.addFish(toAdd);
			}
		}
		fishChoices.addItem(toAdd.nickname + ": " + toAdd.name, tank.fish.size()-1);;
	}

	public Fish[] getFishSpeciesList(){
		Fish[] speciesList = { new CherryBarb(this, "Swimmy"),
				new CherryShrimp(this, "Swimmy"),
				new DwarfPuffer(this, "Swimmy"),
				new Guppy(this, "Swimmy"),
				new IncaSnail(this, "Swimmy"),
//				new NeonTetra(this, "Swimmy"),
				new WhiteCloudMountainMinnow(this, "Swimmy")};
		return speciesList;
	}

	public Plant[] getPlantSpeciesList(){
		Plant[] speciesList = { new TallPlant(this) };
		return speciesList;
	}

	public void updateSavedTanksList(){
		savedTanks.clear();
		LinkedList<String> files = compileFileList();
		for(int i = 0; i < files.size(); i++){
			savedTanks.addItem(files.get(i), i);
		}
	}

	/*****SAVE / LOAD*****/

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
		saveText += tank.name + "\n";
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
		saveText += "Start dead fish info\n";
		for(int i = 0; i < tank.deadFish.size(); i++){
			DeadFish d = tank.deadFish.get(i);
			saveText += d.position.toCommaSeparated();
			saveText += d.orientation.toCommaSeparated();
			saveText += d.species + "\n";
		}
		saveText += "Start plant info\n";
		for(int i = 0; i < tank.plants.size(); i++){
			Plant p = tank.plants.get(i);
			saveText += p.name + "\n";
			saveText += p.absolutePosition.toCommaSeparated();
			saveText += p.orientation.toCommaSeparated();
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
		for(Fish f: getFishSpeciesList()){
			if(f.name.equals(array[start])){
				return f.createFromParameters(array[start+1],
						parseStatus(array[start+2]),
						Long.parseLong(array[start+3]),
						Long.parseLong(array[start+4]),
						Long.parseLong(array[start+5]),
						Integer.parseInt(array[start+6]));
			}
		}
		throw new CorruptedSaveFileException();
	}

	public Plant makePlant(String[] array, int start) throws CorruptedSaveFileException{
		for(Plant p: getPlantSpeciesList()){
			System.out.println(array[start]);
			System.out.println(array[start+1]);
			System.out.println(array[start+2]);
			if(p.name.equals(array[start])){
				return p.createFromParameters(this,
						new Vector3D(array[start+1]),
						new Vector3D(array[start+2]));
			}
		}
		throw new CorruptedSaveFileException();
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
			end = indexOf(lines, "Start dead fish info");
			LinkedList<Food> food = new LinkedList<Food>();
			for(int i = start; i < end; i++){
				food.add(new Food(this, new Vector3D(lines[i])));
			}
			start = indexOf(lines, "Start dead fish info")+1;
			end = indexOf(lines, "Start plant info");
			LinkedList<DeadFish> deadFish = new LinkedList<DeadFish>();
			for(int i = start; i < end; i+=3){
				deadFish.add(new DeadFish(this, new Vector3D(lines[i]), new Vector3D(lines[i+1]), lines[i+2]));
			}
			start = indexOf(lines, "Start plant info")+1;
			end = indexOf(lines, "Start fish info");
			LinkedList<Plant> plants = new LinkedList<Plant>();
			for(int i = start; i < end; i+=3){
				plants.add(makePlant(lines, i));
			}
			start = indexOf(lines, "Start fish info")+1;
			end = lines.length;
			LinkedList<Fish> fish = new LinkedList<Fish>();
			for(int i = start; i < end; i+=7){
				fish.add(makeFish(lines, i));
			}
			Tank tank = new Tank(Integer.parseInt(lines[2]),
					Integer.parseInt(lines[3]),
					Integer.parseInt(lines[4]),
					Double.parseDouble(lines[5]),
					Double.parseDouble(lines[6]),
					Double.parseDouble(lines[7]),
					Double.parseDouble(lines[8]),
					Double.parseDouble(lines[9]),
					Double.parseDouble(lines[10]),
					Double.parseDouble(lines[11]),
					Double.parseDouble(lines[12]),
					Double.parseDouble(lines[13]),
					Double.parseDouble(lines[14]),
					Integer.parseInt(lines[15]),
					lines[16],
					poops, food, deadFish, plants, fish);
			return tank;
		}
		catch(CorruptedSaveFileException e){
			throw new CorruptedSaveFileException();
		}
	}

	/*****RAY TRACING*****/

	public void determineBounds(){
		drawDummyBox();
		loadPixels();
		draw();
		int x = 0;
		int y = 0;
		for(int i: pixels){
			if(i == -65536){
				if(x < backMinX) backMinX = x;
				else if(x > backMaxX) backMaxX = x;
				if(y > backMaxY) backMaxY = y;
			}
			else if (i == -256){
				if( x < leftMinX) leftMinX = x;
				else if(x > rightMaxX) rightMaxX = x;
				if(y > sidesMaxY) sidesMaxY = y;
			}
			x++;
			if(x >= fieldX){
				x = 0;
				y++;
			}
		}
	}

	public void selectFish(Vector3D start, Vector3D end){
		Vector3D normal = end.addVector(start.multiplyScalar(-1)).normalize();
		Fish closest = null;
		float z = -1000;
		for(Fish f: tank.fish){
			if(clickedFish(f, start, normal)){
				Vector3D absolutePosition = f.position.addVector(new Vector3D((int)(.4*fieldX), (int)(.5*fieldY)+(int)(zoomPercentage*fieldY*.5*(1-tank.waterLevel)), (int)(-fieldZ)+(int)(zoomPercentage*.25*fieldZ)));
				if(absolutePosition.z > z){
					z = absolutePosition.z;
					closest = f;
				}				
			}
		}
		if(closest != null){
			infoPane.getTab("default").setActive(false);
			infoPane.getTab("add").setActive(false);
			infoPane.getTab("save").setActive(false);
			infoPane.getTab("help").setActive(false);
			infoPane.getTab("fishinfo").setActive(true);
			activeTab = 2;
			int selected = fishChoices.getItem(closest.nickname + ": " + closest.name).getValue();
			fishChoices.setValue(selected);
			controlEvent(new ControlEvent(fishChoices));
		}
	}

	public void removeWaste(Vector3D start, Vector3D end){
		Vector3D normal = end.addVector(start.multiplyScalar(-1)).normalize();
		Waste closest = null;
		float z = -1000;
		for(Poop p: tank.poops){
			if(raySphereIntersect(start, normal, p.absolutePosition, p.dimensions.x*2)){
				if(p.absolutePosition.z > z){
					z = p.absolutePosition.z;
					closest = p;
				} 
			}
		}
		for(Food f: tank.food){
			if(raySphereIntersect(start, normal, f.absolutePosition, f.dimensions.x*2)){
				if(f.absolutePosition.z > z){
					z = f.absolutePosition.z;
					closest = f;
				}
			}
		}
		for(DeadFish d: tank.deadFish){
			if(clickedDeadFish(d, start, normal)){
				if(d.absolutePosition.z > z){
					z = d.absolutePosition.z;
					closest = d;
				}				
			}
		}
		if(closest != null){
			closest.removeFromTank(this.tank);
		}
	}

	public float triangleArea(Vector3D point1, Vector3D point2, Vector3D point3){
		return (float) Math.abs(((point1.x*(point2.y-point3.y)) + point2.x*(point3.y-point1.y) + point3.x*(point1.y-point2.y))/2.0);
	}

	public boolean onLeftSide(float mouseX, float mouseY){
		Vector3D point = new Vector3D(mouseX, mouseY, 0);
		
		Vector3D leftBottomLeft = new Vector3D(leftMinX, sidesMaxY, 0);
		Vector3D leftBottomRight = new Vector3D(backMinX, backMaxY, 0);
		Vector3D leftTopRight = new Vector3D(backMinX, 0, 0);
		Vector3D leftTopLeft = new Vector3D(leftMinX, 0, 0);
		
		float area = triangleArea(leftBottomLeft, leftBottomRight, leftTopLeft) + triangleArea(leftBottomRight, leftTopRight, leftTopLeft);
		float pointArea  = triangleArea(leftBottomLeft, point, leftBottomRight) + triangleArea(leftBottomRight, point, leftTopRight) +
				triangleArea(leftTopRight, point, leftTopLeft) + triangleArea(leftTopLeft, point, leftBottomLeft);
		
		return pointArea <= area;
	}
	
	public boolean onRightSide(float mouseX, float mouseY){
		Vector3D point = new Vector3D(mouseX, mouseY, 0);
		
		Vector3D rightBottomLeft = new Vector3D(backMaxX, backMaxY, 0);
		Vector3D rightBottomRight = new Vector3D(rightMaxX, sidesMaxY, 0);
		Vector3D rightTopRight = new Vector3D(rightMaxX, 0, 0);
		Vector3D rightTopLeft = new Vector3D(backMaxX, 0, 0);
		
		float area = triangleArea(rightBottomLeft, rightBottomRight, rightTopLeft) + triangleArea(rightBottomRight, rightTopRight, rightTopLeft);
		float pointArea  = triangleArea(rightBottomLeft, point, rightBottomRight) + triangleArea(rightBottomRight, point, rightTopRight) +
				triangleArea(rightTopRight, point, rightTopLeft) + triangleArea(rightTopLeft, point, rightBottomLeft);
		
		return pointArea <= area;
	}

	public boolean onBottom(float mouseX, float mouseY){
		Vector3D point = new Vector3D(mouseX, mouseY, 0);
		
		Vector3D bottomLeft = new Vector3D(leftMinX, sidesMaxY, 0);
		Vector3D bottomRight = new Vector3D(rightMaxX, sidesMaxY, 0);
		Vector3D topRight = new Vector3D(backMaxX, backMaxY, 0);
		Vector3D topLeft = new Vector3D(backMinX, backMaxY, 0);
		
		float area = triangleArea(bottomLeft, bottomRight, topLeft) + triangleArea(bottomRight, topRight, topLeft);
		float pointArea  = triangleArea(bottomLeft, point, bottomRight) + triangleArea(bottomRight, point, topRight) +
				triangleArea(topRight, point, topLeft) + triangleArea(topLeft, point, bottomLeft);
		
		return pointArea <= area;
	}

	public boolean raySphereIntersect(Vector3D rayOrigin, Vector3D rayNormal, Vector3D sphereCenter, float sphereRadius){
		double determinant = Math.pow(rayNormal.dotProduct(rayOrigin.addVector(sphereCenter.multiplyScalar(-1))), 2) - Math.pow(rayOrigin.addVector(sphereCenter.multiplyScalar(-1)).magnitude(), 2) + Math.pow(sphereRadius, 2);
		if(determinant < 0) return false;
		else return true;
	}

	public boolean clickedFish(Fish f, Vector3D rayOrigin, Vector3D rayNormal){
		Vector3D absolutePosition = f.position.addVector(new Vector3D((int)(.4*fieldX), (int)(.5*fieldY)+(int)(zoomPercentage*fieldY*.5*(1-tank.waterLevel)), (int)(-fieldZ)+(int)(zoomPercentage*.25*fieldZ)));
		float width = (float) Math.abs((Math.cos(f.orientation.y)*f.dimensions.x) + Math.abs(Math.sin(f.orientation.y)*f.dimensions.z));
		float height = f.dimensions.y;
		float dist = (absolutePosition.z-rayOrigin.z)/rayNormal.z;
		Vector3D pointAt = rayOrigin.addVector(rayNormal.multiplyScalar(dist));
		return pointAt.x < (absolutePosition.x + width/2.0) && pointAt.x > (absolutePosition.x - width/2.0)
				&& pointAt.y < (absolutePosition.y + height/2.0) && pointAt.y > (absolutePosition.y - height/2.0);
	}

	public boolean clickedDeadFish(DeadFish d, Vector3D rayOrigin, Vector3D rayNormal){
		float width = (float) Math.abs((Math.cos(d.orientation.y)*d.dimensions.x) + Math.abs(Math.sin(d.orientation.y)*d.dimensions.z));
		float height = d.dimensions.y;
		float dist = (d.absolutePosition.z-rayOrigin.z)/rayNormal.z;
		Vector3D pointAt = rayOrigin.addVector(rayNormal.multiplyScalar(dist));
		return pointAt.x < (d.absolutePosition.x + width/2.0) && pointAt.x > (d.absolutePosition.x - width/2.0)
				&& pointAt.y < (d.absolutePosition.y + height/2.0) && pointAt.y > (d.absolutePosition.y - height/2.0);
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

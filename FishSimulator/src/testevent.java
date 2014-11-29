import controlP5.*;
import processing.core.*;

public class testevent extends PApplet{

	ControlP5 cp5;

	int myColorBackground = color(128);

	int sliderValue = 100;

	public void setup() {
	  size(700,400);
	  noStroke();
	  cp5 = new ControlP5(this);
	  
	  // By default all controllers are stored inside Tab 'default' 
	  // add a second tab with name 'extra'
	  
	  cp5.addTab("extra")
	     .setColorBackground(color(0, 160, 100))
	     .setColorLabel(color(255))
	     .setColorActive(color(255,128,0))
	     ;
	     
	  // if you want to receive a controlEvent when
	  // a  tab is clicked, use activeEvent(true)
	  
	  cp5.getTab("default")
	     .activateEvent(true)
	     .setLabel("my default tab")
	     .setId(1)
	     ;

	  cp5.getTab("extra")
	     .activateEvent(true)
	     .setId(2)
	     ;

	  
	  // create a few controllers
	  
	  cp5.addButton("button")
	     .setBroadcast(false)
	     .setPosition(100,100)
	     .setSize(80,40)
	     .setValue(1)
	     .setBroadcast(true)
	     .getCaptionLabel().align(CENTER,CENTER)
	     ;
	     
	  cp5.addButton("buttonValue")
	     .setBroadcast(false)
	     .setPosition(220,100)
	     .setSize(80,40)
	     .setValue(2)
	     .setBroadcast(true)
	     .getCaptionLabel().align(CENTER,CENTER)
	     ;
	  
	  cp5.addSlider("slider")
	     .setBroadcast(false)
	     .setRange(100,200)
	     .setValue(128)
	     .setPosition(100,160)
	     .setSize(200,20)
	     .setBroadcast(true)
	     ;
	     
	  cp5.addSlider("sliderValue")
	     .setBroadcast(false)
	     .setRange(0,255)
	     .setValue(128)
	     .setPosition(100,200)
	     .setSize(200,20)
	     .setBroadcast(true)
	     ;
	     
	  // arrange controller in separate tabs
	  
	  cp5.getController("sliderValue").moveTo("extra");
	  cp5.getController("slider").moveTo("global");
	  
	  // Tab 'global' is a tab that lies on top of any 
	  // other tab and is always visible
	  
	}

	public void draw() {
	  background(myColorBackground);
	  fill(sliderValue);
	  rect(0,0,width,100);
	}

	public void controlEvent(ControlEvent theControlEvent) {
	  if (theControlEvent.isTab()) {
	    println("got an event from tab : "+theControlEvent.getTab().getName()+" with id "+theControlEvent.getTab().getId());
	  }
	}

	void slider(int theColor) {
	  myColorBackground = color(theColor);
	  println("a slider event. setting background to "+theColor);
	}


	public void keyPressed() {
	  if(keyCode==TAB) {
	    cp5.getTab("extra").bringToFront();
	  }
	}

	
}

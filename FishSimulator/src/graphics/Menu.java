//package graphics;
//
//import controlP5.*;
//
//public class Menu {
//	String tabText;
//	int topLeftX;
//	int topLeftY;
//	Visual visual;
//	ControlP5 menu;
//	int lightness;
//	
//	final int verticalSize = 150;
//	final int horizontalSize = 100;
//	
//	public Menu(Visual visual, String tabText, int topLeftX, int topLeftY, int lightness){
//		this.visual = visual;
//		this.tabText = tabText;
//		this.topLeftX = topLeftX;
//		this.topLeftY = topLeftY;
//		this.lightness = lightness;
//		this.menu = new ControlP5(visual);
//	}
//	
//	public void drawTab(){
//		visual.fill(80 + lightness * 50, 120 + lightness * 50, 150 + lightness * 50);
//		visual.pushMatrix();
//		visual.translate(visual.fieldX/2 - topLeftX, -visual.fieldY/2 + topLeftY);
//		visual.box(50);
//		visual.rect(0, 0, horizontalSize, verticalSize, 12, 0, 0, 12);
//		visual.popMatrix();
//	}
//	
//	public boolean isClicked(int x, int y){
//		System.out.println("Click: " + x + " " + y);
//		System.out.println("left " + (visual.fieldX - topLeftX));
//		System.out.println("right " + (visual.fieldX - topLeftX + horizontalSize));
//		System.out.println("top " + (topLeftY));
//		System.out.println("bottom " + (topLeftY + verticalSize));
//		return x >= (visual.fieldX - topLeftX) &&
//				x <= (visual.fieldX - topLeftX + horizontalSize) &&
//				y >= (topLeftY) &&
//				y <= (topLeftY + verticalSize);
//	}
//
//}

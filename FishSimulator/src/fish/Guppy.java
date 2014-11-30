/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish;

import engine.Coordinate;
import engine.Diet;
import engine.Qualify;
import graphics.Visual;

import java.util.LinkedList;

import saito.objloader.OBJModel;

public class Guppy extends Fish{
    public Guppy(Visual window){
    this.name = "Guppy";
    this.nickname = "Swimmy";
    this.timeWell = System.currentTimeMillis();
    this.ease = 5;
    this.health = this.ease;
    this.maxHappyFull = this.ease*24*60*5;
    this.happiness = this.maxHappyFull;
    this.fullness = this.maxHappyFull;
    this.size = 5;
    this.minPH = 7;
    this.maxPH = 8.5;
    this.minTemp = 20;
    this.maxTemp = 29;
    this.minHard = 8;
    this.maxHard = 20;
    this.peacefulness = 5;
    this.tankSize = 37;
    this.diet = new LinkedList <Diet>();
    this.region = "middle to top";
    this.ammonia = 1000000;
    this.nitrite = 1000000;
    this.nitrate = 1000000;
    this.ratio = 2;
    this.schooling = 0;
    this.properties = new LinkedList <Qualify>();
    this.model = new OBJModel(window, "guppy.obj", Visual.POLYGON);
    this.sprite = "guppy.png";
    this.model.scale(30);
    this.model.translateToCenter();
    this.position = new Coordinate(0, 0, 0);
    
    diet.add(Diet.FLAKE);
    diet.add(Diet.FROZEN);
    diet.add(Diet.LIVE);
    properties.add(Qualify.SUSCEPTIBLE);
    properties.add(Qualify.PLANTS);
    
    }
}

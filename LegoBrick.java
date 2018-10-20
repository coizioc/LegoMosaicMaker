/**
 * LegoBrick is a class that defines the methods and attributes of the LegoBrick
 * object. This is the object that will be used to model the attributes of a 
 * Lego brick.
 */

package legomosaicmaker;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LegoBrick {
    // Static members legoColors and bricks are loaded in from csv files.
    public static ArrayList<LegoColor> legoColors = openLegoColors("colors.csv");
    public static ArrayList<LegoBrick> bricks = openBricks("bricks.csv");
    private static ArrayList<LegoColor> openLegoColors(String filename) {
        try {
            File file = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            ArrayList<LegoColor> colors = new ArrayList<>();
            
            String line;
            while((line = br.readLine()) != null) {
                String[] colorLine = line.split(",");
                colors.add(new LegoColor(colorLine[1], colorLine[2]));
            }
            
            return colors;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static ArrayList<LegoBrick> openBricks(String filename) {
        try {
            File file = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            ArrayList<LegoBrick> bricks = new ArrayList<>();
            
            String line;
            while((line = br.readLine()) != null) {
                String[] brickLine = line.split(",");
                int length = Integer.parseInt(brickLine[0]);
                int width = Integer.parseInt(brickLine[1]);
                bricks.add(new LegoBrick(length, width));
            }
            
            return bricks;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Finds the nearest color out of the list of Lego colors.
    public static LegoColor getNearestLegoColor(Color currentColor) {
        LegoColor closestColor = LegoBrick.legoColors.get(0);
        double closestDistance = Double.MAX_VALUE;
        for(LegoColor color : LegoBrick.legoColors) {
            double currentDistance = color.getDistance(currentColor);
            if(currentDistance < closestDistance) {
                closestDistance = currentDistance;
                closestColor = color;
            }
        }
        
        return closestColor;
    }
    
    int width,  height;
    int x, y;
    double cost;
    LegoColor color;
    
    
    public LegoBrick(int width, int height, int x, int y, double cost, LegoColor color) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.color = color;
    }
    
    public LegoBrick(int width, int height, int x, int y, LegoColor color) {
        this(width, height, x, y, 0, color);
    }
    
    public LegoBrick(int width, int height, int x, int y, Color color) {
        this(width, height, x, y, 0, getNearestLegoColor(color));
    }
    
    public LegoBrick(int width, int height, Color color) {
        this(width, height, 0, 0, 0, getNearestLegoColor(color));
    }
    
    public LegoBrick(int width, int height) {
        this(width, height, 0, 0, 0, new LegoColor("White", 255, 255, 255));
    }
    
    
    public boolean equals(LegoBrick otherBrick) {
        return this.hasEqualDimensions(otherBrick) &&
               this.color.equals(otherBrick.color);
    }
    
    public boolean hasEqualDimensions(LegoBrick otherBrick) {
        return (this.height == otherBrick.height &&
               this.width == otherBrick.width) ||
               (this.width == otherBrick.height &&
               this.height == otherBrick.width);
    }
    
    @Override
    public String toString() {
        return this.color.toString() + " " + 
               this.height + "x" + this.width + " brick";
    }
}

/**
 * LegoColor is a class that extends the default Color class to include more
 * information specific to colors used in Lego bricks.
 */
package legomosaicmaker;

import java.awt.Color;

public class LegoColor extends Color {
    String colorName;
    int officialID;
    int brickLinkID;
    
    public LegoColor(String colorName, int red, int green, int blue, int officialID, int brickLinkID) {
        super(red, green, blue);
        this.colorName = colorName;
        this.officialID = officialID;
        this.brickLinkID = brickLinkID;
    }
    
    public LegoColor(String colorName, int red, int green, int blue) {
        this(colorName, red, green, blue, 0, 0);
    }
    
    public LegoColor(String colorName, String rgbHex) {
        this(colorName, Integer.valueOf(rgbHex.substring(0, 2), 16),
                        Integer.valueOf(rgbHex.substring(2, 4), 16),
                        Integer.valueOf(rgbHex.substring(4, 6), 16));
    }
    
    // Calculates distance between current lego color and another arbitary
    // color.
    public double getDistance(Color otherColor) {
        // returns the Euclidian distance between the RGB values.
        return Math.sqrt(Math.pow(otherColor.getRed() - this.getRed(), 2)
                       + Math.pow(otherColor.getGreen() - this.getGreen(), 2)
                       + Math.pow(otherColor.getBlue() - this.getBlue(), 2));
    }
    
    public double getDistance(int red, int green, int blue) {
        return getDistance(new Color(red, green, blue));
    }
    
    public int getOutlineColor() {
        return (this.getDistance(Color.BLACK) < 100) ? Color.WHITE.getRGB() : 
                                                       Color.BLACK.getRGB();
    }
    
    @Override
    public String toString() {
        return this.colorName;
    }
    
    public boolean equals(LegoColor legoColor) {
        return this.getRGB() == legoColor.getRGB();
    }
}

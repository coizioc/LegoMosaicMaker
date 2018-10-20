/**
 * This class defines the methods and attributes of the LegoSet class. This is
 * the class that converts an image file into a list of bricks and their
 * locations within the image.
 */

package legomosaicmaker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

public class LegoSet {
    private int sizeMultiplier = 8;
    private boolean[][] brickPlaced;
    private ArrayList<LegoBrick> brickLocations;
    private LegoColor[][] pixelColors;
    
    public LegoSet(BufferedImage image) {
        brickPlaced = new boolean[image.getWidth()][image.getHeight()];
        brickLocations = new ArrayList<>();
        getPixels(image);
        placeBricks();
    }
    
    // Places bricks to cover each pixel of the image.
    private void placeBricks() {
        // for each brick in the list of bricks, go to each pixel and see
        // if a brick has been placed. If not, see if the brick's dimensions
        // fit (either height x width or width x height).
        LegoBrick.bricks.forEach((brick) -> {
            for(int i = 0; i < pixelColors.length; i++) {
                for(int j = 0; j < pixelColors[i].length; j++) {
                    if(!brickPlaced[i][j]) {
                        if(brickFits(i, j, brick.height, brick.width)) {
                            addBrick(brick.height, brick.width, i, j, 
                                    pixelColors[i][j]);
                        }
                        else if (brickFits(i, j, brick.width, brick.height)) {
                            addBrick(brick.width, brick.height, i, j, 
                                    pixelColors[i][j]);
                        }
                    }
                }
            }
        });
    }
    
    // Converts the colors of the image to match its nearest LegoColor.
    private void getPixels(BufferedImage image) {
        
        pixelColors = new LegoColor[image.getWidth()][image.getHeight()];
        
        for(int y = 0; y < image.getWidth(); y++) {
            for(int x = 0; x < image.getHeight(); x++) {
                pixelColors[y][x] = LegoBrick.getNearestLegoColor(
                        new Color(image.getRGB(y, x)));
            }
        }
    }

    // Checks to see if a brick fits in a given location in the image.
    private boolean brickFits(int x, int y, int height, int width) {
        // Check if the dimensions of the brick at the current location exceed
        // the size of the image.
        if(height + x > pixelColors.length ||
                width + y > pixelColors[x].length) {
            return false;
        }
        
        LegoColor currentColor = pixelColors[x][y];
        
        // Check if all pixels covered by the brick are the same color.
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if(brickPlaced[i + x][j + y]) {
                    return false;
                }
                if(currentColor != pixelColors[i + x][j + y]) {
                    return false;
                }
            }
        }       
        return true;
    }
    
    // Adds a brick to brickLocations.
    private void addBrick(int height, int width, int x, int y, LegoColor color) {
        brickLocations.add(new LegoBrick(height, width, x, y, color));
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                brickPlaced[x + i][y + j] = true;
            }
        }
    }
    
    // Prints the instructions and writes them to a txt file.
    public String printInstructions() {
        Stream<LegoBrick> stream = Stream.of(brickLocations.toArray(
                new LegoBrick[brickLocations.size()])).parallel();
        Map<String, Long> counter = stream.collect(Collectors.groupingBy(
                LegoBrick::toString, Collectors.counting()));
        int totalBricks = 0;
        String instructions = "To complete this, you will need:\n";
        Iterator it = counter.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            
            long number = (long)pair.getValue();
            totalBricks += number;
            String brickName = (number == 1) ? (String)pair.getKey()
                                             : (String)pair.getKey() + "s";
            
            instructions += String.format("%d %s\n", number, brickName);
            it.remove();
        }
        instructions += String.format("For a total of %d bricks.\n", totalBricks);
        return instructions;
    }
    
    // Draws the final lego mosaic image and saves it as a png file.
    public void draw(BufferedImage original, File file) throws IOException {
        int newWidth = sizeMultiplier * original.getWidth();
        int newHeight = sizeMultiplier * original.getHeight();
        
        BufferedImage newImg = new BufferedImage(newWidth, newHeight, 
                BufferedImage.TYPE_INT_RGB);
        
        Graphics2D g = newImg.createGraphics();
        
        g.drawImage(original, 0, 0, newWidth, newHeight, null);
        
        g.dispose();
        
        brickLocations.forEach((LegoBrick brick) -> {
            int brickxCoord = sizeMultiplier * brick.x;
            int brickyCoord = sizeMultiplier * brick.y;
            int brickHeight = sizeMultiplier * brick.height;
            int brickWidth = sizeMultiplier * brick.width;
            
            for(int i = brickxCoord; i < brickxCoord + brickWidth; i++) {
                for(int j = brickyCoord; j < brickyCoord + brickHeight; j++) {
                    if(i == brickxCoord || i == brickxCoord + brickWidth - 1 || 
                       j == brickyCoord || j == brickyCoord + brickHeight - 1 || 
                       (i % sizeMultiplier == sizeMultiplier - 1 && 
                        j % sizeMultiplier == sizeMultiplier - 1) ||
                       (i % sizeMultiplier == 0 && j % sizeMultiplier == 0)) {
                        newImg.setRGB(i, j, brick.color.getOutlineColor());
                    }
                    else {
                        newImg.setRGB(i, j, brick.color.getRGB());
                    }
                }
            }
        });
        saveImage(newImg, file);
    }
    
    public void saveInstructions(File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(printInstructions());
        }
    }
    
    public void saveImage(BufferedImage image, File file) throws IOException {
        ImageIO.write(image, "png", file);
    }
    
    public void setMultiplier(int newSizeMultiplier) {
        if(newSizeMultiplier < 1) {
            throw new UnsupportedOperationException(
                    "Size multiplier cannot be less than 1.");
        }
        else {
            sizeMultiplier = newSizeMultiplier;
        }
    }
    
    public int getMultiplier() {
        return sizeMultiplier;
    }
    
}

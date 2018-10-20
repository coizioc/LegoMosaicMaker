/**
 * The LegoMosiacMaker program creates a Lego brick mosaic out of an image file.
 * A Lego brick mosaic is an image whose pixels are made up of Lego bricks.
 * Analogously, a Lego brick mosaic consisting solely of 1x1 bricks is similar
 * in structure to a perler bead image. 
 * 
 * @author Coizioc
 * @version 1.0
 * @since 2018-10-19
 */

package legomosaicmaker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class LegoMosaicMaker {
    public static void main(String[] args) {
        BufferedImage image = null;
        File file = null;
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Type 'q' at any time to quit.");
        
        while(true) {
            System.out.print("Enter the name of the file you want to convert: ");
            String filename = sc.nextLine();
            if(filename.equals("q")) {
                break;
            }
            try {
                file = new File(filename);
                image = ImageIO.read(file);
                break;
            }
            catch(IOException e) {
                System.out.println(e.toString() + " File not found or unusable.\n");
                e.printStackTrace();
            }
        }
        
        if(image != null) {
            long timeBefore = System.nanoTime();
            System.out.print("Converting " + file.getName() + "... ");
            LegoSet mosaic = new LegoSet(image);
            long timeAfter = System.nanoTime();
            System.out.println("Conversion finished. Conversion took " + 
                    (timeAfter - timeBefore) / 1000000000 + " seconds.");
            try {
                String filename = file.getName().substring(0, 
                    file.getName().lastIndexOf('.'));
                File instructionsFile = new File(filename + "Instructions.txt");
                File mosaicFile = new File(filename + "Mosaic.png");
                System.out.print("Saving instructions... ");
                mosaic.saveInstructions(instructionsFile);
                System.out.println("Instructions saved.");
                System.out.print("Saving mosaic image... ");
                mosaic.draw(image, mosaicFile);
                System.out.println("Mosaic saved.");
            }
            catch (IOException e) {
                System.out.println(e.toString() + " Unable to save mosaic.");
                e.printStackTrace();
            }
        }
        
    }
}

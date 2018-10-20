package legomosaicmaker;

import javax.swing.JFrame;

public class ImageFrame {
	private JFrame f = new JFrame("Mosaic Preview Image");
	
	public ImageFrame() {
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(300,300);
		f.setVisible(true);
	}
}
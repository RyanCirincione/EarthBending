import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class Dust {
	float opacity;
	double x,y,height,wdith;
	BufferedImage dust;
	
	{
		try {
			dust = ImageIO.read(new File("res/puffofdustdefault.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Dust (double sx, double sy)
	{
		x = sx;
		y = sy;
		opacity = 1f;
	}
	public Dust (double sx, double sy,float op)
	{
		x = sx;
		y = sy;
		opacity = op;
		//dust.
	}
	
}

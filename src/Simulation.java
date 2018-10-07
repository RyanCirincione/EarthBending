import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Simulation {
	int pixels[][] = new int[640][480];
	ArrayList<Rock> rocks = new ArrayList<Rock>();
	BufferedImage rockImage;

	public Simulation() {
		try {
			rockImage = ImageIO.read(new File("res/rock1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void simulate(Graphics gr, boolean up, boolean down, boolean left, boolean right, boolean pleft, boolean pright) {
		//System.out.println(rocks.size());
		for (int i = 0; i < rocks.size();i++) {

			if (rocks.get(i).type == Rock.Type.BOULDER) {
				rocks.get(i).gravity();
			}
			if (rocks.get(i).type == Rock.Type.PILLAR) {
				rocks.get(i).decayTimer();
			}
			if (rocks.get(i).x > 800 || rocks.get(i).x < -200 || rocks.get(i).y > 480) {
				rocks.remove(rocks.get(i));
				i--;
				continue;
			}
			rocks.get(i).fly();
			
			System.out.println(rocks.get(i).x + " " + rocks.get(i).y + " " + rocks.get(i).height + " " + rocks.get(i).isActive);
			gr.drawImage(rockImage, (int) (rocks.get(i).x),  (int)(rocks.get(i).y),  (int)(rocks.get(i).width), (int)(rocks.get(i).height), null);
		}

	}

	// types of rocks 0 = small boulder 1 = large boulder
	public void createBoulder(boolean side, double height,double width,double x)// 0 is left 1 is right
	{
		for (Rock temp : rocks) {
			if (temp.side == side) {
				if (temp.isActive) {
					temp.isActive = false;
				}
			}
		}
		Rock newRock = new Rock(x, 440, side, Rock.Type.BOULDER, height,width);
		rocks.add(newRock);
		//System.out.println(rocks.size());
	}

	public void createPillar(boolean side, double height,double width,double x) {
		Rock newRock = new Rock(x, 480, side, Rock.Type.PILLAR, height,width);
		rocks.add(newRock);
	}

	public void punch(boolean side)// 0 left 1 right
	{
		for (Rock temp : rocks) {
			if (temp.side == side) {
				if (temp.side) {
					temp.velocityX = 3000 / 60.0;// temp number for test reasons
				} else {
					temp.velocityX = -3000 / 60.0;// temp number for test reasons
				}
			}
		}
	}

	public void scatterShot(boolean side)// SIMPLE GEOMETRY
	{
		int counter = rocks.size();
		for(int i = 0; i<counter;i++ )
		{
			if(rocks.get(i).side == side&&rocks.get(i).type!= Rock.Type.FRAGMENT)
			{
				for(int j = 0;j < rocks.get(i).height*rocks.get(i).width/400;j++)
					
				{
					Rock newRock = new Rock(rocks.get(i).x,rocks.get(i).y,rocks.get(i).side,Rock.Type.FRAGMENT,rocks.get(i).height/1.5,rocks.get(i).width/1.5);
					newRock.velocityX = side? 50/60.0:-50/60.0;
					newRock.velocityX = newRock.velocityX + ((Math.random()*20)+40)/60;
					newRock.velocityY = ((Math.random()*100)-50);

					rocks.add(newRock);
				}
				rocks.remove(rocks.get(i));
				i--;
				counter--;
			}
		}

	}
}

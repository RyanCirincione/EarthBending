import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Simulation {
	int pixels[][] = new int[640][480];
	ArrayList<Rock> rocks = new ArrayList<Rock>();

	public void simulate(Graphics gr, boolean up, boolean down, boolean left, boolean right, boolean pleft, boolean pright) {
		System.out.println(rocks.size());
		for (Rock temp : rocks) {

			if (temp.type == Rock.Type.BOULDER) {
				temp.gravity();
			}
			if (temp.type == Rock.Type.PILLAR) {
				temp.decayTimer();
			}
			if (temp.x > 800 || temp.x < -200 || temp.y > 480) {
				rocks.remove(temp);
			}
			temp.fly();
			switch (temp.type) {
			case BOULDER: {
				gr.setColor(Color.getHSBColor(222, 184, 135));
				break;
			}
			case PILLAR: {
				gr.setColor(Color.BLACK);
				break;
			}
			case FRAGMENT: {
				gr.setColor(Color.GRAY);
				break;
			}
			default:
				break;
			}
			System.out.println(temp.x + " " + temp.y + " " + temp.size + " " + temp.isActive);
			gr.fillRect((int) temp.x, (int) temp.y, (int) temp.size, (int) temp.size);
		}

	}

	// types of rocks 0 = small boulder 1 = large boulder
	public void createBoulder(boolean side, int size)// 0 is left 1 is right
	{
		for (Rock temp : rocks) {
			if (temp.side == side) {
				if (temp.isActive) {
					temp.isActive = false;
				}
			}
		}
		int x = side ? 420 : 240;
		Rock newRock = new Rock(x, 440, side, Rock.Type.BOULDER, size);
		rocks.add(newRock);
		System.out.println(rocks.size());
	}

	public void createPillar(boolean side, double size) {
		double x = side ? 420 : 240;
		Rock newRock = new Rock(x, 480, side, Rock.Type.PILLAR, size);
		rocks.add(newRock);
	}

	
	public void punch (boolean side)//0 left 1 right
	{
		for (Rock temp : rocks) {
			if(temp.side == side)
			{
				if(temp.side)
				{
					temp.velocityX = 2000/60.0;//temp number for test reasons
				}
				else
				{
					temp.velocityX = -2000/60.0;//temp number for test reasons
				}
			}
		}
	}
	public void scatterShot(boolean side)//SIMPLE GEOMETRY
	{
		for(Rock temp:rocks)
		{
			if(temp.side == side)
			{
				for(int i = 0;i < temp.size/100;i++)
				{
					Rock newRock = new Rock(temp.x,temp.y,temp.side,temp.type,temp.size);
					newRock.velocityX = side? -50/60.0:50/60.0;
					newRock.velocityY = (Math.random()*2-1);
					rocks.add(newRock);
				}
			}
		}
		
	}
}

import java.awt.Graphics;
import java.util.ArrayList;

public class Simulation
{
	int pixels[][] = new int[640][480];
	ArrayList<Rock> rocks = new ArrayList<Rock>();
	public void simulate(Graphics gr, boolean  up, boolean down, boolean left, boolean right, boolean pleft, boolean pright)
	{
		for(Rock temp : rocks)
		{
		
				if(temp.type == 0 || temp.type == 1 ||temp.type == 2){
					temp.gravity();
				}
				if(temp.x > 1000 || temp.x < -200 ||temp.y < -200)
				{
					rocks.remove(temp);
				}
				if()
		}
	}
	//types of rocks 0 = small boulder 1 = large boulder 
	public void createRock(boolean side)//0 is left 1 is right
	{
		for(Rock temp : rocks)
		{
			if(temp.side == side)
			{
				if(temp.isActive)
				{
					temp.isActive = false;
				}
			}
		}
		int x = side? 420: 240;
		Rock newRock = new Rock(x,0,side,0);
		rocks.add(newRock);
	}
}

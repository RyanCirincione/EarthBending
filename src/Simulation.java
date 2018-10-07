import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Simulation {
	int pixels[][] = new int[640][480];
	ArrayList<Rock> rocks = new ArrayList<Rock>();

	public void simulate(Graphics gr, boolean up, boolean down, boolean left, boolean right, boolean pleft,
			boolean pright) {
		for (Rock temp : rocks) {

			if (temp.type == Rock.Type.BOULDER) {
				temp.gravity();
			}
			if (temp.type == Rock.Type.PILLAR) {
				temp.decayTimer();
			}
			if (temp.x > 1000 || temp.x < -200 || temp.y > 480) {
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
			System.out.println("TEST!");
			gr.drawRect((int) temp.x, (int) temp.y, (int) temp.size, (int) temp.size);

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
		Rock newRock = new Rock(x, 480, side, Rock.Type.BOULDER, size);
		rocks.add(newRock);
		System.out.println(rocks.size());
	}

	public void createPillar(boolean side, int size) {
		int x = side ? 420 : 240;
		Rock newRock = new Rock(x, 480, side, Rock.Type.PILLAR, size);
		rocks.add(newRock);
	}

	public void createFragment(boolean side, int size, int x, int y) {
		Rock newRock = new Rock(x, 480, side, Rock.Type.FRAGMENT, size);
	}
}

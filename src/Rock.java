
public class Rock {

	int x;
	int y;
	boolean side;
	int velocity;
	boolean isActive;
	public Rock (int sx, int sy, boolean sside)
	{
		x = sx;
		y = sy;
		side = sside;//0 left 1 right
		isActive = true; 
	}
}

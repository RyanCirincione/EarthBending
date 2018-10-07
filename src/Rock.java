
public class Rock {

	double x;
	double y;
	double size;
	boolean side;
	double velocityY;
	double velocityX;
	boolean isActive;
	Type type;
	int lifeTimer;
	public static enum Type {
		BOULDER,PILLAR,FRAGMENT;
		
	}
	final double GRAVITY = 10/60;
	public Rock (int sx, int sy, boolean sside, Type temp, double ssize)
	{
		x = sx;
		y = sy;
		side = sside;//0 left 1 right
		isActive = true; 
		velocityY = -50/60;
		type = temp;
		lifeTimer = 600;
		size = ssize;
	}
	public void gravity ()
	{
		if(this.isActive)
			this.y =  (this.velocityY * 1/((480 - this.y)/100) + GRAVITY);//VALUSE WILL NEED TO CHANGE!
		else
			this.y =  (this.y + GRAVITY);
	}
	public void fly ()
	{
		this.x = this.x + velocityX;
	}
	public void decayTimer()
	{
		if(lifeTimer>0)
		{
			lifeTimer--;
		}
		else
		{
			y = y +25/60;
		}
	}
}

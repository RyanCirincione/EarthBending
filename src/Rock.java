
public class Rock {

	double x;
	double y;
	boolean side;
	double velocity;
	boolean isActive;
	int type;
	final double GRAVITY = 10/60;
	public Rock (int sx, int sy, boolean sside, int stype)
	{
		x = sx;
		y = sy;
		side = sside;//0 left 1 right
		isActive = true; 
		velocity = -50/60;
		type =stype;
	}
	public void gravity ()
	{
		if(this.isActive)
			this.y =  (this.velocity * 1/((480 - this.y)/100) + GRAVITY);//VALUSE WILL NEED TO CHANGE!
		else
			this.y =  (this.y + GRAVITY);
	}
}

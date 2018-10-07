
public class Rock {

	double x;
	double y;
	boolean side;
	double velocityY;
	double velocityX;
	boolean isActive;
	double height;
	double width;
	Type type;
	int lifeTimer;

	public static enum Type {
		BOULDER, PILLAR, FRAGMENT;

	}

	final double GRAVITY = (2.0 * ((this.y) + 240)) / 10.0;

	public Rock(double sx, double sy, boolean sside, Type temp, double sheight,double swidth) {
		x = sx;
		y = sy;
		side = sside;// 0 left 1 right
		isActive = true;
		velocityY = -100.0 / 60.0;
		type = temp;
		lifeTimer = 600;
		height = sheight;
		width = swidth;
	}

	public void gravity() {
		if (this.isActive) {
			this.velocityY -= (1.3 * ((this.y) - 240)) / 10.0;// VALUSE WILL NEED TO CHANGE!
			if(this.y > 255) {
				this.velocityY = this.velocityY/1.3;
			}
			if(this.y < 210) {
				this.velocityY += 1.8;
//				this.velocityY = this.velocityY/1.05;
			}
//			if((int)this.y == 220) {
//				System.out.println("HEY CHRIS LOOK HERE YOU DUM DUM" + this.velocityY + "THIS IS THE VELOCITY!!!!");
//			}
		} else {
			this.y = (this.y + GRAVITY);
		}
		this.y += velocityY;
	}
	public void grow()
	{
		if(y > (480-height))
		{
			y += velocityY;
		}
		y = velocityY;
	}
	public void fly() {
		System.out.println("TEST ME BOPI"+velocityX);
		this.x = this.x + velocityX;
		if(type == Rock.Type.FRAGMENT)
		{
			y = y + velocityY;
		}
	}

	public void decayTimer() {
		if (lifeTimer > 0) {
			lifeTimer--;
		} else {
			y = y + 25 / 60.0;
		}
	}
}

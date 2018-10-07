
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
		BOULDER, PILLAR, FRAGMENT;

	}

	final double GRAVITY = 10.0 / 60.0;

	public Rock(int sx, int sy, boolean sside, Type temp, double ssize) {
		x = sx;
		y = sy;
		side = sside;// 0 left 1 right
		isActive = true;
		velocityY = -50 / 60.0;
		type = temp;
		lifeTimer = 600;
		size = ssize;
	}

	public void gravity() {
		if (this.isActive) {
			this.velocityY += GRAVITY - (this.y - 240) / 5.0;// VALUSE WILL NEED TO CHANGE!
		} else {
			this.y = (this.y + GRAVITY);
		}
		if(velocityY > 0) {
			velocityY -= 1;
		} else {
			velocityY += 1;
			
		}
		this.y += velocityY;
	}

	public void fly() {
		System.out.println(velocityX);
		this.x = this.x + velocityX;
	}

	public void decayTimer() {
		if (lifeTimer > 0) {
			lifeTimer--;
		} else {
			y = y + 25 / 60.0;
		}
	}
}

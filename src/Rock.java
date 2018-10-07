
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

	final double GRAVITY = ((2.5 * this.y) + 240) / 10.0;

	public Rock(double sx, double sy, boolean sside, Type temp, double ssize) {
		x = sx;
		y = sy;
		side = sside;// 0 left 1 right
		isActive = true;
		velocityY = -40 / 60.0;
		type = temp;
		lifeTimer = 600;
		size = ssize;
	}

	public void gravity() {
		if (this.isActive) {
			this.velocityY += GRAVITY - ((2.5 * this.y) - 240) / 10.0;// VALUSE WILL NEED TO CHANGE!
			if(this.y > 225) {
				this.velocityY = this.velocityY/1.3;
			}
			if(this.y < 170) {
				this.velocityY = this.velocityY/1.45;
			}
		} else {
			this.y = (this.y + GRAVITY);
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

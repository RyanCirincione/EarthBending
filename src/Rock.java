
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

	final double GRAVITY = (2.0 * ((this.y) + 240)) / 10.0;

	public Rock(double sx, double sy, boolean sside, Type temp, double ssize) {
		x = sx;
		y = sy;
		side = sside;// 0 left 1 right
		isActive = true;
		velocityY = -100.0 / 60.0;
		type = temp;
		lifeTimer = 600;
		size = ssize;
	}

	public void gravity() {
		if (this.isActive) {
			this.velocityY -= (1.8 * ((this.y) - 240)) / 10.0;// VALUSE WILL NEED TO CHANGE!
			if((int)this.y< 222 && (int)this.velocityY<0) {
				if((int)this.velocityY < -1) {
					this.velocityY = -2;
				}
				this.velocityY += 0.1;
				this.velocityY = this.velocityY/1.03;
			}
			if(248 < (int)this.y && (int)this.velocityY > 0) {
				if((int)this.velocityY > 1) {
					this.velocityY = 2;
				}
				this.velocityY -= 0.1;
				this.velocityY = this.velocityY/1.03;
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

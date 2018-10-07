import java.awt.image.BufferedImage;

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
	BufferedImage image;

	public static enum Type {
		BOULDER, PILLAR, FRAGMENT;
	}

	final double GRAVITY = (2.0 * ((this.y) + 240)) / 10.0;


	public Rock(double sx, double sy, boolean sside, Type temp, double sheight,double swidth,BufferedImage image) {

		x = sx;
		y = sy;
		side = sside;// 0 left 1 right
		isActive = true;
		velocityY = -100.0 / 60.0;
		type = temp;
		lifeTimer = 600;
		height = sheight;
		width = swidth;
		this.image = image;
	}
	public Rock(double sx, double sy, boolean sside, Type temp, double sheight,double swidth,double svelocityY) {
		x = sx;
		y = sy;
		side = sside;// 0 left 1 right
		isActive = true;
		velocityY = -100.0 / 60.0 + svelocityY;
		type = temp;
		lifeTimer = 600;
		height = sheight;
		width = swidth;
	}
	public void gravity() {
		if (this.isActive) {
			this.velocityY -= (1.8 * ((this.y) - 240)) / 10.0;// VALUSE WILL NEED TO CHANGE!
			if ((int) this.y < 222 && (int) this.velocityY < 0) {
				if ((int) this.velocityY < -1) {
					this.velocityY = -2;
				}
				this.velocityY += 0.1;
				this.velocityY = this.velocityY / 1.05;
			}
			if (248 < (int) this.y && (int) this.velocityY > 0) {
				if ((int) this.velocityY > 1) {
					this.velocityY = 2;
				}
				this.velocityY -= 0.1;
				this.velocityY = this.velocityY / 1.05;
			}
		} else {
			this.y = (this.y + GRAVITY);
		}
		this.y += velocityY;
	}

	public void grow() {
		if (y > (480 - height)) {
			y += -600 / 60;
		}
	}

	public void fly() {
		this.x = this.x + velocityX;
		if (type == Rock.Type.FRAGMENT) {
			y = y + velocityY;
		}
	}

	public void decayTimer() {
		if (lifeTimer > 0) {
			lifeTimer--;
			this.grow();
		} else {
			y = y + 25 / 60.0;
		}
	}
}

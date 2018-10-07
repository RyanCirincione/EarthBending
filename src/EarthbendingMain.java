
import static org.bytedeco.javacpp.opencv_core.cvFlip;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.VideoInputFrameGrabber;

public class EarthbendingMain extends JPanel {
	public static void main(String[] args) {
		JFrame frame = new JFrame(":B:earthbending");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new EarthbendingMain();
		frame.getContentPane().add(panel);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				panel.repaint();
			}
		}, 0, 1000 / 60);
	}

	private static final long serialVersionUID = 4486604239167882738L;
	static final int STANDING_SPACE = 250, S_WIDTH = 640, S_HEIGHT = 480;
	boolean w, a, s, d, left, right, pillarUpgrade;
	Simulation sim;
	BufferedImage background;
	FrameGrabber grabber;
	OpenCVFrameConverter.ToIplImage converter;
	IplImage img;
	int bgTimer, threshold, punchThreshold, screenShake;

	public EarthbendingMain() {
		sim = new Simulation();
		screenShake = 0;
		pillarUpgrade = false;
		threshold = 100;
		punchThreshold = 8000;
		bgTimer = 0;
		background = null;
		grabber = new VideoInputFrameGrabber(0);
		converter = new OpenCVFrameConverter.ToIplImage();
		try {
			grabber.start();
		} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
			e.printStackTrace();
		}

		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_W:
					screenShake += 14;
					pillarUpgrade = true;
					w = true;
					for (int i = 80; i < S_WIDTH-140; i += 20) {
						sim.dusts.add(new Dust(i + Math.random() * 20, S_HEIGHT - 80 + Math.random() * 20, 0.35f));
					}
					break;
				case KeyEvent.VK_A:
					if (pillarUpgrade) {
						if (s) {
							sim.createTriplePillar(false, 200, 80, S_WIDTH / 2 - STANDING_SPACE / 2 - 85);
						} else {
							sim.createPillar(false, 200, 80, S_WIDTH / 2 - STANDING_SPACE / 2 - 85);
						}
						pillarUpgrade = false;
					} else {
						if (s) {
							sim.createTripleBoulder(false, 70, 70, S_WIDTH / 2 - STANDING_SPACE / 2 - 85);
						} else {
							sim.createBoulder(false, d ? 90 : 70, d ? 90 : 70, S_WIDTH / 2 - STANDING_SPACE / 2 - 85);
						}
					}
					screenShake += 4;
					a = true;
					break;
				case KeyEvent.VK_S:
					screenShake += 6;
					s = true;
					for (int i = 80; i < S_WIDTH-140; i += 40) {
						sim.dusts.add(new Dust(i + Math.random() * 20, S_HEIGHT - 80 + Math.random() * 20, 0.35f));
					}
					break;
				case KeyEvent.VK_D:
					if (pillarUpgrade) {
						if (s) {
							sim.createTriplePillar(true, 200, 80, S_WIDTH / 2 + STANDING_SPACE / 2 + 25);
						} else {
							sim.createPillar(true, 200, 80, S_WIDTH / 2 + STANDING_SPACE / 2 + 25);
						}
						pillarUpgrade = false;
					} else {
						if (s) {
							sim.createTripleBoulder(true, 70, 70, S_WIDTH / 2 + STANDING_SPACE / 2 + 25);
						} else {
							sim.createBoulder(true, a ? 90 : 70, a ? 90 : 70, S_WIDTH / 2 + STANDING_SPACE / 2 + 25);
						}
					}
					screenShake += 4;
					d = true;
					break;
				case KeyEvent.VK_LEFT:
					threshold--;
					break;
				case KeyEvent.VK_RIGHT:
					threshold++;
					break;
				case KeyEvent.VK_UP:
					punchThreshold += 100;
					break;
				case KeyEvent.VK_DOWN:
					punchThreshold -= 100;
					break;
				}
			}

			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_W:
					w = false;
					break;
				case KeyEvent.VK_A:
					a = false;
					break;
				case KeyEvent.VK_S:
					s = false;
					break;
				case KeyEvent.VK_D:
					d = false;
					break;
				}
			}
		});
		this.setFocusable(true);
		this.requestFocus();

		this.setPreferredSize(new Dimension(S_WIDTH, S_HEIGHT));
	}

	public void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		int ssX = (int) (Math.random() * screenShake * 2) - screenShake, ssY = (int) (Math.random() * screenShake * 2) - screenShake;
		if (screenShake > 0) {
			if (screenShake > 30) {
				screenShake = 30;
			}
			screenShake -= 2;
		}
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, S_WIDTH, S_HEIGHT);
		g.translate(ssX, ssY);
		BufferedImage image = null;
		try {
			Frame frame = grabber.grab();
			img = converter.convert(frame);
			// the grabbed frame will be flipped, re-flip to make it right
			cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise

			image = IplImageToBufferedImage(img);
			if (bgTimer < 30) {
				bgTimer++;
			} else if (bgTimer == 30) {
				bgTimer++;
				background = deepCopy(image);
			}
		} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
			e.printStackTrace();
		}

		g.drawImage(image, 0, 0, null);

		int leftCount = 0, rightCount = 0;
		boolean[][] pixels = new boolean[640][480], pixels2 = new boolean[640][480];
		if (background != null) {
			// Flag every pixel that is different enough from the background
			for (int x = 0; x < image.getWidth(); x++) {
				for (int y = 0; y < image.getHeight(); y++) {
					int bgRGB = background.getRGB(x, y), iRGB = image.getRGB(x, y);
					if (Math.abs(((bgRGB >> 16) & 0xFF) - ((iRGB >> 16) & 0xFF)) + Math.abs(((bgRGB >> 8) & 0xFF) - ((iRGB >> 8) & 0xFF))
							+ Math.abs((bgRGB & 0xFF) - (iRGB & 0xFF)) > threshold) {
						pixels2[x][y] = true;
						if (x < S_WIDTH / 2 - STANDING_SPACE / 2) {
							leftCount++;
						}
						if (x > S_WIDTH / 2 + STANDING_SPACE / 2) {
							rightCount++;
						}
					}
				}
			}
			if (leftCount > punchThreshold) {
				if (!left) {
					if (w && s) {
						sim.scatterShot(false);
					}
					sim.punch(false);
				}
				left = true;
			} else {
				left = false;
			}
			if (rightCount > punchThreshold) {
				if (!right) {
					if (w && s) {
						sim.scatterShot(true);
					}
					sim.punch(true);
				}
				right = true;
			} else {
				right = false;
			}

			// Make pixels a copy of pixels2 that has all the falses spread by 1 pixel
			for (int x = 0; x < pixels2.length; x++) {
				for (int y = 0; y < pixels2[x].length; y++) {
					pixels[x][y] = pixels2[x][y];
					for (int dx = -1; dx <= 1 && pixels2[x][y]; dx++) {
						for (int dy = -1; dy <= 1 && pixels2[x][y]; dy++) {
							if (x + dx < 0 || y + dy < 0 || x + dx >= pixels2.length || y + dy >= pixels2[x + dx].length || !pixels2[x + dx][y + dy]) {
								pixels[x][y] = false;
							}
						}
					}
				}
			}
		}
		final int CARTOON_EFFECT_SCALE = 32;
		for (int x = 0; x < pixels.length; x++) {
			for (int y = 0; y < pixels[x].length; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF, gre = (rgb >> 8) & 0xFF, b = rgb & 0xFF;
				g.setColor(new Color((r / CARTOON_EFFECT_SCALE) * CARTOON_EFFECT_SCALE + 24, (gre / CARTOON_EFFECT_SCALE) * CARTOON_EFFECT_SCALE + 24,
						(b / CARTOON_EFFECT_SCALE) * CARTOON_EFFECT_SCALE + 24));
				g.fillRect(x, y, 1, 1);

				if (pixels[x][y]) {
					g.setColor(Color.white);
					g.fillRect(x, y, 1, 1);
				}
			}
		}

		// g.drawImage(image, 641, 0, null);
		// g.setColor(Color.white);
		// for (int x = 0; x < pixels.length; x++) {
		// for (int y = 0; y < pixels[x].length; y++) {
		// if (pixels2[x][y]) {
		// g.fillRect(641 + x, y, 1, 1);
		// }
		// }
		// }

		sim.simulate(g, w, s, a, d, left, right);

		g.setColor(Color.cyan);
		g.drawLine(S_WIDTH / 2 - STANDING_SPACE / 2, 0, S_WIDTH / 2 - STANDING_SPACE / 2, S_HEIGHT);
		g.drawLine(S_WIDTH / 2 + STANDING_SPACE / 2, 0, S_WIDTH / 2 + STANDING_SPACE / 2, S_HEIGHT);
		g.translate(-ssX, -ssY);

		g.setColor(Color.white);
		g.fillRect(0, 485, 140, 20);
		g.setColor(Color.blue);
		g.drawString("" + threshold, 2, 500);
		g.drawString("" + punchThreshold, 82, 500);
	}

	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	/**
	 * Copy/pasted, converts IplImage to BufferedImage
	 * 
	 * @param src
	 *            IplImage to convert
	 * @return Converted BufferedImage
	 */
	public static BufferedImage IplImageToBufferedImage(IplImage src) {
		OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
		Java2DFrameConverter paintConverter = new Java2DFrameConverter();
		Frame frame = grabberConverter.convert(src);
		return paintConverter.getBufferedImage(frame, 1);
	}
}
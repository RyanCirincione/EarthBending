
import static org.bytedeco.javacpp.opencv_core.cvFlip;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
	static final int STANDING_SPACE = 250, S_WIDTH = 640, S_HEIGHT = 510;
	boolean w, a, s, d, left, right;
	Simulation sim;
	BufferedImage background;
	FrameGrabber grabber;
	OpenCVFrameConverter.ToIplImage converter;
	IplImage img;
	int bgTimer, threshold, punchThreshold, screenShake;

	public EarthbendingMain() {
		sim = new Simulation();
		screenShake = 0;
		threshold = 100;
		punchThreshold = 6000;
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
					w = true;
					break;
				case KeyEvent.VK_A:
					sim.createBoulder(false, 50);
					screenShake += 4;
					a = true;
					break;
				case KeyEvent.VK_S:
					screenShake += 6;
					s = true;
					break;
				case KeyEvent.VK_D:
					sim.createBoulder(true, 50);
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
		int ssX = (int) (Math.random() * screenShake * 2) - screenShake, ssY = (int) (Math.random() * screenShake * 2) - screenShake;
		if (screenShake > 0) {
			screenShake -= 2;
		}
		gr.setColor(Color.lightGray);
		gr.fillRect(0, 0, S_WIDTH, S_HEIGHT);
		gr.translate(ssX, ssY);
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

		gr.drawImage(image, 0, 0, null);

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
				System.out.println("left");
			}
			if (rightCount > punchThreshold) {
				System.out.println("\t\t\tright");
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

			sim.simulate(gr, w, s, a, d, left, right);
		}
		final int CARTOON_EFFECT_SCALE = 64;
		for (int x = 0; x < pixels.length; x++) {
			for (int y = 0; y < pixels[x].length; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF, g = (rgb >> 8) & 0xFF, b = rgb & 0xFF;
				gr.setColor(new Color((r / CARTOON_EFFECT_SCALE) * CARTOON_EFFECT_SCALE + 24, (g / CARTOON_EFFECT_SCALE) * CARTOON_EFFECT_SCALE + 24,
						(b / CARTOON_EFFECT_SCALE) * CARTOON_EFFECT_SCALE + 24));
				gr.fillRect(x, y, 1, 1);

				if (pixels[x][y]) {
					gr.setColor(Color.white);
					gr.fillRect(x, y, 1, 1);
				}
			}
		}

		// gr.drawImage(image, 641, 0, null);
		// gr.setColor(Color.white);
		// for (int x = 0; x < pixels.length; x++) {
		// for (int y = 0; y < pixels[x].length; y++) {
		// if (pixels2[x][y]) {
		// gr.fillRect(641 + x, y, 1, 1);
		// }
		// }
		// }
		
		gr.setColor(Color.cyan);
		gr.drawLine(S_WIDTH / 2 - STANDING_SPACE / 2, 0, S_WIDTH / 2 - STANDING_SPACE / 2, S_HEIGHT);
		gr.drawLine(S_WIDTH / 2 + STANDING_SPACE / 2, 0, S_WIDTH / 2 + STANDING_SPACE / 2, S_HEIGHT);
		gr.translate(-ssX, -ssY);

		gr.setColor(Color.white);
		gr.fillRect(0, 485, 140, 20);
		gr.setColor(Color.blue);
		gr.drawString("" + threshold, 2, 500);
		gr.drawString("" + punchThreshold, 82, 500);
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
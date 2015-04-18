import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class SpriteSheet extends JFrame implements Runnable {

	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension d = tk.getScreenSize();
	int width = d.width;
	int height = d.height;
	static BufferedImage[] sprites;
	static int count = 0;
	static BufferedImage drawMe;
	static BufferedImage BKG = null;
	Kernel kernel = new Kernel(3, 3, new float[] { 1f / 9f, 1f / 9f, 1f / 9f,
			1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f });
	BufferedImageOp op = new ConvolveOp(kernel);
	BufferedImage cursorImg = new BufferedImage(16, 16,
			BufferedImage.TYPE_INT_ARGB);
	Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			cursorImg, new Point(0, 0), "blank cursor");

	@Override
	public void run() {
		Thread robot = new Thread(new Runnable() {
			public void run() {
				Robot robot = null;
				try {
					robot = new Robot();
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
				while (true) {
					robot.mouseMove(width / 2, height / 2);
				}
			}
		});
		robot.start();
		while (true) {
			try {
				Thread.sleep(80);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			drawMe = sprites[count];
			BufferedImage scales = new BufferedImage(width + 2, height + 2, BufferedImage.TRANSLUCENT);
			Graphics2D g2 = scales.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(drawMe, 0, 0, width + 2, height + 2, null);
			g2.dispose();
			drawMe = scales;
			count++;
			System.out.println(count);
			if (count == 21 * 20 - 11)
				count = 0;
			repaint();
		}
	}

	public void paint(Graphics g) {
		Image dbi = createImage(getWidth(), getHeight());
		Graphics dbg = dbi.getGraphics();
		paintComponent(dbg);
		g.drawImage(dbi, 0, 0, this);
	}

	private void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(drawMe, op, -1, -1);
	}

	public static void main(String[] args) {

		SpriteSheet s = new SpriteSheet();
		s.setVisible(true);

		try {
		} catch (Exception e) {
			e.printStackTrace();
		}

		final int width = 45;
		final int height = 36;
		final int rows = 21;
		final int cols = 21;
		sprites = new BufferedImage[rows * cols];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				sprites[(i * cols) + j] = BKG.getSubimage(j * width,
						i * height, width, height);
			}
		}

		drawMe = sprites[count];

		new Thread(s).start();
	}

	public SpriteSheet() {
		try {
			BKG = ImageIO.read(this.getClass().getResource("BKG.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		/**
		 * comment out this line and uncomment the keylistener section below
		 */
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setTitle("radiohead.com");
		setAlwaysOnTop(true);
		setUndecorated(true);
		getContentPane().setCursor(blankCursor);
		
		this.addKeyListener(new KeyAdapter() {
//			String letters = "";
//			public void keyPressed(KeyEvent e) {
//				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) { 
//					if(letters.length() > 0)
//						letters = letters.substring(0, letters.length() - 1);
//				}
//				else if (e.getKeyCode() == KeyEvent.VK_ALT) {}
//				else if (e.getKeyCode() == KeyEvent.VK_F4) {}
//				else 
//					letters += KeyEvent.getKeyText(e.getKeyCode());
//				
//				
//				if (letters.equalsIgnoreCase("jackdahmsisthebombs")) {
//					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//					say("good to go");
//				}
//				else {
//					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//					say(letters);
//				}
//			}
		});
		
		this.addWindowFocusListener(new WindowAdapter() {
			public void windowLostFocus(WindowEvent e) {
				setExtendedState(JFrame.MAXIMIZED_BOTH);
			    toFront();
			}
		});
	}
	
	public static void say(String m) {
		System.out.println(m);
	}

}

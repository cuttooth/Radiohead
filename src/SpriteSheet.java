import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
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
    Dimension d = this.tk.getScreenSize();
    int width = this.d.width;
    int height = this.d.height;
    static BufferedImage[] sprites;
    static int count = 0;
    static BufferedImage drawMe;
    static BufferedImage BKG = null;
    Kernel kernel = new Kernel(3, 3, new float[] { 1f / 9f, 1f / 9f, 1f / 9f,
            1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f });
    BufferedImageOp op = new ConvolveOp(this.kernel);
    BufferedImage cursorImg = new BufferedImage(16, 16,
            BufferedImage.TYPE_INT_ARGB);

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            drawMe = sprites[count];
            BufferedImage scales = new BufferedImage(this.width + 2,
                    this.height + 2, BufferedImage.TRANSLUCENT);
            Graphics2D g2 = scales.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(drawMe, 0, 0, this.width + 2, this.height + 2, null);
            g2.dispose();
            drawMe = scales;
            count++;
            if (count == 21 * 20 - 11) {
                count = 0;
            }
            this.repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        Image dbi = this.createImage(this.getWidth(), this.getHeight());
        Graphics dbg = dbi.getGraphics();
        this.paintComponent(dbg);
        g.drawImage(dbi, 0, 0, this);
    }

    private void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(drawMe, this.op, -1, -1);
    }

    public static void main(String[] args) {

        SpriteSheet s = new SpriteSheet();
        s.setVisible(true);

        final int width = 45;
        final int height = 36;
        final int rows = 21;
        final int cols = 21;
        sprites = new BufferedImage[rows * cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sprites[(i * cols) + j] = BKG.getSubimage(j * width, i * height,
                        width, height);
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

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("radiohead.com");
        this.setUndecorated(true);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            try {
                gd.setFullScreenWindow(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg = new BufferedImage(16, 16,
                BufferedImage.TYPE_INT_ARGB);

        // Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit()
                .createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

        // Set the blank cursor to the JFrame.
        this.getContentPane().setCursor(blankCursor);
    }

}

package experimental;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import jfreerails.client.top.GameLoop;
import jfreerails.controller.ScreenHandler;

/**
 * This class tests that the game loop and screen handler are working correctly.
 * All it does is display the current time in ms and display the number of
 * frames per second.
 * 
 * @author Luke Lindsay
 * 
 */
public class AnimationExpt extends JComponent {

    private static final long serialVersionUID = 3690191057862473264L;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        g.drawString(str, 100, 100);
    }

    public static void main(String[] args) {
        System.setProperty("SHOWFPS", "true");

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.getContentPane().add(new AnimationExpt());

        ScreenHandler screenHandler = new ScreenHandler(f,
                ScreenHandler.WINDOWED_MODE);
        screenHandler.apply();

        GameLoop gameLoop = new GameLoop(screenHandler);
        Thread t = new Thread(gameLoop);
        t.start();
    }
}
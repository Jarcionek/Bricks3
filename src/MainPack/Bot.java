package MainPack;

import java.awt.Color;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 * @author Jaroslaw Pawlak
 */
public class Bot extends Thread {
    //middle of left top brick is (200;50), bricks are 50px
    //right bottom is 700;550

    /**
     * Colours as those defined in the game
     */
    private static final Color[] COLORS = {
        new Color(255, 230, 0), //yellow
        new Color(230, 0, 0), //red
        new Color(51, 51, 255), //blue
        new Color(51, 204, 51), //green
        new Color(214, 0, 147), //purple
        new Color(0, 220, 220), //cyan
        new Color(150, 150, 150), //gray
        new Color(255, 120, 255), //pink
        new Color(0, 128, 0), //dark green
        new Color(255, 153, 0), //orange
    };

    private static Robot r;
    /**
     * 50 or 55 - sizes of a brick in pixels
     */
    private static int s = 50;
    /**
     * Margin for highlighting. 17 may be highlighted, 16 is one pixel from
     * highlighting of a pressed brick.
     */
    private static int m = 16;



    private Bot() {}

    private static Thread bot;

    private static boolean stopped = true;

    public static void botStart() {
        if (bot == null || bot.isInterrupted() || !bot.isAlive()) {
            bot = new Bot();
            setStopped(false);
            bot.start();
        }
    }

    public static void botStop() {
        setStopped(true);
    }

    private static synchronized boolean isStopped() {
        return stopped;
    }

    private static synchronized void setStopped(boolean stopped) {
        Bot.stopped = stopped;
    }

    @Override
    public void run() {
        try {
            r = new Robot();
            while (!isStopped()) {
                while (!isGameActive() && !isStopped()) {}
                if (isStopped()) {
                    break;
                }
                // rows
                for (int x = 200-m; x <= 700-s-m; x += s) {
                    for (int y = 50-m; y <= 550-m; y += s) {
                        r.mouseMove(x, y);
                        r.mousePress(InputEvent.BUTTON1_MASK);
                        r.mouseMove(x+s, y);
                        r.mousePress(InputEvent.BUTTON1_MASK);
                    }
                }

                while (!isGameActive() && !isStopped()) {}
                if (isStopped()) {
                    break;
                }
                // columns
                for (int y = 50-m; y <= 550-s-m; y += s) {
                    for (int x = 200-m; x <= 700-m; x += s) {
                        r.mouseMove(x, y);
                        r.mousePress(InputEvent.BUTTON1_MASK);
                        r.mouseMove(x, y+s);
                        r.mousePress(InputEvent.BUTTON1_MASK);
                    }
                }
            }
        } catch (Exception ex) {}
    }



    private static boolean isGameActive() {
        if (t()) {
            return true;
        }

        s = s == 50? 55 : 50;
        return t();
    }

    private static boolean t() {
        for (int x = 200-m; x <= 700-m; x += s) {
            for (int y = 50-m; y <= 550-m; y += s) {
                Color p = r.getPixelColor(x, y);
                boolean crcl = false; //correct color
                for (Color c : COLORS) {
                    if (p.equals(c)) {
                        crcl = true;
                        break;
                    }
                }
                if (!crcl) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        botStart();
    }
}

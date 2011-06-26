package MainPack;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Jaroslaw Pawlak
 */
public class Main {
    public static final String VERSION = "1.21";
    public static final String RELEASED = "25/10/2010";

    public static final File DIRECTORY = new File(System.getProperty("user.dir") + "/Bricks3/");

    private static boolean cheatsActive = false;

    private static GraphicsDevice graphicsDevice = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static DisplayMode displayMode = new DisplayMode(800, 600, 32,
            DisplayMode.REFRESH_RATE_UNKNOWN);

    private static JFrame window;

    public static void main(String[] args) {
        window = new JFrame("Bricks 3");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new MenuView());
        window.setUndecorated(true);
        window.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                (new ImageIcon(Main.class.getResource("/Cursor.png")))
                .getImage(), new Point(0,0), null));

        boolean runInWindow = false;
//        runInWindow = true; //TODO for debugging purposes only; put it into settings
        if(!runInWindow && graphicsDevice.isFullScreenSupported()) {
            graphicsDevice.setFullScreenWindow(window);
            if(graphicsDevice.isDisplayChangeSupported()) {
                try {
                    graphicsDevice.setDisplayMode(displayMode);
                } catch(IllegalArgumentException ex) { //no such display mode available
                    runInWindow = true;
                }
            } else { //no display change supported
                runInWindow = true;
            }
        } else { //no full screen supported
            runInWindow = true;
        }

        if(runInWindow) {
            graphicsDevice.setFullScreenWindow(null);

            int srnW = graphicsDevice.getDisplayMode().getWidth();
            int srnH = graphicsDevice.getDisplayMode().getHeight();
            int appW = displayMode.getWidth();
            int appH = displayMode.getHeight();
            window.setLocation((srnW-appW)/2, (srnH-appH)/2);
            
            ((JPanel) window.getContentPane()).setBorder(BorderFactory
                    .createBevelBorder(BevelBorder.RAISED));

            window.pack();
            window.setVisible(true);
        }
        window.addKeyListener(new KeyAdapter() {
            private String password = "debugmode";
            private String customGameStarter = "custom";
            private int i = 0;
            @Override
            public void keyTyped(KeyEvent e) {
                char x = e.getKeyChar();
                if(!Main.isCheatsActive()) {
                    if(x == password.charAt(i)) {
                        i++;
                        if(i == password.length()) {
                            i = 0;
                            Toolkit.getDefaultToolkit().beep();
                            Main.setCheatsActive(true);
                        }
                    } else if(x == password.charAt(0)) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                } else {
                    if(x == customGameStarter.charAt(i)) {
                        i++;
                        if(i == customGameStarter.length()) {
                            i = 0;
                            MenuView.clearMessages();
                            Timer.cancelLastWorker(true);
                            SwapWorker.cancelLastWorker(true);
                            RemoveWorker.cancelLastWorker(true);
                            MenuView.displayCustomGameStarter();
                        }
                    } else if(x == customGameStarter.charAt(0)) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                }
            }
        });
        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int i = e.getKeyCode();
                if(Game.isActive() && !Game.isOver()) {
                    if(i == KeyEvent.VK_ESCAPE) {
                        Sounds.playButtonPressed();
                        Game.exit();
                    } else if(i == KeyEvent.VK_SPACE
                            || i == KeyEvent.VK_PAUSE
                            || i == KeyEvent.VK_P) {
                        Sounds.playButtonPressed();
                        Game.pressPause();
                    }
                } else {
                    MenuView.keyPressed(i);
                }
            }
        });
    }
    public static synchronized void setCheatsActive(boolean active) {
        cheatsActive = active;
    }
    public static synchronized boolean isCheatsActive() {
        return cheatsActive;
    }
    public static void requestWindowFocus() {
        window.requestFocusInWindow();
    }
}

/* //TODO LIST
 * - settings - run in window mode, sounds on/off, animations speed and quality?
 *      !!!!! allow to play in normal decorated unresizable frame!!!!!
 *
 *      at BorderLayout.NORTH add Panel with different views (like tabbed pane):
 *      - graphics/video (checkbox for play in window)
 *      - audio (turn sounds off - boolean in Sounds class?, allow to change volume)
 *      - about (that's what is currently as help)
 *
 *      saving settings
 *
 * - highest score game over panel:
 *      disable cut, copy, paste, if length == 20 then e.consume()
 *      if vk_enter pressed treat as menu button mouse pressed event
 * - improve debugging tool:
 *      in custom game started add possibility to load normal modes
 *      add mnemonics on colors and special changers
 * - algorithm for creating special bricks depending on player's moves
 *      when it randomizes which of bricks will be special, if drawn is already
        special, randomize again
 * - some checker in Timer class - if thinking too long, show move???
 * - Sounds:
 *      sounds while swapping bricks (falling and vanishing as well?)
 *      music???
 *      volume control (find sth better than applet's tools)
 * - some tool for background loading - at the program startup look for jpg
 *      images, save their names in array, if bigger than cut middle 550x550?
 *      all in Board class
 * - help (as prepared game with messages?)
 * - different display modes
 */
/* 1.1 PATCH NOTES
 * - it is no longer possible to use "pause" and "menu" buttons after game is over
 * - algorithm checking possible moves now does not ignore bottom and right board's edges
 * - "No more moves" sign is shown before recreating entire board
 * - special bricks added
 * - gray brick is darker now to make it more different from pink one
 * - number of colors in game is now increasing correctly every 5 levels
 */
/* 1.2 PATCH NOTES
 * - save and load added - game is saved automatically after each move
 * - keyboard shortcuts in both menu and game added
 * - debugging tools added: custom mode, special bricks chooser, color changer, time controler
 * - new background is loaded after bricks fall, also level and difficulty are increased with some lag
 * - maximum time decreases with new levels
 * - chance that new brick will be special has been decreased
 * - application uses now a bit less memory
 */
/* 1.21 PATCH NOTES
 * - game was not saved when there was no directory Bricks3 in Bricks3.jar's
 * directory - this has been fixed
 */



/*
 * - save game after each move, before mouse is unblocked (run in it background thread? get values and start saving?)
 *      when game over then delete existing save
 *      what to save, what is it, where to get, how to start, additional info:
 * int[][]  board of bricks     Board       Board.setSpecifiedBoard(int[][])    calculate size for Game constructor
 * int      colors              Game        Game constructor
 * float    colors+ factor      Game        Game constructor
 * int      time for brick      Game        Game constructor
 * float    time- factor        Game        Game constructor
 * int      max time            Game        Game constructor
 * float    max- factor         Game        Game constructor
 * int      level               Scores      Game constructor
 * int      score               Scores      Game constructor
 * int      current time        Timer       Game constructor
 * bool[][] hiders              Board       Board.setHiders(boolean[][])        calculate numberOfHiders in Board class
 * int      background          Board       Board.setLastBackgroundAsCurrent()
 * int[3]   previous backs      Board       Board.setLastBackgrounds(int, int, int)
 * int      mode                MenuView    MenuView.startCustomGame(GamesParameters, int, int)
 * int      difficulty          MenuView    MenuView.startCustomGame(GamesParameters, int, int)
 * 1. set specified board
 * 2. start custom game
 * 3. set last backgrounds
 * 4. set background and hiders
 */
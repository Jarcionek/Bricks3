package MainPack;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Scores {
    public static final File FILE = new File(Main.DIRECTORY + "/scores.ser");

    public static final String DIFFICULTY_CALM_EASY = "calm easy";
    public static final String DIFFICULTY_CALM_MEDIUM = "calm medium";
    public static final String DIFFICULTY_CALM_HARD = "calm hard";
    public static final String DIFFICULTY_RUSH_EASY = "rush easy";
    public static final String DIFFICULTY_RUSH_MEDIUM = "rush medium";
    public static final String DIFFICULTY_RUSH_HARD = "rush hard";

    private static Game game;
    private static String difficulty;
    private static int level;
    private static int score;

    private static HashMap<String, String[][]> scores = init();

    private static JLabel[][] cells;

    /**
     * Sets game and resets score.
     */
    public static synchronized void setGame(Game game, int level, int score) {
        Scores.game = game;
        Scores.level = level;
        Scores.score = score;
    }
    public static synchronized void setDifficulty(String difficulty) {
        Scores.difficulty = difficulty;
    }
    /**
     * Adds <code>points</code> to current score and updates Game's scoreLabel
     */
    public static synchronized void addScore(int points) {
        score += points;
        game.setScore(score);
    }
    public static synchronized void levelUp() {
        level++;
    }
    public static synchronized int getScore() {
        return score;
    }
    public static synchronized int getLevel() {
        return level;
    }
    public static String getDifficulty() {
        return difficulty;
    }

    private static HashMap<String, String[][]> resetScores() {
        HashMap<String, String[][]> x = new HashMap<String, String[][]>();
        x.put(DIFFICULTY_CALM_EASY, createArray(0.35f, 500));
        x.put(DIFFICULTY_CALM_MEDIUM, createArray(0.35f, 400));
        x.put(DIFFICULTY_CALM_HARD, createArray(0.35f, 300));
        x.put(DIFFICULTY_RUSH_EASY, createArray(0.15f, 250));
        x.put(DIFFICULTY_RUSH_MEDIUM, createArray(0.15f, 150));
        x.put(DIFFICULTY_RUSH_HARD, createArray(0.15f, 100));
        return x;
    }
    private static String[][] createArray(float level, int score) {
        String[][] temp = new String[4][10];
        for(int j = 0; j < 10; j++) {
            temp[0][j] = (j+1) + ".";
            temp[1][j] = "Anonymous";
            temp[2][j] = "" + (int) (level*(9-j));
            temp[3][j] = "" + (10-j)*score;
        }
        return temp;
    }

    private static HashMap<String, String[][]> init() {
        HashMap<String, String[][]> x = null;
        try {
            FileInputStream fis = new FileInputStream(FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            x = (HashMap<String, String[][]>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception ex) {
            if(x == null) {
                x = resetScores();
            }
        }
        return x;
    }
    private static void save() {
        if(!Main.DIRECTORY.exists()) {
            Main.DIRECTORY.mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(scores);
            oos.close();
            fos.close();
        } catch (Exception ex) {}
    }

    /**
     * Loads scores from the file and checks if the current score is the highest
     * in its difficulty.
     */
    public static boolean isHighestScore() {
        boolean official = true;
//        official = false; //TODO comment this line for official versions
        if(official && Main.isCheatsActive()) {
            return false;
        }
        String[][] temp = scores.get(difficulty);
        if(temp == null) {
            return false;
        }
        for(int i = 0; i < 10; i++) {
            if(Integer.parseInt(temp[2][i]) < level) {
                return true;
            }
        }
        if(Integer.parseInt(temp[2][9]) == level
                && Integer.parseInt(temp[3][9]) < score) {
            return true;
        }
        return false;
    }

    /**
     * Puts current score into array and saves. Does nothing if current score
     * is not highest for current difficulty.
     */
    public static void saveScore(String name) {
        if(name.length() > 20) {
            name = name.substring(0, 20);
        }

        int k = -1;
        String[][] temp = scores.get(difficulty);
        if(temp == null) {
            return;
        }

        for(int i = 0; i < 10; i++) {
            if(Integer.parseInt(temp[2][i]) < level) {
                k = i;
                break;
            } else if(Integer.parseInt(temp[2][i]) == level
                    && Integer.parseInt(temp[3][i]) < score) {
                k = i;
                break;
            }
        }
        if (k == -1) {
            return;
        }

        for(int i = 9; i > k; i--) {
            temp[1][i] = temp[1][i-1];
            temp[2][i] = temp[2][i-1];
            temp[3][i] = temp[3][i-1];
        }

        temp[1][k] = name;
        temp[2][k] = String.valueOf(level);
        temp[3][k] = String.valueOf(score);

        scores.put(difficulty, temp);
        save();
    }

    public static JPanel getCellsForTable() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        cells = new JLabel[11][4];

        cells[0][0] = new JLabel("Place");
        cells[0][1] = new JLabel("Name");
        cells[0][2] = new JLabel("Level");
        cells[0][3] = new JLabel("Score");

        String[][] temp = scores.get(DIFFICULTY_CALM_EASY);
        for(int i = 1; i < cells.length; i++) {
            for(int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new JLabel(temp[j][i-1]);
            }
        }

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.insets = new Insets(5, 10, 5, 10);
        for(int i = 0; i < cells.length; i++) {
            c.gridy = i;
            for(int j = 0; j < cells[i].length; j++) {
                cells[i][j].setForeground(Game.COLOR_TEXT);
                cells[i][j].setFont(new Font("BankGothic Md BT", Font.BOLD, 20));
                panel.add(cells[i][j], c);
            }
        }

        return panel;
    }

    public static void applyDifficultyToCells(String difficulty) {
        String[][] temp = scores.get(difficulty);
        for(int i = 1; i < cells.length; i++) {
            for(int j = 0; j < cells[i].length; j++) {
                cells[i][j].setText(temp[j][i-1]);
            }
        }
    }
}
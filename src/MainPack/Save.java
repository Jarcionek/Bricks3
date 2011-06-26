package MainPack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Jaroslaw Pawlak
 */
public class Save implements Serializable {
    private static final long serialVersionUID = 732050807568877293L;

    private static final File FILE =
            new File(Main.DIRECTORY + "/save.ser");

    private static Save save;

    private GamesParameters gamesParameters;
    private int[][] board;
    private int[][] specials;
    private boolean[][] hiders;
    private int backgroundC;
    private int backgroundB;
    private int backgroundA;
    private int mode;
    private int difficulty;
    
    public static boolean save() {
        save = new Save();
        save.gamesParameters = Game.getGamesParameters();
        save.gamesParameters.size = 0;
        save.board = Board.getBoard();
        save.specials = Board.getSpecials();
        save.hiders = Board.getHiders();
        save.backgroundA = Board.getLastBackgrounds()[0];
        save.backgroundB = Board.getLastBackgrounds()[1];
        save.backgroundC = Board.getLastBackgrounds()[2];
        if(save.gamesParameters.difficulty.equals(Scores.DIFFICULTY_CALM_EASY)) {
            save.mode = MenuView.MODE_CALM;
            save.difficulty = MenuView.DIFFICULTY_EASY;
        } else if(save.gamesParameters.difficulty.equals(Scores.DIFFICULTY_CALM_MEDIUM)) {
            save.mode = MenuView.MODE_CALM;
            save.difficulty = MenuView.DIFFICULTY_MEDIUM;
        } else if(save.gamesParameters.difficulty.equals(Scores.DIFFICULTY_CALM_HARD)) {
            save.mode = MenuView.MODE_CALM;
            save.difficulty = MenuView.DIFFICULTY_HARD;
        } else if(save.gamesParameters.difficulty.equals(Scores.DIFFICULTY_RUSH_EASY)) {
            save.mode = MenuView.MODE_RUSH;
            save.difficulty = MenuView.DIFFICULTY_EASY;
        } else if(save.gamesParameters.difficulty.equals(Scores.DIFFICULTY_RUSH_MEDIUM)) {
            save.mode = MenuView.MODE_RUSH;
            save.difficulty = MenuView.DIFFICULTY_MEDIUM;
        } else if(save.gamesParameters.difficulty.equals(Scores.DIFFICULTY_RUSH_HARD)) {
            save.mode = MenuView.MODE_RUSH;
            save.difficulty = MenuView.DIFFICULTY_HARD;
        }
        
        try {
            if(!Main.DIRECTORY.exists()) {
                Main.DIRECTORY.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(save);
            oos.close();
            fos.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    public static boolean load() {
        try {
            FileInputStream fis = new FileInputStream(FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            save = (Save) ois.readObject();
            ois.close();
            fis.close();
        } catch(Exception ex) {
            return false;
        }
        return true;
    }
    public static void use() {
        Board.setSpecifiedBoard(save.board);
        MenuView.startCustomGame(save.gamesParameters, save.mode, save.difficulty);
        Board.setSpecials(save.specials);
        Board.setLastBackgrounds(save.backgroundA, save.backgroundB, save.backgroundC);
        Board.setLastBackgroundAsCurrent();
        Board.setHiders(save.hiders);
    }
    public static void delete() {
        save = null;
        FILE.delete();
    }
}

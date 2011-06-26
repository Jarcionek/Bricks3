package MainPack;

import java.io.Serializable;

/**
 *
 * @author Jaroslaw Pawlak
 */
public class GamesParameters implements Serializable {
    private static final long serialVersionUID = 414213562373095048L;

    public static final GamesParameters CALM_EASY = initCE();
    public static final GamesParameters CALM_MEDIUM = initCM();
    public static final GamesParameters CALM_HARD = initCH();
    public static final GamesParameters RUSH_EASY = initRE();
    public static final GamesParameters RUSH_MEDIUM = initRM();
    public static final GamesParameters RUSH_HARD = initRH();

    public String difficulty = null;
    public int size = 10;
    public float colors = 7;
    public float colorsF = 0.2f;
    public int timeForBrick = 1000;
    public float timeForBrickF = 0.9f;
    public int maxTime = 60000;
    public int currentTime = maxTime;
    public float maxTimeF = 0.9f;
    public int score = 0;
    public int level = 0;

    private static GamesParameters initCE() {
        GamesParameters r = new GamesParameters();
        r.difficulty = Scores.DIFFICULTY_CALM_EASY;
        r.size = 11; r.colors = 6; r.timeForBrick = 1000;
        r.currentTime = r.maxTime = 300000;
        return r;
    }
    private static GamesParameters initCM() {
        GamesParameters r = new GamesParameters();
        r.difficulty = Scores.DIFFICULTY_CALM_MEDIUM;
        r.size = 11; r.colors = 8; r.timeForBrick = 1000;
        r.currentTime = r.maxTime = 300000;
        return r;
    }
    private static GamesParameters initCH() {
        GamesParameters r = new GamesParameters();
        r.difficulty = Scores.DIFFICULTY_CALM_HARD;
        r.size = 11; r.colors = 10; r.timeForBrick = 1000;
        r.currentTime = r.maxTime = 300000;
        return r;
    }
    private static GamesParameters initRE() {
        GamesParameters r = new GamesParameters();
        r.difficulty = Scores.DIFFICULTY_RUSH_EASY;
        r.size = 10; r.colors = 5; r.timeForBrick = 800;
        r.currentTime = r.maxTime = 30000; r.maxTimeF = 1.0f;
        return r;
    }
    private static GamesParameters initRM() {
        GamesParameters r = new GamesParameters();
        r.difficulty = Scores.DIFFICULTY_RUSH_MEDIUM;
        r.size = 10; r.colors = 7; r.timeForBrick = 600;
        r.currentTime = r.maxTime = 20000; r.maxTimeF = 1.0f;
        return r;
    }
    private static GamesParameters initRH() {
        GamesParameters r = new GamesParameters();
        r.difficulty = Scores.DIFFICULTY_RUSH_HARD;
        r.size = 10; r.colors = 9; r.timeForBrick = 400;
        r.currentTime = r.maxTime = 10000; r.maxTimeF = 1.0f;
        return r;
    }
}
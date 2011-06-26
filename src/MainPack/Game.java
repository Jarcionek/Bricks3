package MainPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class Game extends JPanel {
    private static Game lastGame;
    private static GridBagConstraints c;
    public static final Color COLOR_TEXT = new Color(255, 200, 0); //gold

    private Timer timer;
    private Board board;

    private boolean gameOver;

    private JLabel scoreLabel;
    private JLabel levelLabel;

    /**
     * The number of different bricks' colors in game
     */
    private float colors;
    private float colorsIncreaseFactor;
    /**
     * Time in miliseconds added for each brick vanished
     */
    private int timeForBrick;
    private float timeForBrickDecreaseFactor;
    /**
     * Starting and maximum time accumulated by timer
     */
    private int maxTime;
    private float maxTimeDecreaseFactor;

    private JLabel pauseLabel;
    private static final ImageIcon pauseN =
            new ImageIcon(Game.class.getResource("/Pause.png"));
    private static final ImageIcon pauseHY =
            new ImageIcon(Game.class.getResource("/PauseHY.png"));
    private static final ImageIcon pauseHR =
            new ImageIcon(Game.class.getResource("/PauseHR.png"));

    /**
     * @param size number of bricks in row/column (if 0, specified board will be used)
     * @param colors number of colors at start
     * @param colorsF number of colors increase factor
     * @param timeForBrick time gained for each brick vanished
     * @param timeForBrickF time gained decrease factor
     * @param maxTime maximum time
     * @param currentTime starting time
     * @param maxTimeF maximum time decrease factor
     * @param score starting score
     * @param level starting level
     * @param useDebuggingBoard use specified board
     */
    public Game(int size, float colors, float colorsF,
            int timeForBrick, float timeForBrickF,
            int maxTime, int currentTime, float maxTimeF,
            int score, int level) {
        super();
        lastGame = this;
        Scores.setGame(this, level, score);

        this.colors = colors;
        this.colorsIncreaseFactor = colorsF+0.01f;
        this.timeForBrick = timeForBrick;
        this.timeForBrickDecreaseFactor = timeForBrickF;
        this.maxTime = maxTime;
        this.maxTimeDecreaseFactor = maxTimeF;
        this.scoreLabel = new JLabel("" + score);
        this.levelLabel = new JLabel("" + level);

        this.timer = new Timer(currentTime, maxTime);
        this.board = new Board(size, timer);

        this.setOpaque(false);
        this.setLayout(new GridBagLayout());

        JLabel boardBackground = new JLabel();
        boardBackground.setIcon(new ImageIcon(this.getClass().getResource("/BoardShadow.png")));
        boardBackground.setLayout(new GridBagLayout());
        boardBackground.add(board);

        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(150, 600));
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JPanel leftTopPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            leftTopPanel.setOpaque(false);

                JPanel levelPanel = new JPanel(new GridLayout(2, 1, 10, 10));
                levelPanel.setBackground(Board.BORDERS_COLOR);
                levelPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createBevelBorder(BevelBorder.RAISED),
                        BorderFactory.createBevelBorder(BevelBorder.LOWERED)));

                    JLabel levelTitleLabel = new JLabel(
                            new ImageIcon(this.getClass().getResource("/Level.png")));
                    levelTitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
                    levelPanel.add(levelTitleLabel);
                    levelLabel.setFont(new Font("BankGothic Md BT", Font.BOLD, 30));
                    levelLabel.setForeground(COLOR_TEXT);
                    levelLabel.setHorizontalAlignment(JLabel.CENTER);
                    levelPanel.add(levelLabel);

            leftTopPanel.add(levelPanel);

                JPanel scorePanel = new JPanel(new GridLayout(2, 1, 10, 10));
                scorePanel.setBackground(Board.BORDERS_COLOR);
                scorePanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createBevelBorder(BevelBorder.RAISED),
                        BorderFactory.createBevelBorder(BevelBorder.LOWERED)));

                    JLabel scoreTitleLabel = new JLabel(
                            new ImageIcon(this.getClass().getResource("/Score.png")));
                    scoreTitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
                    scorePanel.add(scoreTitleLabel);
                    scoreLabel.setFont(new Font("BankGothic Md BT", Font.BOLD, 30));
                    scoreLabel.setForeground(COLOR_TEXT);
                    scoreLabel.setHorizontalAlignment(JLabel.CENTER);
                    scorePanel.add(scoreLabel);

            leftTopPanel.add(scorePanel);

        leftPanel.add(leftTopPanel, BorderLayout.NORTH);

            JPanel pausePanel = new JPanel(new GridBagLayout());
            pausePanel.setOpaque(false);

                pauseLabel = new JLabel(
                        new ImageIcon(this.getClass().getResource("/Pause.png")));
                pauseLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(!Game.isOver()) {
                            Sounds.playButtonPressed();
                            Game.pressPause();
                        }
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if(!Game.isOver()) {
                            Sounds.playButtonPressed();
                            pauseLabel.setIcon(pauseHY);
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        if(!Game.isOver()) {
                            if(Timer.isPaused()) {
                                pauseLabel.setIcon(pauseHR);
                            } else {
                                pauseLabel.setIcon(pauseN);
                            }
                        }
                    }
                });
                pausePanel.add(pauseLabel);

            leftPanel.add(pausePanel, BorderLayout.CENTER);

            JPanel menuPanel = new JPanel(new GridLayout(2, 1, 10, 10));
            menuPanel.setOpaque(false);
            
                Button menuLabel = new Button("/Menu.png", "/MenuH.png") {
                    @Override
                    public synchronized void doClick() {
                        super.doClick();
                        exit();
                    }
                };
                menuLabel.addMouseListener(new ButtonMouseListener(menuLabel) {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if(!Game.isOver()) {
                            super.mouseEntered(e);
                        }
                    }
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(!Game.isOver()) {
                            super.mousePressed(e);
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        if(!Game.isOver()) {
                            super.mouseExited(e);
                        }
                    }
                });
                menuPanel.add(menuLabel);
                
            leftPanel.add(menuPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setPreferredSize(new Dimension(50, 600));
        rightPanel.setOpaque(false);
        rightPanel.add(timer);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;

        this.add(leftPanel, c);
        c.gridx = 1;
        this.add(boardBackground, c);
        c.gridx = 2;
        this.add(rightPanel, c);

        timer.start();
    }

    public Game(GamesParameters p) {
        this(p.size, p.colors, p.colorsF, p.timeForBrick, p.timeForBrickF,
                p.maxTime, p.currentTime, p.maxTimeF,
                p.score, p.level);
    }

    public static synchronized boolean isOver() {
        return lastGame.gameOver;
    }
    public static synchronized void setGameOver() {
        Save.delete();
        lastGame.gameOver = true;
        MenuView.displayGameOverMessage(Scores.isHighestScore());
    }
    /**
     * Displays score in <code>scoreLabel</code>. Larger scores (longer than
     * 5 digits) shows in format dddEd, e.g. instead of 1234567 shows 123E4.
     */
    public synchronized void setScore(int score) {
        if(score/100000 >= 1) {
            String result = "";

            int x = 0;
            for(int i = 1; score >= Math.pow(10, i-1); i++) {
                x = i;
            }

            result += score/((int) Math.pow(10, x-3));
            result += "E";
            result += x-3;

            scoreLabel.setText(result);
        } else {
            scoreLabel.setText("" + score);
        }
    }

    /**
     * Sets new level on the level label.
     */
    public static synchronized void levelUp() {
        int l = Integer.parseInt(lastGame.levelLabel.getText());
        lastGame.levelLabel.setText(String.valueOf(l+1));
    }

    /**
     * Returns the number of colors which currently can be drawn in game.
     * Checks <code>Brick.COLORS</code> length and does not return integer
     * greater than its length.
     */
    public static synchronized int getBricksColors() {
        int r = (int) lastGame.colors;
        int b = Brick.COLORS.length;
        if(r > b) {
            return b;
        } else {
            return r;
        }
    }
    public static synchronized void increaseBricksColors() {
        lastGame.colors += lastGame.colorsIncreaseFactor;
    }
    public static synchronized int getTimeForBrick() {
        return lastGame.timeForBrick;
    }
    public static synchronized void decreaseTimeForBrick() {
        lastGame.timeForBrick *= lastGame.timeForBrickDecreaseFactor;
    }
    public static synchronized int getMaxTime() {
        return lastGame.maxTime;
    }
    public static synchronized void decreaseMaxTime() {
        lastGame.maxTime *= lastGame.maxTimeDecreaseFactor;
        lastGame.timer.setMaximum(lastGame.maxTime);
    }

    public static boolean isActive() {
        return lastGame != null && lastGame.isShowing();
    }
    public static void exit() {
        boolean save = !lastGame.board.isMouseBlocked();
        MenuView.clearMessages();
        Timer.cancelLastWorker(true);
        SwapWorker.cancelLastWorker(true);
        RemoveWorker.cancelLastWorker(true);
        if(save) {
            Save.save();
        }
        MenuView.restoreMenu();
    }
    public static void pressPause() {
        Timer.setPaused(!Timer.isPaused());

        lastGame.board.setBricksHidden(Timer.isPaused());

        Rectangle r = new Rectangle(
                lastGame.pauseLabel.getLocationOnScreen(),
                lastGame.pauseLabel.getPreferredSize());
        if(!r.contains(MouseInfo.getPointerInfo().getLocation())) {
            if(Timer.isPaused()) {
                lastGame.pauseLabel.setIcon(pauseHR);
            } else {
                lastGame.pauseLabel.setIcon(pauseN);
            }
        }
    }
    public static GamesParameters getGamesParameters() {
        GamesParameters r = new GamesParameters();
        r.colors = lastGame.colors;
        r.colorsF = lastGame.colorsIncreaseFactor;
        r.currentTime = Timer.getCurrentTime();
        r.difficulty = Scores.getDifficulty();
        r.level = Scores.getLevel();
        r.maxTime = lastGame.maxTime;
        r.maxTimeF = lastGame.maxTimeDecreaseFactor;
        r.score = Scores.getScore();
        r.size = Board.getBoardsSize();
        r.timeForBrick = lastGame.timeForBrick;
        r.timeForBrickF = lastGame.timeForBrickDecreaseFactor;
        return r;
    }
}
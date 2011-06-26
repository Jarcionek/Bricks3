package MainPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class MenuView extends JPanel {
    /**
     * Last created instance of <code>MenuView</code> class
     */
    private static MenuView menuView;

    private static final int BUTTON_HEIGHT = 40;

    public static final int MODE_CALM = 1;
    public static final int MODE_RUSH = 2;
    
    public static final int DIFFICULTY_EASY = 1;
    public static final int DIFFICULTY_MEDIUM = 2;
    public static final int DIFFICULTY_HARD = 3;

    /**
     * first container containing background image
     */
    private JLabel mainPane;
    /**
     * the most top empty container for displaying messages
     */
    private JPanel messagePane;
    private JLabel menu;
    private JPanel mainMenuOptions;
    private JPanel modeOptions;
    private JPanel difficultyOptions;

    private ButtonGroup mainMenuButtonGroup = new ButtonGroup();
    private ButtonGroup modeButtonGroup = new ButtonGroup();
    private ButtonGroup difficultyButtonGroup = new ButtonGroup();

    private int mode;
    private int difficulty;

    /**
     * For highest scores view.<p>
     * 0 - calm easy<p>
     * 1 - calm medium<p>
     * 2 - calm hard<p>
     * 3 - rush easy<p>
     * 4 - rush medium<p>
     * 5 - rush hard<p>
     */
    private int currentScore;
    private JLabel difficultyScoresLabel;

    private Button backInHelp;
    private Button backInScores;
    private Button leftInScores;
    private Button rightInScores;

    private Button loadButton;

    public MenuView() {
        super();

        menuView = this;

        this.setPreferredSize(new Dimension(800, 600));
        this.setLayout(null);
        mainPane = new JLabel(new ImageIcon(this.getClass().getResource("/Background.jpg")));
        mainPane.setBounds(0, 0, 800, 600);
        mainPane.setLayout(new GridBagLayout());

        messagePane = new JPanel(new GridBagLayout());
        messagePane.setBounds(0, 0, 800, 600);
        messagePane.setOpaque(false);

        this.add(messagePane);
        this.add(mainPane);

        menu = new JLabel();
        menu.setLayout(new GridBagLayout());
        menu.setHorizontalAlignment(JLabel.CENTER);
        menu.setIcon(new ImageIcon(this.getClass().getResource("/MenuBackground.png")));
        mainPane.add(menu);
        
        initializeMenuOptions();
        initializeModeOptions();
        initializeDifficultyOptions();

        menu.add(mainMenuOptions);
    }
    private void initializeMenuOptions() {
        mainMenuOptions = new JPanel(new GridBagLayout());
        mainMenuOptions.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = new Insets(15, 0, 15, 0);

        final Button play = new Button("/Play.png", "/PlayH.png") {
            @Override
            public void doClick() {
                super.doClick();
                MenuView.this.menu.removeAll();
                MenuView.this.menu.add(modeOptions);
                MenuView.this.menu.revalidate();
                MenuView.this.menu.repaint();
            }
        };
        play.addMouseListener(new ButtonMouseListener(play));
        mainMenuOptions.add(play, c);

        final Button scores = new Button("/Scores.png", "/ScoresH.png") {
            @Override
            public void doClick() {
                super.doClick();
                displayScores();
            }
        };
        scores.addMouseListener(new ButtonMouseListener(scores));
        mainMenuOptions.add(scores, c);

        Button settings = new Button("/Settings.png", "/SettingsH.png");
        settings.addMouseListener(new ButtonMouseListener(settings) {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
            }
        });
//        mainMenuOptions.add(settings, c);

        final Button help = new Button("/Help.png", "/HelpH.png") {
            @Override
            public void doClick() {
                super.doClick();
                displayHelp();
            }
        };
        help.addMouseListener(new ButtonMouseListener(help));
        mainMenuOptions.add(help, c);

        final Button exit = new Button("/Exit.png", "/ExitH.png") {
            @Override
            public void doClick() {
                super.doClick();
                try {
                    Thread.sleep(150); //wait for sound to finish
                } catch(InterruptedException ex) {
                } finally {
                    System.exit(0);
                }
            }
        };
        exit.addMouseListener(new ButtonMouseListener(exit));
        mainMenuOptions.add(exit, c);

        mainMenuButtonGroup.add(play);
        mainMenuButtonGroup.add(scores);
        mainMenuButtonGroup.add(help);
        mainMenuButtonGroup.add(exit);
    }
    private void initializeModeOptions() {
        modeOptions = new JPanel(new GridBagLayout());
        modeOptions.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = new Insets(15, 0, 15, 0);

        Button modeLabel = new Button("/ModeC.png", "/ModeC.png");
        modeOptions.add(modeLabel, c);

        loadButton = new Button("/Load.png", "/LoadH.png");
        loadButton.addMouseListener(new ButtonMouseListener(loadButton) {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Save.use();
            }
        });
        modeOptions.add(loadButton, c);
        loadButton.setVisible(Save.load());

        Button calmMode = new Button("/Calm.png", "/CalmH.png") {
            ImageIcon icon = new ImageIcon(this.getClass().getResource("/CalmC.png"));
            @Override
            public synchronized void doClick() {
                super.doClick();
                MenuView.this.menu.removeAll();
                MenuView.this.menu.add(difficultyOptions);
                MenuView.this.mode = MODE_CALM;
                ((JLabel) difficultyOptions.getComponent(0)).setIcon(icon);
                MenuView.this.menu.revalidate();
                MenuView.this.menu.repaint();
            }
        };
        calmMode.addMouseListener(new ButtonMouseListener(calmMode));
        modeOptions.add(calmMode, c);

        Button rushMode = new Button("/Rush.png", "/RushH.png") {
            ImageIcon icon = new ImageIcon(this.getClass().getResource("/RushC.png"));
            @Override
            public synchronized void doClick() {
                super.doClick();
                MenuView.this.menu.removeAll();
                MenuView.this.menu.add(difficultyOptions);
                MenuView.this.mode = MODE_RUSH;
                ((JLabel) difficultyOptions.getComponent(0)).setIcon(icon);
                MenuView.this.menu.revalidate();
                MenuView.this.menu.repaint();
            }
        };
        rushMode.addMouseListener(new ButtonMouseListener(rushMode));
        modeOptions.add(rushMode, c);

        Button back = new Button("/Back.png", "/BackH.png") {
            @Override
            public synchronized void doClick() {
                super.doClick();
                MenuView.this.menu.removeAll();
                MenuView.this.menu.add(mainMenuOptions);
                MenuView.this.menu.revalidate();
                MenuView.this.menu.repaint();
            }
        };
        back.addMouseListener(new ButtonMouseListener(back));
        modeOptions.add(back, c);

        modeButtonGroup.add(loadButton);
        modeButtonGroup.add(calmMode);
        modeButtonGroup.add(rushMode);
        modeButtonGroup.add(back);
    }
    private void initializeDifficultyOptions() {
        difficultyOptions = new JPanel(new GridBagLayout());
        difficultyOptions.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = new Insets(15, 0, 15, 0);

        JLabel difficultyLabel = new JLabel();
        difficultyLabel.setHorizontalAlignment(JLabel.CENTER);
        difficultyOptions.add(difficultyLabel, c);

        Button easy = new Button("/Easy.png", "/EasyH.png") {
            @Override
            public synchronized void doClick() {
                super.doClick();
                MenuView.this.difficulty = DIFFICULTY_EASY;
                startGame();
            }
        };
        easy.addMouseListener(new ButtonMouseListener(easy));
        difficultyOptions.add(easy, c);

        Button medium = new Button("/Medium.png", "/MediumH.png") {
            @Override
            public synchronized void doClick() {
                super.doClick();
                MenuView.this.difficulty = DIFFICULTY_MEDIUM;
                startGame();
            }
        };
        medium.addMouseListener(new ButtonMouseListener(medium));
        difficultyOptions.add(medium, c);

        Button hard = new Button("/Hard.png", "/HardH.png") {
            @Override
            public synchronized void doClick() {
                super.doClick();
                MenuView.this.difficulty = DIFFICULTY_HARD;
                startGame();
            }
        };
        hard.addMouseListener(new ButtonMouseListener(hard));
        difficultyOptions.add(hard, c);

        Button back = new Button("/Back.png", "/BackH.png") {
            @Override
            public synchronized void doClick() {
                super.doClick();
                MenuView.this.menu.removeAll();
                loadButton.setVisible(Save.load());
                MenuView.this.menu.add(modeOptions);
                MenuView.this.menu.revalidate();
                MenuView.this.menu.repaint();
            }
        };
        back.addMouseListener(new ButtonMouseListener(back));
        difficultyOptions.add(back, c);

        difficultyButtonGroup.add(easy);
        difficultyButtonGroup.add(medium);
        difficultyButtonGroup.add(hard);
        difficultyButtonGroup.add(back);
    }

    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    private void startGame() {
        GamesParameters p = null;

        switch(mode*10+difficulty) {
            case MODE_CALM*10+DIFFICULTY_EASY: {
                p = GamesParameters.CALM_EASY;
                break;
            }
            case MODE_CALM*10+DIFFICULTY_MEDIUM: {
                p = GamesParameters.CALM_MEDIUM;
                break;
            }
            case MODE_CALM*10+DIFFICULTY_HARD: {
                p = GamesParameters.CALM_HARD;
                break;
            }
            case MODE_RUSH*10+DIFFICULTY_EASY: {
                p = GamesParameters.RUSH_EASY;
                break;
            }
            case MODE_RUSH*10+DIFFICULTY_MEDIUM: {
                p = GamesParameters.RUSH_MEDIUM;
                break;
            }
            case MODE_RUSH*10+DIFFICULTY_HARD: {
                p = GamesParameters.RUSH_HARD;
                break;
            }
        }

        Scores.setDifficulty(p.difficulty);
        Game game = new Game(p);

        MenuView.this.mainPane.removeAll();
        MenuView.this.mainPane.add(game);
        MenuView.this.mainPane.revalidate();
        MenuView.this.mainPane.repaint();
    }
    public static void startCustomGame(GamesParameters p, int mode, int difficulty) {
        menuView.mode = mode;
        menuView.difficulty = difficulty;
        Scores.setDifficulty(p.difficulty);

        menuView.mainPane.removeAll();
        menuView.mainPane.add(new Game(p));
        menuView.mainPane.revalidate();
        menuView.mainPane.repaint();

        Main.requestWindowFocus();
    }

    public static void restoreMenu() {
        menuView.mainPane.removeAll();
        menuView.loadButton.setVisible(Save.load());
        menuView.mainPane.add(menuView.menu);
        menuView.mainPane.revalidate();
        menuView.mainPane.repaint();
    }

    public static void clearMessages() {
        menuView.messagePane.removeAll();
    }

    /**
     * Displays in mainPane
     */
    private void displayScores() {
        currentScore = 0;

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Board.BORDERS_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
            leftInScores = new Button("/Left.png", "/LeftH.png") {
                @Override
                public synchronized void doClick() {
                    super.doClick();
                    Rectangle x = new Rectangle(leftInScores.getLocationOnScreen(),
                            leftInScores.getPreferredSize());
                    if(x.contains(MouseInfo.getPointerInfo().getLocation())) {
                        leftInScores.setHighlighted(true);
                    }
                    if(currentScore == 0) {
                        currentScore = 5;
                    } else {
                        currentScore--;
                    }
                    displayDifficultyInScores();
                    
                }
            };
            leftInScores.addMouseListener(new ButtonMouseListener(leftInScores));
            topPanel.add(leftInScores, BorderLayout.WEST);

            difficultyScoresLabel = new JLabel(new ImageIcon(MenuView.class.getResource("/CalmEasy.png")));
            difficultyScoresLabel.setPreferredSize(new Dimension(360, 50));
            topPanel.add(difficultyScoresLabel, BorderLayout.CENTER);

            rightInScores = new Button("/Right.png", "/RightH.png") {
                @Override
                public synchronized void doClick() {
                    super.doClick();
                    Rectangle x = new Rectangle(rightInScores.getLocationOnScreen(),
                            rightInScores.getPreferredSize());
                    if(x.contains(MouseInfo.getPointerInfo().getLocation())) {
                        rightInScores.setHighlighted(true);
                    }
                    if(currentScore == 5) {
                        currentScore = 0;
                    } else {
                        currentScore++;
                    }
                    displayDifficultyInScores();
                }
            };
            rightInScores.addMouseListener(new ButtonMouseListener(rightInScores));
            topPanel.add(rightInScores, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        panel.add(Scores.getCellsForTable(), BorderLayout.CENTER);

        backInScores = new Button("/Back.png", "/BackH.png") {
            @Override
            public synchronized void doClick() {
                super.doClick();
                MenuView.this.mainPane.removeAll();
                MenuView.this.mainPane.add(menu);
                MenuView.this.mainPane.revalidate();
                MenuView.this.mainPane.repaint();
            }
        };
        backInScores.addMouseListener(new ButtonMouseListener(backInScores));
        JPanel backPanel = new JPanel(new GridBagLayout());
        backPanel.setPreferredSize(new Dimension(200, 50));
        backPanel.setOpaque(false);
        backPanel.add(backInScores);
        panel.add(backPanel, BorderLayout.SOUTH);

        mainPane.removeAll();
        mainPane.add(panel);
        mainPane.revalidate();
        mainPane.repaint();
    }
    private void displayDifficultyInScores() {
        switch(currentScore) {
            case 0: Scores.applyDifficultyToCells(Scores.DIFFICULTY_CALM_EASY);
            difficultyScoresLabel.setIcon(new ImageIcon(this.getClass().getResource("/CalmEasy.png"))); break;
            case 1: Scores.applyDifficultyToCells(Scores.DIFFICULTY_CALM_MEDIUM);
            difficultyScoresLabel.setIcon(new ImageIcon(this.getClass().getResource("/CalmMedium.png"))); break;
            case 2: Scores.applyDifficultyToCells(Scores.DIFFICULTY_CALM_HARD);
            difficultyScoresLabel.setIcon(new ImageIcon(this.getClass().getResource("/CalmHard.png"))); break;
            case 3: Scores.applyDifficultyToCells(Scores.DIFFICULTY_RUSH_EASY);
            difficultyScoresLabel.setIcon(new ImageIcon(this.getClass().getResource("/RushEasy.png"))); break;
            case 4: Scores.applyDifficultyToCells(Scores.DIFFICULTY_RUSH_MEDIUM);
            difficultyScoresLabel.setIcon(new ImageIcon(this.getClass().getResource("/RushMedium.png"))); break;
            case 5: Scores.applyDifficultyToCells(Scores.DIFFICULTY_RUSH_HARD);
            difficultyScoresLabel.setIcon(new ImageIcon(this.getClass().getResource("/RushHard.png")));break;
        }
    }

    /**
     * Displays in mainPane
     */
    private void displayHelp() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Board.BORDERS_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)));

        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel[][] temp = new JLabel[3][2];
        for(int i = 0; i < temp.length; i++) {
            for(int j = 0; j < temp[j].length; j++) {
                temp[i][j] = new JLabel();
                temp[i][j].setFont(new Font("BankGothic Md BT", Font.BOLD, 18));
                if(j == 0) {
                    temp[i][j].setForeground(new Color(0, 146, 63));
                } else {
                    temp[i][j].setForeground(Game.COLOR_TEXT);
                }
                topPanel.add(temp[i][j]);
            }
        }
        temp[0][0].setText("Author:");
        temp[1][0].setText("Version:");
        temp[2][0].setText("Released:");
        temp[0][1].setText("Jaroslaw Pawlak");
        temp[1][1].setText(Main.VERSION);
        temp[2][1].setText(Main.RELEASED);


        panel.add(topPanel, BorderLayout.NORTH);


        backInHelp = new Button("/Back.png", "/BackH.png") {
            @Override
            public synchronized void doClick() {
                super.doClick();
                MenuView.this.mainPane.removeAll();
                MenuView.this.mainPane.add(menu);
                MenuView.this.mainPane.revalidate();
                MenuView.this.mainPane.repaint();
            }
        };
        backInHelp.addMouseListener(new ButtonMouseListener(backInHelp));
        JPanel backPanel = new JPanel(new GridBagLayout());
        backPanel.setPreferredSize(new Dimension(200, BUTTON_HEIGHT+10));
        backPanel.setOpaque(false);
        backPanel.add(backInHelp);
        panel.add(backPanel, BorderLayout.SOUTH);


        mainPane.removeAll();
        mainPane.add(panel);
        mainPane.revalidate();
        mainPane.repaint();
    }

    /**
     * Displays in mainPane
     */
    public static void displayCustomGameStarter() {
        menuView.mainPane.removeAll();
        menuView.mainPane.add(new CustomGameStarter());
        menuView.mainPane.revalidate();
        menuView.mainPane.repaint();
    }

    /**
     * Displays in messagePane
     * @param isScoreHighest
     */
    public static void displayGameOverMessage(boolean isScoreHighest) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Board.BORDERS_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)));

        GridBagConstraints c = new GridBagConstraints();

        final JTextField textField = new JTextField("Anonymous");
        Button menuLabel = new Button("/MenuG.png", "/MenuGH.png") {
            @Override
            public synchronized void doClick() {
                super.doClick();
                Scores.saveScore(textField.getText());
                MenuView.clearMessages();
                MenuView.restoreMenu();
                Main.requestWindowFocus();
            }
        };
        Button restartLabel = new Button("/Restart.png","/RestartH.png") {
            @Override
            public synchronized void doClick() {
                super.doClick();
                Scores.saveScore(textField.getText());
                MenuView.clearMessages();
                menuView.startGame();
                Main.requestWindowFocus();
            }
        };

        if(isScoreHighest) {
            JLabel label = new JLabel("Highest score!");
            label.setFont(new Font("BankGothic Md BT", Font.BOLD, 30));
            label.setForeground(Game.COLOR_TEXT);
            label.setOpaque(false);

            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.insets = new Insets(5, 5, 5, 5);
            panel.add(label, c);

            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setFont(new Font("BankGothic Md BT", Font.BOLD, 18));
            nameLabel.setForeground(Game.COLOR_TEXT);
            nameLabel.setOpaque(false);

            c.gridy = 1;
            c.gridwidth = 1;
            panel.add(nameLabel, c);

            textField.selectAll();
            textField.setSelectionColor(Game.COLOR_TEXT);
            textField.setFont(new Font("BankGothic Md BT", Font.PLAIN, 18));
            textField.setForeground(Game.COLOR_TEXT);
            textField.setBackground(Board.BORDERS_COLOR);
            textField.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

            c.gridx = 1;
            c.fill = GridBagConstraints.BOTH;
            panel.add(textField, c);

            c.gridy = 2;
            c.gridx = 0;
            c.fill = GridBagConstraints.NONE;
            panel.add(menuLabel, c);

            c.gridx = 1;
            panel.add(restartLabel, c);
        } else {
            JLabel label = new JLabel("Game over!");
            label.setFont(new Font("BankGothic Md BT", Font.BOLD, 30));
            label.setForeground(new Color(0, 146, 63));
            label.setOpaque(false);

            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.insets = new Insets(5, 5, 5, 5);
            panel.add(label, c);

            c.gridy = 1;
            c.gridwidth = 1;
            panel.add(menuLabel, c);

            c.gridx = 1;
            panel.add(restartLabel, c);
        }

        menuLabel.addMouseListener(new ButtonMouseListener(menuLabel));
        restartLabel.addMouseListener(new ButtonMouseListener(restartLabel));

        menuView.messagePane.add(panel);
        menuView.messagePane.revalidate();
        menuView.messagePane.repaint();
        if(textField != null) {
            textField.requestFocusInWindow();
        }
    }

    /**
     * Displays in messagePane
     */
    public static void displayNoMoreMovesMessage() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Board.BORDERS_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)));
        JLabel label = new JLabel(new ImageIcon(
                MenuView.class.getResource("/NoMoreMoves.png")));
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(label);

        menuView.messagePane.add(panel);
        menuView.messagePane.revalidate();
        menuView.messagePane.repaint();
    }

    public static void keyPressed(int key) {
        if(menuView.mainMenuOptions.isShowing()) {
            if(key == KeyEvent.VK_UP) {
                menuView.mainMenuButtonGroup.moveHighlight(ButtonGroup.LEFT);
            } else if(key == KeyEvent.VK_DOWN) {
                menuView.mainMenuButtonGroup.moveHighlight(ButtonGroup.RIGHT);
            } else if(key == KeyEvent.VK_ENTER) {
                Button b = menuView.mainMenuButtonGroup.getHighlighted();
                if(b != null) {
                    b.doClick();
                }
            } else if(key == KeyEvent.VK_ESCAPE) {
                Button b = menuView.mainMenuButtonGroup.getHighlighted();
                if(b != null) {
                    b.setHighlighted(false);
                }
                menuView.mainMenuButtonGroup.getLast().doClick();
            }
        } else if(menuView.modeOptions.isShowing()) {
            if(key == KeyEvent.VK_UP) {
                menuView.modeButtonGroup.moveHighlight(ButtonGroup.LEFT);
            } else if(key == KeyEvent.VK_DOWN) {
                menuView.modeButtonGroup.moveHighlight(ButtonGroup.RIGHT);
            } else if(key == KeyEvent.VK_ENTER) {
                Button b = menuView.modeButtonGroup.getHighlighted();
                if(b != null) {
                    b.doClick();
                }
            } else if(key == KeyEvent.VK_ESCAPE) {
                Button b = menuView.modeButtonGroup.getHighlighted();
                if(b != null) {
                    b.setHighlighted(false);
                }
                menuView.modeButtonGroup.getLast().doClick();
            }
        } else if(menuView.difficultyOptions.isShowing()) {
            if(key == KeyEvent.VK_UP) {
                menuView.difficultyButtonGroup.moveHighlight(ButtonGroup.LEFT);
            } else if(key == KeyEvent.VK_DOWN) {
                menuView.difficultyButtonGroup.moveHighlight(ButtonGroup.RIGHT);
            } else if(key == KeyEvent.VK_ENTER) {
                Button b = menuView.difficultyButtonGroup.getHighlighted();
                if(b != null) {
                    b.doClick();
                }
            } else if(key == KeyEvent.VK_ESCAPE) {
                Button b = menuView.difficultyButtonGroup.getHighlighted();
                if(b != null) {
                    b.setHighlighted(false);
                }
                menuView.difficultyButtonGroup.getLast().doClick();
            }
        } else if(menuView.backInHelp != null && menuView.backInHelp.isShowing()) {
            if(key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
                Sounds.playButtonHighlited();
                menuView.backInHelp.setHighlighted(true);
            } else if(key == KeyEvent.VK_ENTER) {
                if(menuView.backInHelp.isHighlighted()) {
                    menuView.backInHelp.doClick();
                }
            } else if(key == KeyEvent.VK_ESCAPE) {
                menuView.backInHelp.doClick();
            }
        } else if(menuView.backInScores != null && menuView.backInScores.isShowing()) {
            if(key == KeyEvent.VK_LEFT) {
                menuView.leftInScores.doClick();
            } else if(key == KeyEvent.VK_RIGHT) {
                menuView.rightInScores.doClick();
            } else if(key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
                Sounds.playButtonHighlited();
                menuView.backInScores.setHighlighted(true);
            } else if(key == KeyEvent.VK_ENTER) {
                if(menuView.backInScores.isHighlighted()) {
                    menuView.backInScores.doClick();
                }
            } else if(key == KeyEvent.VK_ESCAPE) {
                menuView.backInScores.doClick();
            }
        }
    }
}
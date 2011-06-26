package MainPack;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;

/**
 *
 * @author Jaroslaw Pawlak
 */
public class CustomGameStarter extends JPanel {
    JLabel size = new JLabel("Bricks in row/column:");
    JPanel sizePanel = new JPanel(new GridLayout(1, 3));
    JRadioButton size0 = new JRadioButton("specified");
    JRadioButton size10 = new JRadioButton("10");
    JRadioButton size11 = new JRadioButton("11");

    JLabel colors = new JLabel("Number of colors:");
    JSlider colorsSlider = defaultSlider(1, 10, 7);

    JLabel colorsF = new JLabel("Colors increase factor:");
    JTextField colorsFField = defaultTextField("0.2");

    JLabel timeForBrick = new JLabel("Time for brick (miliseconds):");
    JTextField timeForBrickField = defaultTextField("1000");

    JLabel timeForBrickF = new JLabel("Time for brick decrease factor:");
    JTextField timeForBrickFField = defaultTextField("0.9");

    JLabel maxTime = new JLabel("Maximum time (seconds):");
    JTextField maxTimeField = defaultTextField("60");

    JLabel maxTimeF = new JLabel("Maximum time decrease factor:");
    JTextField maxTimeFField = defaultTextField("0.9");

    JLabel mode = new JLabel("Mode:");
    JPanel modePanel = new JPanel(new GridLayout(1, 2));
    JRadioButton modeCalm = new JRadioButton("calm");
    JRadioButton modeRush = new JRadioButton("rush");

    JLabel difficulty = new JLabel("Difficulty:");
    JPanel difficultyPanel = new JPanel(new GridLayout(1, 3));
    JRadioButton difficultyEasy = new JRadioButton("easy");
    JRadioButton difficultyMedium = new JRadioButton("medium");
    JRadioButton difficultyHard = new JRadioButton("hard");

    JLabel score = new JLabel("Score:");
    JTextField scoreField = defaultTextField("0");

    JLabel level = new JLabel("Level:");
    JTextField levelField = defaultTextField("0");

    JButton backButton = new JButton("Back");
    JButton startButton = new JButton("Start");

    public CustomGameStarter() {
        this.setLayout(new GridLayout(0, 2));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        size10.setSelected(true);
        javax.swing.ButtonGroup group0 = new javax.swing.ButtonGroup();
        group0.add(size0);
        group0.add(size10);
        group0.add(size11);
        sizePanel.add(size0);
        sizePanel.add(size10);
        sizePanel.add(size11);

        modeCalm.setSelected(true);
        javax.swing.ButtonGroup group1 = new javax.swing.ButtonGroup();
        group1.add(modeCalm);
        group1.add(modeRush);
        modePanel.add(modeCalm);
        modePanel.add(modeRush);

        difficultyEasy.setSelected(true);
        javax.swing.ButtonGroup group2 = new javax.swing.ButtonGroup();
        group2.add(difficultyEasy);
        group2.add(difficultyMedium);
        group2.add(difficultyHard);
        difficultyPanel.add(difficultyEasy);
        difficultyPanel.add(difficultyMedium);
        difficultyPanel.add(difficultyHard);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuView.restoreMenu();
                Main.requestWindowFocus();
            }
        });
        
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean ok = true;
                int min = 0;
                int max = 0;
                
                GamesParameters p = new GamesParameters();

                if(size0.isSelected()) {
                    p.size = 0;
                } else if(size10.isSelected()) {
                    p.size = 10;
                } else if(size11.isSelected()) {
                    p.size = 11;
                }
                p.colors
                        = colorsSlider.getValue();
                if(p.colors <= 2 && !size0.isSelected()) {
                    ok = false;
                    size0.setSelected(true);
                }
                try {
                    min = 0; max = 2;
                    p.colorsF = Float.parseFloat(colorsFField.getText());
                    if(p.colorsF < min || p.colorsF > max) {
                        throw new NumberFormatException();
                    }
                    colorsFField.setText("" + p.colorsF);
                } catch (NumberFormatException ex) {
                    colorsFField.setText("must be: <" + min + ", " + max + ">");
                    ok = false;
                }
                try {
                    min = 0; max = 1000;
                    p.timeForBrick = Integer.parseInt(timeForBrickField.getText());
                    if(p.timeForBrick < min || p.timeForBrick > max) {
                        throw new NumberFormatException();
                    }
                    timeForBrickField.setText("" + p.timeForBrick);
                } catch (NumberFormatException ex) {
                    timeForBrickField.setText("must be: <" + min + ", " + max + ">");
                    ok = false;
                }
                try {
                    min = 0; max = 2;
                    p.timeForBrickF = Float.parseFloat(timeForBrickFField.getText());
                    if(p.timeForBrickF < min || p.timeForBrickF > max) {
                        throw new NumberFormatException();
                    }
                    timeForBrickFField.setText("" + p.timeForBrickF);
                } catch (NumberFormatException ex) {
                    timeForBrickFField.setText("must be: <" + min + ", " + max + ">");
                    ok = false;
                }
                try {
                    min = 5; max = 3600;
                    p.maxTime = Integer.parseInt(maxTimeField.getText());
                    if(p.maxTime < min || p.maxTime > max) {
                        throw new NumberFormatException();
                    }
                    maxTimeField.setText("" + p.maxTime);
                    p.maxTime *= 1000;
                    p.currentTime = p.maxTime;
                } catch (NumberFormatException ex) {
                    maxTimeField.setText("must be: <" + min + ", " + max + ">");
                    ok = false;
                }
                try {
                    min = 0; max = 2;
                    p.maxTimeF = Float.parseFloat(maxTimeFField.getText());
                    if(p.maxTimeF < min || p.maxTimeF > max) {
                        throw new NumberFormatException();
                    }
                    maxTimeFField.setText("" + p.maxTimeF);
                } catch (NumberFormatException ex) {
                    maxTimeFField.setText("must be: <" + min + ", " + max + ">");
                    ok = false;
                }
                try {
                    min = 0;
                    p.score = Integer.parseInt(scoreField.getText());
                    if(p.score < min) {
                        throw new NumberFormatException();
                    }
                    scoreField.setText("" + p.score);
                } catch (NumberFormatException ex) {
                    scoreField.setText("must be non-negative integer");
                    ok = false;
                }
                try {
                    min = 0;
                    p.level = Integer.parseInt(levelField.getText());
                    if(p.level < min) {
                        throw new NumberFormatException();
                    }
                    levelField.setText("" + p.level);
                } catch (NumberFormatException ex) {
                    levelField.setText("must be non-negative integer");
                    ok = false;
                }

                if(ok) {
                    int mode = 0;
                    int difficulty = 0;
                    if(modeCalm.isSelected()) {
                        mode = MenuView.MODE_CALM;
                    } else if(modeRush.isSelected()) {
                        mode = MenuView.MODE_RUSH;
                    }
                    if(difficultyEasy.isSelected()) {
                        difficulty = MenuView.DIFFICULTY_EASY;
                    } else if(difficultyMedium.isSelected()) {
                        difficulty = MenuView.DIFFICULTY_MEDIUM;
                    } else if(difficultyHard.isSelected()) {
                        difficulty = MenuView.DIFFICULTY_HARD;
                    }
                    switch(10*mode+difficulty) {
                        case MenuView.MODE_CALM*10+MenuView.DIFFICULTY_EASY: {
                            p.difficulty = Scores.DIFFICULTY_CALM_EASY;
                            break;
                        }
                        case MenuView.MODE_CALM*10+MenuView.DIFFICULTY_MEDIUM: {
                            p.difficulty = Scores.DIFFICULTY_CALM_MEDIUM;
                            break;
                        }
                        case MenuView.MODE_CALM*10+MenuView.DIFFICULTY_HARD: {
                            p.difficulty = Scores.DIFFICULTY_CALM_HARD;
                            break;
                        }
                        case MenuView.MODE_RUSH*10+MenuView.DIFFICULTY_EASY: {
                            p.difficulty = Scores.DIFFICULTY_RUSH_EASY;
                            break;
                        }
                        case MenuView.MODE_RUSH*10+MenuView.DIFFICULTY_MEDIUM: {
                            p.difficulty = Scores.DIFFICULTY_RUSH_MEDIUM;
                            break;
                        }
                        case MenuView.MODE_RUSH*10+MenuView.DIFFICULTY_HARD: {
                            p.difficulty = Scores.DIFFICULTY_RUSH_HARD;
                            break;
                        }
                    }
                    MenuView.startCustomGame(p, mode, difficulty);
                }
            }
        });

        this.add(size);
        this.add(sizePanel);
        this.add(colors);
        this.add(colorsSlider);
        this.add(colorsF);
        this.add(colorsFField);
        this.add(timeForBrick);
        this.add(timeForBrickField);
        this.add(timeForBrickF);
        this.add(timeForBrickFField);
        this.add(maxTime);
        this.add(maxTimeField);
        this.add(maxTimeF);
        this.add(maxTimeFField);
        this.add(mode);
        this.add(modePanel);
        this.add(difficulty);
        this.add(difficultyPanel);
        this.add(score);
        this.add(scoreField);
        this.add(level);
        this.add(levelField);
        this.add(backButton);
        this.add(startButton);
    }

    private JSlider defaultSlider(int min, int max, int value) {
        JSlider r = new JSlider(JSlider.HORIZONTAL, min, max, value);
        r.setLabelTable(r.createStandardLabels(1));
        r.setPaintLabels(true);
        r.setSnapToTicks(true);
        return r;
    }

    private JTextField defaultTextField(String text) {
        JTextField r = new JTextField(text);
        r.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField field = (JTextField) e.getSource();
                field.selectAll();
            }
        });
        return r;
    }
}

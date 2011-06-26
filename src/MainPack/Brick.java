package MainPack;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

public class Brick extends JLabel {
    /**
     * Predefined array of possible <code>Bricks</code> '<code>Colors</code>.
     */
    public static final Color[] COLORS = {
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

    public static final int SPECIAL_BOMB_3x3 = 1;
    public static final int SPECIAL_ENTIRE_ROW = 2;
    public static final int SPECIAL_ENTIRE_COLUMN = 3;

    public static final int SPECIAL_BOMB_5x5 = 4;
    public static final int SPECIAL_ROW_AND_COLUMN = 5;

    public static final int SPECIAL_ALL_COLOR = 6;

    private static final RandomMachine randomMachine = init();

    private int color;
    private int special;

    private static RandomMachine init() {
        RandomMachine x = new RandomMachine();
        x.put(new Integer(SPECIAL_ALL_COLOR), 1);
        x.put(new Integer(SPECIAL_ROW_AND_COLUMN), 1);
        x.put(new Integer(SPECIAL_BOMB_5x5), 1);
        x.put(new Integer(SPECIAL_ENTIRE_COLUMN), 2);
        x.put(new Integer(SPECIAL_ENTIRE_ROW), 2);
        x.put(new Integer(SPECIAL_BOMB_3x3), 3);
        x.put(new Integer(0), 500 - x.getAllChances());
        return x;
    }
    private Brick() {};
    public Brick(int color) {
        super();
        this.setOpaque(true);
        this.setHorizontalAlignment(JLabel.CENTER);

        this.setColor(color);
        this.setSpecial(((Integer) randomMachine.get()).intValue());
        this.setHighlighted(false);
    }
    public void setColor(int color) {
        if(color < 0) {
            throw new IllegalArgumentException("Illegal color: " + color
                    + ", cannot be negative value");
        } else if(color >= COLORS.length) {
            throw new IllegalArgumentException("Illegal color: " + color
                    + ", max: " + (COLORS.length-1));
        }
        this.color = color;
        this.setBackground(COLORS[color]);
        this.repaint();
    }
    public int getColor() {
        return color;
    }
    /**
     * Returns true if and only if colors of <code>this</code> and
     * <code>brick</code> are the same.
     */
    public boolean equals(Brick brick) {
        return this.color == brick.color;
    }
    public void setSpecial(int special) {
        if(special < 0 || special > 6) {
            throw new IllegalArgumentException("unknown special type: " + special);
        }
        this.special = special;
        switch(special) {
            case SPECIAL_BOMB_3x3:
                setIcon(new ImageIcon(this.getClass().getResource("/B3x3.png")));
                break;
            case SPECIAL_BOMB_5x5:
                setIcon(new ImageIcon(this.getClass().getResource("/B5x5.png")));
                break;
            case SPECIAL_ENTIRE_ROW:
                setIcon(new ImageIcon(this.getClass().getResource("/R.png")));
                break;
            case SPECIAL_ENTIRE_COLUMN:
                setIcon(new ImageIcon(this.getClass().getResource("/Cn.png")));
                break;
            case SPECIAL_ROW_AND_COLUMN:
                setIcon(new ImageIcon(this.getClass().getResource("/RC.png")));
                break;
            case SPECIAL_ALL_COLOR:
                setIcon(new ImageIcon(this.getClass().getResource("/Cr.png")));
                break;
            default: setIcon(null);
        }
    }
    public int getSpecial() {
        return special;
    }
    public void setHighlighted(boolean highlighted) {
        if (highlighted) {
            int R = 255;
            int G = 200;
            int B = 0;
            Color leftTop = new Color(R, G, B);
            Color rightBottom = new Color(R/4, G/4, B/4);

            this.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createBevelBorder(BevelBorder.RAISED,
                    leftTop, //1
                    rightBottom), //4
                    BorderFactory.createBevelBorder(BevelBorder.RAISED,
                    leftTop.darker().darker(), //2
                    rightBottom.brighter().brighter()))); //3
        } else {
            this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }
    }
}
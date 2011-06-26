package MainPack;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;

public class Board extends JLabel {
    /**
     * Space around the brick where background is clearly visible.
     */
    public static final int MARGIN = 5;
    public static final Color BORDERS_COLOR = new Color(143, 84, 68);

    private static int[][] specifiedBoard = { //10x10
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {2, 3, 2, 3, 2, 3, 2, 3, 2, 3},
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {2, 3, 2, 3, 2, 3, 2, 3, 2, 3},
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {2, 3, 2, 3, 2, 3, 2, 3, 2, 3},
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {2, 3, 2, 3, 2, 3, 2, 3, 2, 3},
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {2, 3, 2, 3, 2, 3, 2, 3, 2, 3},
        };

    private static Board lastBoard;

    private static final RandomMachine chance3 = init3();
    private static final RandomMachine chance4 = init4();
    private static final RandomMachine chance5 = init5();
    private static final RandomMachine chance6 = init6();
    private static final RandomMachine chance7 = init7();

    private final Timer TIMER;
    final int SIZE;
    final int BRICK_SIZE;

    private JPanel bricksPane;
    private Brick[][] bricks;

    private JPanel hidersPane;
    private JLabel[][] hiders;

    private Point firstSelection;
    private Point secondSelection;

    private boolean[][] toRemove;
    private boolean[][] toRemoveFromSpecial;
    private boolean[][] toFall;
    private Brick[] newBricks;

    private boolean mouseBlocked = false;

    private static int[] lastBackgrounds = {-1, -1, -1};
    private int numberOfHiders;

    private Point[] specialBricksCoordinates;
    private int[] specialBricks;

    private static RandomMachine init3() {
        RandomMachine x = new RandomMachine(100);
        x.put(new Integer(Brick.SPECIAL_BOMB_3x3), 5);
        return x;
    }
    private static RandomMachine init4() {
        RandomMachine x = new RandomMachine(100);
        x.put(new Integer(Brick.SPECIAL_BOMB_3x3), 24);
        x.put(new Integer(Brick.SPECIAL_ENTIRE_ROW), 3);
        x.put(new Integer(Brick.SPECIAL_ENTIRE_COLUMN), 3);
        return x;
    }
    private static RandomMachine init5() {
        RandomMachine x = new RandomMachine(100);
        x.put(new Integer(Brick.SPECIAL_BOMB_3x3), 34);
        x.put(new Integer(Brick.SPECIAL_ENTIRE_ROW), 5);
        x.put(new Integer(Brick.SPECIAL_ENTIRE_COLUMN), 5);
        x.put(new Integer(Brick.SPECIAL_BOMB_5x5), 3);
        x.put(new Integer(Brick.SPECIAL_ROW_AND_COLUMN), 3);
        return x;
    }
    private static RandomMachine init6() {
        RandomMachine x = new RandomMachine(100);
        x.put(new Integer(Brick.SPECIAL_BOMB_3x3), 25);
        x.put(new Integer(Brick.SPECIAL_ENTIRE_ROW), 5);
        x.put(new Integer(Brick.SPECIAL_ENTIRE_COLUMN), 5);
        x.put(new Integer(Brick.SPECIAL_BOMB_5x5), 5);
        x.put(new Integer(Brick.SPECIAL_ROW_AND_COLUMN), 5);
        x.put(new Integer(Brick.SPECIAL_ALL_COLOR), 5);
        return x;
    }
    private static RandomMachine init7() {
        RandomMachine x = new RandomMachine(100);
        x.put(new Integer(Brick.SPECIAL_BOMB_3x3), 10);
        x.put(new Integer(Brick.SPECIAL_ENTIRE_ROW), 7);
        x.put(new Integer(Brick.SPECIAL_ENTIRE_COLUMN), 7);
        x.put(new Integer(Brick.SPECIAL_BOMB_5x5), 8);
        x.put(new Integer(Brick.SPECIAL_ROW_AND_COLUMN), 8);
        x.put(new Integer(Brick.SPECIAL_ALL_COLOR), 10);
        return x;
    }

    /**
     * @param size number of bricks' in row and column
     * @param bricksNo number of different bricks colors
     */
    public Board(int size, Timer timer) {
        super();
        lastBoard = this;

        boolean useSpecifiedBoard = size == 0;

        TIMER = timer;
        SIZE = useSpecifiedBoard? specifiedBoard.length : size;
        BRICK_SIZE = 550/SIZE;

        toRemove = new boolean[SIZE][SIZE];
        toRemoveFromSpecial = new boolean[SIZE][SIZE];
        toFall = new boolean[SIZE][SIZE];
        newBricks = new Brick[SIZE];

        this.setBackground(BORDERS_COLOR);
        this.setLayout(null);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)));

        bricksPane = new JPanel(null);
        bricksPane.setOpaque(false);
        bricksPane.setBounds(this.getInsets().left, this.getInsets().top, 550, 550);
        bricksPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (Board.this.isMouseBlocked() || Timer.isPaused() || Game.isOver()) {
                    return;
                }
                Board.this.setMouseBlocked(true);

                int x = e.getX()/BRICK_SIZE;
                int y = e.getY()/BRICK_SIZE;
                if (x < 0) {
                    x = 0;
                } else if (x >= SIZE) {
                    x = SIZE - 1;
                }
                if (y < 0) {
                    y = 0;
                } else if (y >= SIZE) {
                    y = SIZE - 1;
                }

                if(Main.isCheatsActive() && e.getButton() == MouseEvent.BUTTON3) {
                    final int xFinal = x;
                    final int yFinal = y;
                    JPopupMenu popup = new JPopupMenu();

                    JMenu specials = new JMenu("Specials");
                    JMenuItem[] specialsMenuItems = new JMenuItem[7];
                    specialsMenuItems[0] = new JMenuItem();
                    specialsMenuItems[0].setPreferredSize(new Dimension(30, 30));
                    specials.add(specialsMenuItems[0]);
                    specials.add(specialsMenuItems[1] = new JMenuItem(new ImageIcon(this.getClass().getResource("/B3x3.png"))));
                    specials.add(specialsMenuItems[2] = new JMenuItem(new ImageIcon(this.getClass().getResource("/R.png"))));
                    specials.add(specialsMenuItems[3] = new JMenuItem(new ImageIcon(this.getClass().getResource("/Cn.png"))));
                    specials.add(specialsMenuItems[4] = new JMenuItem(new ImageIcon(this.getClass().getResource("/B5x5.png"))));
                    specials.add(specialsMenuItems[5] = new JMenuItem(new ImageIcon(this.getClass().getResource("/RC.png"))));
                    specials.add(specialsMenuItems[6] = new JMenuItem(new ImageIcon(this.getClass().getResource("/Cr.png"))));
                    for(int i = 0; i < 7; i++) {
                        final int iFinal = i;
                        specialsMenuItems[i].addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                bricks[xFinal][yFinal].setSpecial(iFinal);
                            }
                        });
                    }

                    JMenu colors = new JMenu("Colors");
                    JMenuItem[] colorMenuItems = new JMenuItem[Brick.COLORS.length];
                    for(int i = 0; i < colorMenuItems.length; i++) {
                        final int iFinal = i;
                        ImageIcon icon = new ImageIcon() {
                            @Override
                            public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
                                super.paintIcon(c, g, x, y);
                                g.setColor(Brick.COLORS[iFinal]);
                                g.fillRect(x, y, 15, 15);
                            }
                            @Override
                            public int getIconHeight() {
                                return 16;
                            }
                            @Override
                            public int getIconWidth() {
                                return 16;
                            }
                        };
                        colorMenuItems[i] = new JMenuItem(icon);
                        colorMenuItems[i].addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                bricks[xFinal][yFinal].setColor(iFinal);
                            }
                        });
                        colors.add(colorMenuItems[i]);
                    }

                    popup.add(colors);
                    popup.add(specials);
                    popup.show(bricks[x][y], e.getX() - x*BRICK_SIZE, e.getY() - y*BRICK_SIZE);
                    Board.this.setMouseBlocked(false);
                    return;
                }

                if (firstSelection == null) { // NO SELECTION
                    firstSelection = new Point(x, y);
                    bricks[x][y].setHighlighted(true);
                    Board.this.setMouseBlocked(false);
                } else { // SELECTION
                    if (isFirstSelectionAdjacentTo(x, y)) { // ADJACENT
                        bricks[firstSelection.x][firstSelection.y].setHighlighted(false);
                        secondSelection = new Point(x, y);
                        if (isSwapAllowed()) {
                            Timer.setAnimationInProgress(true);
                            SwapWorker worker = new SwapWorker(Board.this);
                            worker.execute();
                        } else {
                            firstSelection = null;
                            secondSelection = null;
                            Board.this.setMouseBlocked(false);
                        }
                    } else if (firstSelection.x == x // THE SAME = UNHIGHLIGHT
                            && firstSelection.y == y) {
                        firstSelection = null;
                        bricks[x][y].setHighlighted(false);
                        Board.this.setMouseBlocked(false);
                    } else { // FAR FROM FIRST = HIGHLIGHT NEW
                        bricks[firstSelection.x][firstSelection.y].setHighlighted(false);
                        firstSelection = new Point(x, y);
                        bricks[x][y].setHighlighted(true);
                        Board.this.setMouseBlocked(false);
                    }
                }
            }
        });
        this.add(bricksPane);

        createBoard(useSpecifiedBoard);

        hidersPane = new JPanel(new GridLayout(SIZE, SIZE));
        hidersPane.setOpaque(false);
        hidersPane.setBounds(this.getInsets().left, this.getInsets().top, 550, 550);
        this.add(hidersPane);

        hiders = new JLabel[SIZE][SIZE];
        for (int i = 0; i < hiders.length; i++) {
            for (int j = 0; j < hiders[i].length; j++) {
                hiders[i][j] = new JLabel();
                hiders[i][j].setOpaque(true);
                hiders[i][j].setBackground(BORDERS_COLOR);
                hiders[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                hidersPane.add(hiders[i][j]);
            }
        }

        this.randomizeNewBackground();
    }
    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }
    public synchronized void setBricksHidden(boolean hidden) {
        for(Component e : bricksPane.getComponents()) {
            e.setVisible(!hidden);
        }
    }

    private void createBoard(boolean useSpecifiedBoard) {
        bricks = new Brick[SIZE][SIZE];

        Random randomize = new Random();
        boolean notOK;
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                if(useSpecifiedBoard) {
                    bricks[i][j] = new Brick(specifiedBoard[i][j]);
                } else {
                    do {
                        notOK = false;
                        bricks[i][j] = new Brick(randomize.nextInt(Game.getBricksColors()));
                        try {
                            boolean vertical = bricks[i][j].equals(bricks[i-2][j])
                                    && bricks[i][j].equals(bricks[i-1][j]);
                            notOK = vertical;
                        } catch (IndexOutOfBoundsException ex) {}
                        try {
                            boolean horizontal = bricks[i][j].equals(bricks[i][j-2])
                                    && bricks[i][j].equals(bricks[i][j-1]);
                            notOK = notOK || horizontal;
                        } catch (IndexOutOfBoundsException ex) {}
                    } while (notOK);
                }
            }
        }

        if(!useSpecifiedBoard && getPossibleMoves() == 0) {
            createBoard(false);
        } else {
            for(int i = 0; i < SIZE; i++) {
                for(int j = 0; j < SIZE; j++) {
                    bricksPane.add(bricks[i][j]);
                    bricks[i][j].setBounds(
                            MARGIN+i*BRICK_SIZE, MARGIN+j*BRICK_SIZE,
                            BRICK_SIZE-2*MARGIN, BRICK_SIZE-2*MARGIN);
                }
            }
        }
    }
    private boolean isFirstSelectionAdjacentTo(int x, int y) {
        return firstSelection.x == x - 1 && firstSelection.y == y
            || firstSelection.x == x + 1 && firstSelection.y == y
            || firstSelection.x == x && firstSelection.y == y - 1
            || firstSelection.x == x && firstSelection.y == y + 1;
    }

    public synchronized Point getFirstSelectionCoordinates() {
        return firstSelection;
    }
    public synchronized Point getSecondSelectionCoordinates() {
        return secondSelection;
    }
    public synchronized Brick getFirstSelection() {
        return bricks[firstSelection.x][firstSelection.y];
    }
    public synchronized Brick getSecondSelection() {
        return bricks[secondSelection.x][secondSelection.y];
    }
    public synchronized Brick getBrick(int x, int y) {
        return bricks[x][y];
    }
    /**
     * Swaps Bricks' references in <code>bricksArray</code>.
     */
    public synchronized void swapSelections() {
        Brick temp = bricks[firstSelection.x][firstSelection.y];
        bricks[firstSelection.x][firstSelection.y] =
                bricks[secondSelection.x][secondSelection.y];
        bricks[secondSelection.x][secondSelection.y] = temp;
    }
    public synchronized void clearSelections() {
        firstSelection = null;
        secondSelection = null;
    }

    public synchronized boolean isMouseBlocked() {
        return mouseBlocked;
    }

    public synchronized void setMouseBlocked(boolean mouseBlocked) {
        this.mouseBlocked = mouseBlocked;
    }

    public boolean isSomethingToVanish() {
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                try {
                    if (bricks[i][j].equals(bricks[i-1][j])
                            && bricks[i][j].equals(bricks[i+1][j])) {
                        return true;
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {}
                try {
                    if (bricks[i][j].equals(bricks[i][j-1])
                            && bricks[i][j].equals(bricks[i][j+1])) {
                        return true;
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {}
            }
        }
        return false;
    }
    public boolean isSwapAllowed() {
        boolean result;
        swapSelections();
        result = isSomethingToVanish();
        swapSelections();
        return result;
    }
    /**
     * Flags bricks to remove taking into account those affected but special
     * bricks. Prepares new special bricks to create.
     */
    public synchronized void fillToRemoveArray() {
        ArrayList<Point> toRemoveBricks = new ArrayList<Point>();
        for (int i = 0; i < toRemove.length; i++) {
            for (int j = 0; j < toRemove[i].length; j++) {
                try {
                    if (bricks[i][j].equals(bricks[i-1][j])
                            && bricks[i][j].equals(bricks[i+1][j])) {
                        toRemove[i-1][j] = true;
                        toRemove[i][j] = true;
                        toRemove[i+1][j] = true;
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {}
                try {
                    if (bricks[i][j].equals(bricks[i][j-1])
                            && bricks[i][j].equals(bricks[i][j+1])) {
                        toRemove[i][j-1] = true;
                        toRemove[i][j] = true;
                        toRemove[i][j+1] = true;
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {}
            }
        }
        for (int i = 0; i < toRemove.length; i++) {
            for (int j = 0; j < toRemove[i].length; j++) {
                if(toRemove[i][j]) {
                    toRemoveBricks.add(new Point(i, j));
                    switch(bricks[i][j].getSpecial()) {
                        case Brick.SPECIAL_BOMB_3x3:
                            bomb3x3(i, j);
                            break;
                        case Brick.SPECIAL_BOMB_5x5:
                            bomb5x5(i, j);
                            break;
                        case Brick.SPECIAL_ENTIRE_ROW:
                            entireRow(j);
                            break;
                        case Brick.SPECIAL_ENTIRE_COLUMN:
                            entireColumn(i);
                            break;
                        case Brick.SPECIAL_ROW_AND_COLUMN:
                            entireColumn(i);
                            entireRow(j);
                            break;
                        case Brick.SPECIAL_ALL_COLOR:
                            allColor(bricks[i][j].getColor());
                            break;
                    }
                }
            }
        }
        switch(toRemoveBricks.size()) {
            case 0: throw new RuntimeException("Less than 3 bricks have vanished");
            case 1: throw new RuntimeException("Less than 3 bricks have vanished");
            case 2: throw new RuntimeException("Less than 3 bricks have vanished");
            case 3: prepareSpecialBricks(toRemoveBricks, 1, chance3); break;
            case 4: prepareSpecialBricks(toRemoveBricks, 1, chance4); break;
            case 5: prepareSpecialBricks(toRemoveBricks, 1, chance5); break;
            case 6: prepareSpecialBricks(toRemoveBricks, 2, chance6); break;
            case 7: prepareSpecialBricks(toRemoveBricks, 2, chance7); break;
            case 8: prepareSpecialBricks(toRemoveBricks, 2, chance7); break;
            case 9: prepareSpecialBricks(toRemoveBricks, 2, chance7); break;
            case 10: prepareSpecialBricks(toRemoveBricks, 3, chance7); break;
            case 11: prepareSpecialBricks(toRemoveBricks, 3, chance7); break;
            case 12: prepareSpecialBricks(toRemoveBricks, 3, chance7); break;
            case 13: prepareSpecialBricks(toRemoveBricks, 3, chance7); break;
            case 14: prepareSpecialBricks(toRemoveBricks, 4, chance7); break;
            case 15: prepareSpecialBricks(toRemoveBricks, 4, chance7); break;
            case 16: prepareSpecialBricks(toRemoveBricks, 4, chance7); break;
            case 17: prepareSpecialBricks(toRemoveBricks, 4, chance7); break;
            default: prepareSpecialBricks(toRemoveBricks, 5, chance7);
        }
        for (int i = 0; i < toRemoveFromSpecial.length; i++) {
            for (int j = 0; j < toRemoveFromSpecial[i].length; j++) {
                toRemove[i][j] |= toRemoveFromSpecial[i][j];
                toRemoveFromSpecial[i][j] = false;
            }
        }
    }
    private void prepareSpecialBricks(ArrayList<Point> toRemoveBricks,
            int numberOfSpecialBricks, RandomMachine randomMachine) {
//        System.out.println(toRemoveBricks.size() + " vanished bricks");
        specialBricksCoordinates = new Point[numberOfSpecialBricks];
        specialBricks = new int[numberOfSpecialBricks];
        Random random = new Random();
        for(int i = 0; i < numberOfSpecialBricks; i++) {
            specialBricksCoordinates[i] = toRemoveBricks
                    .remove(random.nextInt(toRemoveBricks.size()));
            Integer special = (Integer) randomMachine.get();
            if(special != null) {
                specialBricks[i] = special.intValue();
            }
        }
    }
    public void applySpecialBricks() {
        for(int i = 0; i < specialBricksCoordinates.length; i++) {
            bricks[specialBricksCoordinates[i].x][specialBricksCoordinates[i].y]
                    .setSpecial(specialBricks[i]);
        }
    }
    private void bomb3x3(int i, int j) {
        for(int a = i-1; a <= i+1; a++) {
            for(int b = j-1; b <= j+1; b++) {
                try {
                    toRemoveFromSpecial[a][b] = true;
                } catch (IndexOutOfBoundsException ex) {}
            }
        }
    }
    private void bomb5x5(int i, int j) {
        for(int a = i-2; a <= i+2; a++) {
            for(int b = j-2; b <= j+2; b++) {
                try {
                    toRemoveFromSpecial[a][b] = true;
                } catch (IndexOutOfBoundsException ex) {}
            }
        }
    }
    private void entireRow(int i) {
        for(int a = 0; a < toRemoveFromSpecial.length; a++) {
            toRemoveFromSpecial[a][i] = true;
        }
    }
    private void entireColumn(int i) {
        for(int a = 0; a < toRemoveFromSpecial.length; a++) {
            toRemoveFromSpecial[i][a] = true;
        }
    }
    private void allColor(int c) {
        for (int i = 0; i < toRemoveFromSpecial.length; i++) {
            for (int j = 0; j < toRemoveFromSpecial[i].length; j++) {
                if(bricks[i][j].getColor() == c) {
                    toRemoveFromSpecial[i][j] = true;
                }
            }
        }
    }
    public synchronized void fillToRemoveArrayWithAllTrues() {
        for (int i = 0; i < toRemove.length; i++) {
            for (int j = 0; j < toRemove[i].length; j++) {
                toRemove[i][j] = true;
            }
        }
    }
    /**
     * Removes bricks from bricksPane, sets bricks[i][j] to null and resets
     * toRemove to all falses
     */
    public synchronized void remove() {
        for (int i = 0; i < toRemove.length; i++) {
            for (int j = 0; j < toRemove[i].length; j++) {
                if(toRemove[i][j]) {
                    bricksPane.remove(bricks[i][j]);
                    bricks[i][j] = null;
                    toRemove[i][j] = false;
                }
            }
        }
    }
    /**
     * Reads <code>toRemove</code> array and returns the amount of bricks
     * which will vanish.
     */
    public synchronized int count() {
        int result = 0;
        for (int i = 0; i < toRemove.length; i++) {
            for (int j = 0; j < toRemove[i].length; j++) {
                if (toRemove[i][j]) {
                    result++;
                }
            }
        }
        return result;
    }
    public synchronized boolean isToRemove(int x, int y) {
        return toRemove[x][y];
    }
    /**
     * Fills toFall array. Creates new bricks, adds them to the bricksPane,
     * set bounds to be outside the container and sets proper references in
     * newBricks array.
     */
    public synchronized void fillToFallArray() {
        for (int i = SIZE - 1; i >= 0; i--) {
            for (int j = SIZE - 1; j >= 0; j--) {
                if (bricks[i][j] == null) {
                    for (int k = j-1; k >= 0; k--) {
                        toFall[i][k] = true;
                    }
                }
            }
        }

        Random randomize = new Random();
        for (int i = 0; i < newBricks.length; i++) {
            if (bricks[i][0] == null || toFall[i][0]) {
                newBricks[i] = new Brick(randomize.nextInt(Game.getBricksColors()));
                bricksPane.add(newBricks[i]);
                newBricks[i].setBounds(
                        MARGIN+i*BRICK_SIZE, MARGIN-BRICK_SIZE,
                        BRICK_SIZE-2*MARGIN, BRICK_SIZE-2*MARGIN);
            } else {
                newBricks[i] = null;
            }
        }
    }
    public synchronized boolean isFalling(int x, int y) {
        return toFall[x][y];
    }
    public synchronized Brick getNewBrick(int i) {
        return newBricks[i];
    }
    /**
     * Moves bricks one level down in bricks array (change references),
     * adds new bricks at top, finally clears toFall array.
     */
    public synchronized void moveOneLevelDown() {
        for (int i = SIZE - 1; i >= 0; i--) {
            for (int j = SIZE - 1; j > 0; j--) {
                if (bricks[i][j] == null) {
                    bricks[i][j] = bricks[i][j-1];
                    bricks[i][j-1] = null;
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            if (bricks[i][0] == null) {
                bricks[i][0] = newBricks[i];
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                toFall[i][j] = false;
            }
        }
    }
    public synchronized boolean containsEmptySpaces() {
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                if (bricks[i][j] == null) {
                    return true;
                }
            }
        }
        return false;
    }
    public synchronized int getPossibleMoves() {
        int result = 0;
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                Brick t = bricks[i][j];
                boolean r = false;

                try {
                    bricks[i][j] = bricks[i+1][j];
                    bricks[i+1][j] = t;
                    r = r || t(i, j);
                    r = r || t(i+1, j);
                    bricks[i+1][j] = bricks[i][j];
                    bricks[i][j] = t;
                    if(r) {
                        result++;
                        r = false;
                    }
                } catch (IndexOutOfBoundsException e) {}

                try {
                    bricks[i][j] = bricks[i][j+1];
                    bricks[i][j+1] = t;
                    r = r || t(i, j);
                    r = r || t(i, j+1);
                    bricks[i][j+1] = bricks[i][j];
                    bricks[i][j] = t;
                    if(r) {
                        result++;
                        r = false;
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        }
        return result;
    }
    /**
     * Checks if brick[x][y] is a part of a 3-element line
     */
    private boolean t(int x, int y) {
        boolean t = false;
        try {
            t = t || (bricks[x][y].equals(bricks[x-1][y]) && bricks[x][y].equals(bricks[x+1][y]));
        } catch (IndexOutOfBoundsException ex) {}
        try {
            t = t || (bricks[x][y].equals(bricks[x][y-1]) && bricks[x][y].equals(bricks[x][y+1]));
        } catch (IndexOutOfBoundsException ex) {}
        try {
            t = t || (bricks[x][y].equals(bricks[x-2][y]) && bricks[x][y].equals(bricks[x-1][y]));
        } catch (IndexOutOfBoundsException ex) {}
        try {
            t = t || (bricks[x][y].equals(bricks[x+2][y]) && bricks[x][y].equals(bricks[x+1][y]));
        } catch (IndexOutOfBoundsException ex) {}
        try {
            t = t || (bricks[x][y].equals(bricks[x][y-2]) && bricks[x][y].equals(bricks[x][y-1]));
        } catch (IndexOutOfBoundsException ex) {}
        try {
            t = t || (bricks[x][y].equals(bricks[x][y+2]) && bricks[x][y].equals(bricks[x][y+1]));
        } catch (IndexOutOfBoundsException ex) {}
        return t;
    }

    /**
     * Randomizes new background and sets all hiders visible.
     */
    public void randomizeNewBackground() {
        Random r = new Random();
        int n;
        do {
            n = r.nextInt(53);
        } while(lastBackgrounds[0] == n
                || lastBackgrounds[1] == n
                || lastBackgrounds[2] == n);

        lastBackgrounds[0] = lastBackgrounds[1];
        lastBackgrounds[1] = lastBackgrounds[2];
        lastBackgrounds[2] = n;

        for(int i = 0; i < hiders.length; i++) {
            for(int j = 0; j < hiders[i].length; j++) {
                hiders[i][j].setVisible(true);
            }
        }
        this.setIcon(new ImageIcon(this.getClass().getResource("/Backgrounds/" +
                    n + ".jpg")));
        numberOfHiders = hiders.length*hiders[0].length;
    }
    public synchronized void hideHiders() {
        for (int i = 0; i < toRemove.length; i++) {
            for (int j = 0; j < toRemove[i].length; j++) {
                if(toRemove[i][j] && hiders[j][i].isVisible()) {
                    hiders[j][i].setVisible(false);
                    numberOfHiders--;
                }
            }
        }
    }
    /**
     * Checks if number of remaining hiders is 0. If it is 0, restores hiders,
     * loads new background and changes the difficulty. Otherwie do nothing.
     */
    public synchronized void createNewHidersAndBackground() {
        if (numberOfHiders == 0) {
            randomizeNewBackground();
            Game.increaseBricksColors();
            Game.decreaseMaxTime();
            Game.decreaseTimeForBrick();
            Scores.levelUp();
            Game.levelUp();
            TIMER.setMaximum(Game.getMaxTime());
        }
    }

    public static int[][] getBoard() {
        int[][] r = new int[lastBoard.SIZE][lastBoard.SIZE];
        for(int i = 0; i < lastBoard.SIZE; i++) {
            for(int j = 0; j < lastBoard.SIZE; j++) {
                r[i][j] = lastBoard.bricks[i][j].getColor();
            }
        }
        return r;
    }
    public static void setSpecifiedBoard(int[][] board) {
        Board.specifiedBoard = board;
    }
    public static int[][] getSpecials() {
        int[][] r = new int[lastBoard.SIZE][lastBoard.SIZE];
        for(int i = 0; i < lastBoard.SIZE; i++) {
            for(int j = 0; j < lastBoard.SIZE; j++) {
                r[i][j] = lastBoard.bricks[i][j].getSpecial();
            }
        }
        return r;
    }
    public static void setSpecials(int [][] specials) {
        for(int i = 0; i < lastBoard.SIZE; i++) {
            for(int j = 0; j < lastBoard.SIZE; j++) {
                lastBoard.bricks[i][j].setSpecial(specials[i][j]);
            }
        }
    }
    public static int[]getLastBackgrounds() {
        return lastBackgrounds;
    }
    public static void setLastBackgrounds(int a, int b, int c) {
        lastBackgrounds[0] = a;
        lastBackgrounds[1] = b;
        lastBackgrounds[2] = c;
    }
    public static void setLastBackgroundAsCurrent() {
        lastBoard.setIcon(new ImageIcon(Board.class.getResource("/Backgrounds/" +
                    lastBackgrounds[2] + ".jpg")));
    }
    public static boolean[][] getHiders() {
        boolean[][] r = new boolean[lastBoard.SIZE][lastBoard.SIZE];
        for(int i = 0; i < lastBoard.SIZE; i++) {
            for(int j = 0; j < lastBoard.SIZE; j++) {
                r[i][j] = lastBoard.hiders[i][j].isVisible();
            }
        }
        return r;
    }
    public static void setHiders(boolean[][] hiders) {
        lastBoard.numberOfHiders = 0;
        for(int i = 0; i < hiders.length; i++) {
            for(int j = 0; j < hiders[i].length; j++) {
                if(hiders[i][j]) {
                    lastBoard.hiders[i][j].setVisible(true);
                    lastBoard.numberOfHiders++;
                } else {
                    lastBoard.hiders[i][j].setVisible(false);
                }
            }
        }
    }
    public static int getBoardsSize() {
        return lastBoard.SIZE;
    }
}
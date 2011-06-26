package MainPack;

import java.awt.Rectangle;
import javax.swing.SwingWorker;

public class RemoveWorker extends SwingWorker {
    private static SwingWorker worker;

    /**
     * The number of all steps in vanish animation.
     */
    private static final int STEPS_V = 10;
    /**
     * The total time of vanish animation (in miliseconds).
     */
    private static final int TIME_V = 250;
    /**
     * The number of steps in one-level-fall animation.
     */
    public static final int STEPS_F = 10;
    /**
     * The total time of one-level-fall animation (in miliseconds).
     */
    public static final int TIME_F = 150;

    private final Board BOARD;
    private final int BRICK_SIZE;

    public RemoveWorker(Board board) {
        super();
        worker = this;
        BOARD = board;
        BRICK_SIZE = board.BRICK_SIZE;

    }

    @Override
    protected Object doInBackground() throws Exception {
        boolean removeAll = false;
        while(true) {
            do {
                if(remove(removeAll)) {
                    return null;
                }
                removeAll = false;
                if(fall()) {
                    return null;
                }
//                BOARD.applySpecialBricks();
            } while(BOARD.isSomethingToVanish());

            if(BOARD.getPossibleMoves() != 0) {
                break;
            } else {
                removeAll = true;
            }
        }
        Save.save();
        return null;
    }

    @Override
    protected void done() {
        Timer.setAnimationInProgress(false);
        BOARD.setMouseBlocked(false);
    }

    /**
     * Returns true if worker's execution has been cancelled, false otherwise
     */
    private boolean remove(boolean removeAll) {
        if(removeAll) {
            BOARD.fillToRemoveArrayWithAllTrues();
            Timer.setPaused(true);
            MenuView.displayNoMoreMovesMessage();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {}
            MenuView.clearMessages();
            Timer.setPaused(false);
        } else {
            BOARD.fillToRemoveArray();
            BOARD.hideHiders();
            int b = BOARD.count();
            Timer.addTimeToAdd(b);
            Scores.addScore(b);
        }
        for (int i = 1; i <= STEPS_V; i++) {
            try {
                Thread.sleep(TIME_V/STEPS_V);
            } catch (InterruptedException ex) {}
            if (isCancelled()) {
                return true;
            }
            if (Timer.isPaused()) {
                i--;
                continue;
            }
            for(int j = 0; j < BOARD.SIZE; j++) {
                for(int k = 0; k < BOARD.SIZE; k++) {
                    if(BOARD.isToRemove(j, k)) {
                        Rectangle r = BOARD.getBrick(j, k).getBounds();
                        Rectangle n = new Rectangle(
                                r.x - (i-1)*BRICK_SIZE/STEPS_V/2 + i*BRICK_SIZE/STEPS_V/2,
                                r.y - (i-1)*BRICK_SIZE/STEPS_V/2 + i*BRICK_SIZE/STEPS_V/2,
                                r.width + (i-1)*BRICK_SIZE/STEPS_V - i*BRICK_SIZE/STEPS_V,
                                r.height + (i-1)*BRICK_SIZE/STEPS_V - i*BRICK_SIZE/STEPS_V);
                        BOARD.getBrick(j, k).setBounds(n);
                    }
                }
            }
        }
        BOARD.remove();
        return false;
    }

    /**
     * Returns true if worker's execution has been cancelled, false otherwise
     */
    private boolean fall() {
        do {
            BOARD.fillToFallArray();
            for (int i = 1; i <= STEPS_F; i++) {
                try {
                    Thread.sleep(TIME_F/STEPS_F);
                } catch (InterruptedException ex) {}
                if (isCancelled()) {
                    return true;
                }
                if (Timer.isPaused()) {
                    i--;
                    continue;
                }

                //existing bricks
                for(int j = 0; j < BOARD.SIZE; j++) {
                    for(int k = 0; k < BOARD.SIZE; k++) {
                        if(BOARD.isFalling(j, k) && BOARD.getBrick(j, k) != null) {
                            Rectangle r = BOARD.getBrick(j, k).getBounds();
                            r.translate(0, i*BRICK_SIZE/STEPS_F - (i-1)*BRICK_SIZE/STEPS_F);
                            BOARD.getBrick(j, k).setBounds(r);
                        }
                    }
                }

                //new bricks
                for(int j = 0; j < BOARD.SIZE; j++) {
                    if(BOARD.getNewBrick(j) != null) {
                        Rectangle r = BOARD.getNewBrick(j).getBounds();
                        r.translate(0, i*BRICK_SIZE/STEPS_F - (i-1)*BRICK_SIZE/STEPS_F);
                        BOARD.getNewBrick(j).setBounds(r);
                    }
                }
            }
            BOARD.moveOneLevelDown();
        } while (BOARD.containsEmptySpaces());
        BOARD.createNewHidersAndBackground();
        return false;
    }

    public static void cancelLastWorker(boolean mayInterruptIfRunning) {
        if(worker != null) {
            worker.cancel(mayInterruptIfRunning);
        }
    }
}
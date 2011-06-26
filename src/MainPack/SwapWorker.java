package MainPack;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.SwingWorker;

public class SwapWorker extends SwingWorker {
    private static SwingWorker worker;

    /**
     * The number of all steps in animation.
     */
    private static final int STEPS = 10;
    /**
     * The total time of animation (in miliseconds).
     */
    private static final int TIME = 150;

    private final Board BOARD;
    private final int BRICK_SIZE;

    private final Brick B_ONE;
    private final Brick B_TWO;
    private final Point C_ONE;
    private final Point C_TWO;
    private final Rectangle R_ONE;
    private final Rectangle R_TWO;

    private final boolean horizontal;

    public SwapWorker(Board board) {
        super();
        worker = this;
        BOARD = board;
        BRICK_SIZE = board.BRICK_SIZE;

        B_ONE = BOARD.getFirstSelection();
        B_TWO = BOARD.getSecondSelection();
        C_ONE = BOARD.getFirstSelectionCoordinates();
        C_TWO = BOARD.getSecondSelectionCoordinates();
        R_ONE = B_ONE.getBounds();
        R_TWO = B_TWO.getBounds();

        horizontal = C_ONE.y == C_TWO.y;
    }
    @Override
    protected Object doInBackground() throws Exception {
        int x = 0;
        int d = 0;
        if (horizontal) {
            d = C_TWO.x - C_ONE.x;
            for (int i = 1; i <= STEPS; i++) {
                try {
                    Thread.sleep(TIME/STEPS);
                } catch (InterruptedException ex) {}
                if(isCancelled()) {
                    return null;
                }
                if (Timer.isPaused()) {
                    i--;
                    continue;
                }
                x = BRICK_SIZE*(i-1)/STEPS;
                R_ONE.translate(d*(BRICK_SIZE*i/STEPS - x), 0);
                R_TWO.translate(-d*(BRICK_SIZE*i/STEPS - x), 0);
                B_ONE.setBounds(R_ONE);
                B_TWO.setBounds(R_TWO);
            }
        } else {
            d = C_TWO.y - C_ONE.y;
            for (int i = 1; i <= STEPS; i++) {
                try {
                    Thread.sleep(TIME/STEPS);
                } catch (InterruptedException ex) {}
                if(isCancelled()) {
                    return null;
                }
                if (Timer.isPaused()) {
                    i--;
                    continue;
                }
                x = BRICK_SIZE*(i-1)/STEPS;
                R_ONE.translate(0, d*(BRICK_SIZE*i/STEPS - x));
                R_TWO.translate(0, -d*(BRICK_SIZE*i/STEPS - x));
                B_ONE.setBounds(R_ONE);
                B_TWO.setBounds(R_TWO);
            }
        }
        return null;
    }
    @Override
    protected void done() {
        if(!isCancelled()) {
            BOARD.swapSelections();
            BOARD.clearSelections();
            (new RemoveWorker(BOARD)).execute();
        }
    }
    public static void cancelLastWorker(boolean mayInterruptIfRunning) {
        if(worker != null) {
            worker.cancel(mayInterruptIfRunning);
        }
    }
}
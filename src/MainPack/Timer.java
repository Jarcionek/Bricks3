package MainPack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingWorker;
import javax.swing.border.EtchedBorder;

public class Timer extends JProgressBar {
    private static SwingWorker worker;
    private static Timer lastTimer;

    private boolean pause = false;
    private int timeToAdd = 0;
    private boolean animationInProgress = false;

    private boolean active = true;

    public Timer(int currentTime, int maxTime) {
        super(JProgressBar.VERTICAL, 0, maxTime);
        this.setPreferredSize(new Dimension(30, 580));
        this.setValue(currentTime);
        this.setForeground(new Color(0, 147, 221));
        this.setBackground(Color.white);
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        lastTimer = this;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(Main.isCheatsActive() && !Game.isOver()) {
                    if(e.getButton() == MouseEvent.BUTTON1) { //LMB
                        int t = (Timer.this.getPreferredSize().height - e.getY())
                                * Timer.this.getMaximum()
                                / Timer.this.getPreferredSize().height;
                        Timer.this.setValue(t);
                    } else if (e.getButton() == MouseEvent.BUTTON3) { //RMB
                        JPopupMenu menu = new JPopupMenu();
                        JRadioButtonMenuItem n = new JRadioButtonMenuItem("normal");
                        n.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                Timer.this.setActive(true);
                            }
                        });
                        n.setSelected(Timer.this.isActive());
                        JRadioButtonMenuItem i = new JRadioButtonMenuItem("infinite");
                        i.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                Timer.this.setActive(false);
                            }
                        });
                        i.setSelected(!Timer.this.isActive());
                        javax.swing.ButtonGroup g = new javax.swing.ButtonGroup();
                        g.add(n);
                        g.add(i);
                        menu.add(n);
                        menu.add(i);

                        menu.show(Timer.this, e.getX(), e.getY());
                    }
                }
            }
        });
    }

    public void start() {
        worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                while(true) {
                    try {
                        Thread.sleep(10);
                    } catch(InterruptedException ex) {}
                    if(isCancelled()) {
                        return null;
                    }
                    if(Timer.isPaused()) {
                        continue;
                    }
                    Timer.this.setValue(Timer.this.getValue()
                            + Timer.this.getTimeToAdd() - 10);
                    Timer.this.repaint();
                    if(Timer.this.getValue() == 0 && !Timer.this.getAnimationInProgress()
                            && Timer.this.isActive()) {
                        Game.setGameOver();
                        return null;
                    }
                }
            }
        };
        worker.execute();
    }

    private synchronized int getTimeToAdd() {
        int time = timeToAdd;
        timeToAdd = 0;
        return time;
    }

    public static synchronized void addTimeToAdd(int bricks) {
        lastTimer.timeToAdd += bricks*Game.getTimeForBrick();
    }

    public static synchronized boolean isPaused() {
        return lastTimer.pause;
    }
    public static synchronized void setPaused(boolean paused) {
        lastTimer.pause = paused;
    }
    /**
     * Invokes
     * {@link SwingWorker#cancel(boolean mayInterruptIfRunning)}
     * on the last used SwingWorker.
     */
    public static void cancelLastWorker(boolean mayInterruptIfRunning) {
        if(worker != null) {
            worker.cancel(mayInterruptIfRunning);
        }
    }

    private synchronized boolean getAnimationInProgress() {
        return animationInProgress;
    }
    public static synchronized void setAnimationInProgress(boolean animation) {
        lastTimer.animationInProgress = animation;
    }

    private synchronized boolean isActive() {
        return active;
    }
    private synchronized void setActive(boolean active) {
        this.active = active;
    }
    public static synchronized int getCurrentTime() {
        return lastTimer.getValue();
    }
}
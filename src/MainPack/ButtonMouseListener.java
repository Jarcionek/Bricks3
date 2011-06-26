package MainPack;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ButtonMouseListener implements MouseListener {
    private Button button;

    private ButtonMouseListener() {}
    public ButtonMouseListener(Button button) {
        this.button = button;
    }
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        button.doClick();
    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {
        Sounds.playButtonHighlited();
        button.setHighlighted(true);
    }
    public void mouseExited(MouseEvent e) {
        button.setHighlighted(false);
    }
}

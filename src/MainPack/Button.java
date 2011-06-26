package MainPack;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Jaroslaw Pawlak
 */
public class Button extends JLabel {
    private ImageIcon normalIcon;
    private ImageIcon highlightedIcon;
    private ButtonGroup buttonGroup;

    public Button(String normal, String highlighted) {
        super();
        this.setHorizontalAlignment(JLabel.CENTER);
        normalIcon = new ImageIcon(this.getClass().getResource(normal));
        highlightedIcon = new ImageIcon(this.getClass().getResource(highlighted));
        this.setIcon(normalIcon);
        this.setPreferredSize(new Dimension(highlightedIcon.getIconWidth(),
                highlightedIcon.getIconHeight()));
    }
    public synchronized void setHighlighted(boolean highlighted) {
        if(highlighted) {
            if(buttonGroup != null) {
                buttonGroup.unhighlightAll();
            }
            this.setIcon(highlightedIcon);
        } else {
            this.setIcon(normalIcon);
        }
    }
    public boolean isHighlighted() {
        return this.getIcon().equals(highlightedIcon);
    }

    /**
     * ButtonGroup.add(Button) should be used instead which invokes this method.
     */
    public void assignButtonGroup(ButtonGroup buttonGroup) {
        this.buttonGroup = buttonGroup;
    }
    public synchronized void doClick() {
        Sounds.playButtonPressed();
        setHighlighted(false);
    }
}

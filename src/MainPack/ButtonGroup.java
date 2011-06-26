package MainPack;

import java.util.ArrayList;

public class ButtonGroup {
    public static final int LEFT = -1;
    public static final int RIGHT = 1;

    private ArrayList<Button> buttons = new ArrayList<Button>(5);
    public void add(Button button) {
        buttons.add(button);
        button.assignButtonGroup(this);
    }
    public void unhighlightAll() {
        for(Button e : buttons) {
            if(e.isHighlighted()) { //is it faster?
                e.setHighlighted(false);
            }
        }
    }
    public void moveHighlight(int direction) {
        if(direction == LEFT) {
            int i = 0;
            while(i < buttons.size()) {
                if(buttons.get(i).isHighlighted()) {
                    break;
                }
                i++;
            }
            if(i != 0) {
                buttons.get(i-1).setHighlighted(true);
            }
            Sounds.playButtonHighlited();
        } else if(direction == RIGHT) {
            int i = buttons.size() - 1;
            while(i >= 0) {
                if(buttons.get(i).isHighlighted()) {
                    break;
                }
                i--;
            }
            if(i != buttons.size() - 1) {
                buttons.get(i+1).setHighlighted(true);
            }
            Sounds.playButtonHighlited();
        } else {
            throw new IllegalArgumentException("illegal direction: " + direction);
        }
    }
    /**
     * Returns highlighted button or null if none from the buttons in group
     * is highlighted.
     */
    public Button getHighlighted() {
        for(Button e : buttons) {
            if(e.isHighlighted()) {
                return e;
            }
        }
        return null;
    }
    public Button getLast() {
        return buttons.get(buttons.size()-1);
    }
}
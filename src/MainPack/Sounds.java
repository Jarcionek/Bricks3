package MainPack;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sounds {
    private static AudioClip buttonHighlightSound = Applet.newAudioClip(Sounds.class.getResource("/highlight.wav"));
    private static AudioClip buttonPressedSound = Applet.newAudioClip(Sounds.class.getResource("/press.wav"));

    private Sounds() {}

    public static void playButtonHighlited() {
        buttonHighlightSound.play();
    }

    public static void playButtonPressed() {
        buttonPressedSound.play();
    }
}
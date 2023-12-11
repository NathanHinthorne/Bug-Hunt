package com.Butterfly.view;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * This class will play audio when called upon by
 * another method.
 */
public final class AudioManager {
    private static AudioManager audioManager; // to be used in singleton design pattern

    // Constants

    private static final double VOLUME_MODIFIER = 0.5; // 0.6



    // Separate media players for music and sound effects
    private MediaPlayer musicPlayer;
    private MediaPlayer sfxPlayer;


    // BUTTON SOUNDS
    public Media mouseClick;
    public Media menu1;
    public Media menu2;
    public Media menu3;
    public Media onElectronic;
    public Media offElectronic;
    public Media error;



    // MUSIC



    // HERO
    // (step1, step2, step3, step4 are from Valve's Team Fortress 2)
    public Media step1; // source: Valve's Team Fortress 2
    public Media step2; // source: Valve's Team Fortress 2
    public Media step3; // source: Valve's Team Fortress 2
    public Media step4; // source: Valve's Team Fortress 2
    public Media heroOof;
    public Media collect;

    // DIALOGUE
    public Media talking;

    // FUNNY SFX
    public Media bonk;

    private AudioManager() { // prevent outside instantiation
        preloadMediaFiles();
    }

    private void preloadMediaFiles() {

        // BUTTONS
        mouseClick = loadMedia("/audio/sfx/button_click.wav");
        menu1 = loadMedia("/audio/sfx/button_menu_1.wav");
        menu2 = loadMedia("/audio/sfx/button_menu_2.wav");
        menu3 = loadMedia("/audio/sfx/button_menu_3.wav");
        onElectronic = loadMedia("/audio/sfx/onElectronic.wav");
        offElectronic = loadMedia("/audio/sfx/offElectronic.wav");
        error = loadMedia("/audio/sfx/button_error.wav");

        // HEDGEHOG
        step1 = loadMedia("/audio/sfx/step1.wav");
        step2 = loadMedia("/audio/sfx/step2.wav");
        step3 = loadMedia("/audio/sfx/step3.wav");
        step4 = loadMedia("/audio/sfx/step4.wav");
        heroOof = loadMedia("/audio/sfx/oof.wav");
        collect = loadMedia("/audio/sfx/collect.wav");

        // DIALOGUE
        talking = loadMedia("/audio/sfx/dialogue_talking.wav");

        // FUNNY
        bonk = loadMedia("/audio/sfx/bonk.wav");

    }

    private Media loadMedia(final String resourcePath) {
//        System.out.println("Loading media: " + resourcePath);
        return new Media(getClass().getResource(resourcePath).toExternalForm()); // don't know which one works
    }

    public static AudioManager getInstance() { // to be used in singleton design pattern
        if (audioManager == null) {
            audioManager = new AudioManager();
        }
        return audioManager;
    }

    public void playSFX(final Media theSFX, final double theVolume) {
        // anonymous inner class that implements the Runnable interface
        Platform.runLater(() -> {
            sfxPlayer = new MediaPlayer(theSFX);
            sfxPlayer.setVolume(theVolume * VOLUME_MODIFIER);
            sfxPlayer.play();
        });
    }

    public void playMusic(final Media theMusic, final boolean theLoop, final double theVolume) {

        // anonymous inner class that implements the Runnable interface
        Platform.runLater(() -> {
            musicPlayer = new MediaPlayer(theMusic);
            musicPlayer.setVolume(theVolume * VOLUME_MODIFIER);

            if (theLoop) {
                musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            } else {
                musicPlayer.setCycleCount(1);
            }

            musicPlayer.play();
        });
    }

    /**
     * Method to stop all audio: sounds and music alike.
     */
    public void stopAll() {
        musicPlayer.stop();
        sfxPlayer.stop();
    }

    /**
     * Method to stop all music
     */
    public void stopMusic() {
        musicPlayer.stop();
    }

    /**
     * Checks if the sfxPlayer is playing a sound effect.
     * @return true if the sfxPlayer is playing a sound effect, false otherwise
     */
    public boolean isPlayingSFX() {
        return sfxPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    /**
     * Checks if the musicPlayer is playing music.
     * @return true if the musicPlayer is playing music, false otherwise
     */
    public boolean isPlayingMusic() {
        return musicPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }


}

package com.BugHunt.view;

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

    private static final double VOLUME_MODIFIER = 0.3; // 0.6


    // Separate media players for music and sound effects
    private MediaPlayer musicPlayer;
    private MediaPlayer sfxPlayer;


    // MUSIC
    public Media menuMusic;
    public Media gameMusic1;
    public Media gameMusic2;
    public Media gameMusic3;


    // UI SOUNDS
    public Media mouseClick;
    public Media menu1;
    public Media menu2;
    public Media menu3;
    public Media onElectronic;
    public Media offElectronic;
    public Media error;
    public Media fancyClick;
    public Media win;
    public Media blip;
    public Media pop;


    // HEDGEHOG
    // (step1, step2, step3, step4 are from Valve's Team Fortress 2)
    public Media step1; // source: Valve's Team Fortress 2
    public Media step2; // source: Valve's Team Fortress 2
    public Media step3; // source: Valve's Team Fortress 2
    public Media step4; // source: Valve's Team Fortress 2
    public Media heroOof;
    public Media collect;

    // BUGS
    public Media grasshopper;
    public Media firefly;
    public Media butterfly;
    public Media dragonfly;
    public Media bee;
    public Media wasp;
    public Media honeycomb; // drip, drip?
    public Media flower;


    // DIALOGUE
    public Media talking;

    // FUNNY SFX
    public Media bonk;

    private AudioManager() { // prevent outside instantiation
        preloadMediaFiles();
    }

    private void preloadMediaFiles() {

        // MUSIC
        menuMusic = loadMedia("/audio/music/forest_trail.mp3");
        gameMusic1 = loadMedia("/audio/music/into_the_woods.mp3");
        gameMusic2 = loadMedia("/audio/music/natural.mp3");
        gameMusic3 = loadMedia("/audio/music/nature.mp3");

        // UI
        mouseClick = loadMedia("/audio/sfx/UI/button_click.wav");
        menu1 = loadMedia("/audio/sfx/UI/button_menu_1.wav");
        menu2 = loadMedia("/audio/sfx/UI/button_menu_2.wav");
        menu3 = loadMedia("/audio/sfx/UI/button_menu_3.wav");
        onElectronic = loadMedia("/audio/sfx/UI/onElectronic.wav");
        offElectronic = loadMedia("/audio/sfx/UI/offElectronic.wav");
        error = loadMedia("/audio/sfx/UI/button_error.wav");
        fancyClick = loadMedia("/audio/sfx/UI/click_confirm.mp3");
        win = loadMedia("/audio/sfx/UI/win.mp3");
        blip = loadMedia("/audio/sfx/UI/dialogue_talking.wav");
        pop = loadMedia("/audio/sfx/UI/pop.mp3");

        // HEDGEHOG
        step1 = loadMedia("/audio/sfx/step1.wav");
        step2 = loadMedia("/audio/sfx/step2.wav");
        step3 = loadMedia("/audio/sfx/step3.wav");
        step4 = loadMedia("/audio/sfx/step4.wav");
        heroOof = loadMedia("/audio/sfx/funny/oof.wav");
        collect = loadMedia("/audio/sfx/UI/collect.wav");

        // BUGS
        grasshopper = loadMedia("/audio/sfx/bugs/grasshopper.wav");
        firefly = loadMedia("/audio/sfx/bugs/firefly.wav");
        butterfly = loadMedia("/audio/sfx/bugs/butterfly.wav");
        dragonfly = loadMedia("/audio/sfx/bugs/dragonfly.wav");
//        bee = loadMedia("/audio/sfx/bugs/bee.wav");
//        wasp = loadMedia("/audio/sfx/bugs/wasp.wav");
//        honeycomb = loadMedia("/audio/sfx/bugs/honeycomb.wav");
//        flower = loadMedia("/audio/sfx/bugs/flower.wav");

        // DIALOGUE
        talking = loadMedia("/audio/sfx/UI/dialogue_talking.wav");

        // FUNNY
        bonk = loadMedia("/audio/sfx/funny/bonk.wav");

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

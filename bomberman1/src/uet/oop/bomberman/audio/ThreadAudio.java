package uet.oop.bomberman.audio;

import javazoom.jl.player.Player;

import java.io.FileInputStream;

public class ThreadAudio implements Runnable {
    private String PATH_AUDIO;
    private boolean loop;

    public ThreadAudio(String path, boolean loop) {
        this.PATH_AUDIO = path;
        this.loop = loop;
    }

    public ThreadAudio(String path) {
        this(path, false);
    }

    @Override
    public void run() {
        FileInputStream fis;
        Player player;

        try {
            String path = AudioGame.class.getResource(PATH_AUDIO).getPath();
            do {
                fis = new FileInputStream(path);
                player = new Player(fis);
                player.play();
            } while (loop);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot play audio.");
        }
    }
}

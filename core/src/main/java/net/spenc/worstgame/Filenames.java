package net.spenc.worstgame;

public enum Filenames {
    PLAYER("tentacle_guy.png"),
    BIBL("bibl.png"),
    DONUT("doNUT.png"),
    SPIKE("spike.png"),
    SPRING("spring.png"),
    MUSIC("background_music.mp3"),
    MAP("level1.tmx");

    private String filename = "";

    Filenames(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

}

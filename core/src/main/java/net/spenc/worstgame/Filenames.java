package net.spenc.worstgame;

public enum Filenames {
    PLAYER("textures/tentacle_guy.png"),
    BIBL("textures/bibl.png"),
    DONUT("textures/doNUT.png"),
    SPIKE("textures/spike.png"),
    BfChicken("textures/buffChicken.png"),
    SPRING("textures/spring.png"),
    PORTAL("textures/level_portal.png"),
    MUSIC("music/background_music.mp3");

    private String filename;

    Filenames(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

}

package net.spenc.worstgame;

public enum Filenames {
    PLAYER("textures/tentacle_guy.png"),

    BIBL("textures/bibl.png"),
    BIBL_SFX("sfx/bibl.ogg"),

    DONUT("textures/doNUT.png"),
    DONUT_SFX("sfx/doNUT.ogg"),

    SPIKE("textures/spike.png"),
    SPIKE_SFX("sfx/spike.ogg"),

    BfChicken("textures/buffChicken.png"),

    SPRING("textures/spring.png"),
    SPRING_0_SFX("sfx/spring0.ogg"),
    SPRING_1_SFX("sfx/spring1.ogg"),

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

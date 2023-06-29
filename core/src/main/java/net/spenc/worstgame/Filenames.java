package net.spenc.worstgame;

public enum Filenames {
    PLAYER("textures/tentacle_guy.png"),

    BIBL("textures/bibl.png"),
    BIBL_SFX("sfx/bibl.ogg"),

    DONUT("textures/doNUT.png"),
    DONUT_SFX("sfx/doNUT.ogg"),

    SPIKE("textures/spike.png"),
    BfChicken("textures/buffChicken.png"),

    SPRING("textures/spring.png"),
    SPRING_0_SFX("sfx/spring0.ogg"),
    SPRING_1_SFX("sfx/spring1.ogg"),

    PORTAL("textures/level_portal.png"),
    MUSIC("music/background_music.mp3"),

    WAR_POPUP_1("textures/warad.png"),
    WAR_POPUP_2("textures/warad2.png"),
    WAR_POPUP_1S("textures/waradshooting.png"),
    WAR_POPUP_2S("textures/warad2shooting.png"),
    WAR_POPUP_LOOP("sfx/waradloop.ogg"),
    WAR_POPUP_SHOOT("sfx/waradshoot.ogg"),

    WIN_POPUP_1("textures/winad.png"),
    WIN_POPUP_2("textures/winad2.png"),
    WIN_POPUP_1S("textures/winadshooting.png"),
    WIN_POPUP_2S("textures/winad2shooting.png"),
    WIN_POPUP_LOOP("sfx/winadloop.ogg"),
    WIN_POPUP_SHOOT("sfx/winadshoot.ogg"),

    BABY_POPUP_1("textures/babyad.png"),
    BABY_POPUP_1S("textures/babyadshooting.png"),
    BABY_POPUP_LOOP("sfx/babyadloop.ogg"),
    BABY_POPUP_SHOOT("sfx/babyadshoot.ogg"),

    WIZARD_POPUP_1("textures/wizardad.png"),
    WIZARD_POPUP_2("textures/wizardad2.png"),
    WIZARD_POPUP_LOOP("sfx/wizardadloop.ogg"),

    CAR_POPUP_1("textures/carad.png"),
    CAR_POPUP_LOOP("sfx/caradloop.ogg"),

    HELL_ORB("textures/hellorb.png");

    private String filename;

    Filenames(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

}

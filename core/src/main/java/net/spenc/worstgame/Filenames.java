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

    BRED("textures/bred.png"),
    BRED_SFX("sfx/bred.ogg"),

    PORTAL("textures/level_portal.png"),
    MUSIC("music/background_music.mp3"),

    WAR_POPUP_1("textures/warad.png"),
    WAR_POPUP_2("textures/warad2.png"),
    WAR_POPUP_1S("textures/waradshooting.png"),
    WAR_POPUP_2S("textures/warad2shooting.png"),
    WAR_POPUP_LOOP("sfx/HolderMLW.mp3"),
    WAR_POPUP_SHOOT("sfx/laser.mp3"),

    WIN_POPUP_1("textures/winad.png"),
    WIN_POPUP_2("textures/winad2.png"),
    WIN_POPUP_1S("textures/winadshooting.png"),
    WIN_POPUP_2S("textures/winad2shooting.png"),
    WIN_POPUP_LOOP("sfx/slots.mp3"),
    WIN_POPUP_SHOOT("sfx/wincannon.wav"),

    BABY_POPUP_1("textures/babyad.png"),
    BABY_POPUP_1S("textures/babyadshooting.png"),
    BABY_POPUP_LOOP("sfx/babycry.wav"),
    BABY_POPUP_SHOOT("sfx/babyshot.wav"),

    WIZARD_POPUP_1("textures/wizardad.png"),
    WIZARD_POPUP_2("textures/wizardad2.png"),
    WIZARD_POPUP_LOOP("sfx/wizard.wav"),

    CAR_POPUP_1("textures/carad.png"),
    CAR_POPUP_LOOP("sfx/caralarm.wav"),

    FEARD_POPUP_1("textures/kingsfeard.png"),
    FEARD_POPUP_LOOP("sfx/feard.wav"),

    XXX_POPUP_1("textures/xxxlove.png"),
    XXX_POPUP_LOOP("sfx/xxx.wav"),

    HELLORB("textures/hellorb.png"),
    HELLORBKILL("sfx/hellorbkill.wav"),

    HOTWIZ_POPUP_1("textures/hotwizardsad.png"),
    HOTWIZ_POPUP_2("textures/hotwizardsad2.png"),
    HOTWIZ_POPUP_LOOP("sfx/hotwizard.wav");

    private String filename;

    Filenames(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

}

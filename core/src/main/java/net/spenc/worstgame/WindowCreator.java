package net.spenc.worstgame;

public interface WindowCreator {
    void newPopup(SharedData shared, String level);
    void newMain(SharedData shared, String level);
    void newOverlay(SharedData shared);
}

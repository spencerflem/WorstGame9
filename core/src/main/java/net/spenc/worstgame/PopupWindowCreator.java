package net.spenc.worstgame;

public interface PopupWindowCreator {
    void newPopup(WorstGame.GameType type);

    /**
     * Creates a new popup
     * Overlay = True means that the popup is floating above everything, tranparent, and unclickable
     * For the special effect of the character falling off the edge of a window
     * Idealy, this would also not have an icon.
     */
    void newPopup(WorstGame.GameType type, boolean overlay);
}

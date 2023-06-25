package net.spenc.worstgame;

import com.badlogic.gdx.Game;

public class WorstGame extends Game {

    public enum GameType {
        HOST,
        MAIN,
        POPUP,
        OVERLAY
    }
    public final PopupWindowCreator popupWindowCreator;
    public final GameType type;
    private final WorstScreen screen;

    public WorstGame(PopupWindowCreator popupWindowCreator, GameType type) {
        this.popupWindowCreator = popupWindowCreator;
        this.type = type;
        this.screen = new WorstScreen(this);
    }

    @Override
    public void create() {
        setScreen(screen);
    }

    @Override
    public void dispose() {
        screen.dispose();
    }
}

package net.spenc.worstgame;

import com.badlogic.gdx.Game;

public class WorstGame extends Game {

    public final PopupWindowCreator popupWindowCreator;

    public WorstGame(PopupWindowCreator popupWindowCreator) {
        this.popupWindowCreator = popupWindowCreator;
    }

    @Override
    public void create() {
        setScreen(new WorstScreen(this));
    }
}

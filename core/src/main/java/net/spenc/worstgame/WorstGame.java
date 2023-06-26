package net.spenc.worstgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public class WorstGame extends Game {

    public final PopupWindowCreator popupWindowCreator;
    public final boolean main;
    public final String level;

    public final AssetManager assets;

    public WorstGame(PopupWindowCreator popupWindowCreator, boolean main, String level, AssetManager assets) {
        this.popupWindowCreator = popupWindowCreator;
        this.main = main;
        this.level = level;
        this.assets = assets;
    }

    @Override
    public void create() {
        setScreen(new WorstScreen(this));
    }

    // auto-dispose
    @Override
    public void setScreen(Screen screen) {
        Screen oldScreen = getScreen();
        super.setScreen(screen);
        if (oldScreen != null) {
            oldScreen.dispose();
        }
    }

    @Override
    public void dispose() {
        screen.dispose();
    }
}

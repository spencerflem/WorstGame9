package net.spenc.worstgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public class WorstGame extends Game {

    public final PopupWindowCreator popupWindowCreator;
    public final boolean main;
    public final String initialLevel;
    public final AssetManager assets;

    public WorstGame(PopupWindowCreator popupWindowCreator, boolean main, String initialLevel, AssetManager assets) {
        this.popupWindowCreator = popupWindowCreator;
        this.main = main;
        this.initialLevel = initialLevel;
        this.assets = assets;
    }

    @Override
    public void create() {
        setScreen(new WorstScreen(this, initialLevel));
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
        if (main) {
            Gdx.app.exit();
        }
    }
}

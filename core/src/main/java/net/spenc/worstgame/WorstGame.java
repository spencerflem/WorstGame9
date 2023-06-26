package net.spenc.worstgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public class WorstGame extends Game {
    public final boolean main;
    public final String initialLevel;
    public final SharedData shared;

    public WorstGame(SharedData shared, boolean main, String initialLevel) {
        this.shared = shared;
        this.main = main;
        this.initialLevel = initialLevel;
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

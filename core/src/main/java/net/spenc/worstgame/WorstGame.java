package net.spenc.worstgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorstGame extends Game {

    public WindowManager windowManager;
    public AssetManager assets;
    public SpriteBatch batch;

    public WorstGame(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    @Override
    public void create() {
        this.assets = new AssetManager();
        this.batch = new SpriteBatch();
        setScreen(new LoadingScreen(this));
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

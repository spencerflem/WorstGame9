package net.spenc.worstgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Array;

public class ClientApp extends Game {
    public interface ClientScreen extends Screen {
        Array<Entity> getEntities();
    }

    public static class ClientScreenAdapter extends ScreenAdapter implements ClientScreen {
        private Array<Entity> entities = new Array<>();

        @Override
        public Array<Entity> getEntities() {
            return entities;
        }
    }

    private Input input;
    private Graphics graphics;
    @Override
    public void create() {
        input = Gdx.input;
        graphics = Gdx.graphics;
    }

    public Input getInput() {
        return input;
    }

    public Graphics getGraphics() {
        return graphics;
    }
}

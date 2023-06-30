package net.spenc.worstgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;

public class ClientApp extends Game {
    public interface ClientScreen extends Screen {
        Array<Entity> getEntities();
        int boundsLeft();
        int boundsRight();
        int boundsTop();
        int boundsBottom();
    }

    private Input input;
    @Override
    public void create() {
        input = Gdx.input;
    }

    public Input getInput() {
        return input;
    }
}

package net.spenc.worstgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class ClientApp extends Game {
    private Input input;
    @Override
    public void create() {
        input = Gdx.input;
    }

    public Input getInput() {
        return input;
    }
}

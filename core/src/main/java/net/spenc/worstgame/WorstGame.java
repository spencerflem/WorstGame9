package net.spenc.worstgame;

import com.badlogic.gdx.Game;

public class WorstGame extends Game {
    @Override
    public void create() {
        setScreen(new WorstScreen());
    }
}

package net.spenc.worst;

import com.badlogic.gdx.Game;

public class WorstGame extends Game {
    @Override
    public void create() {
        setScreen(new WorstScreen());
    }
}

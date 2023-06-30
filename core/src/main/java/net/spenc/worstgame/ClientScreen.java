package net.spenc.worstgame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;

public interface ClientScreen extends Screen {
    Array<Entity> getEntities();
}

package net.spenc.worstgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Entity {
    public float WIDTH = 1;
    public float HEIGHT = 1;

    public final Vector2 position = new Vector2();
    public Texture texture;

    public void draw(Batch batch) {
        batch.draw(texture, position.x, position.y, WIDTH, HEIGHT);
    }
}

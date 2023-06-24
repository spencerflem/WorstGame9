package net.spenc.worstgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Entity {
    public float width = 1;
    public float height = 1;

    public final Vector2 position = new Vector2();
    public Texture texture;

    public void draw(Batch batch) {
        batch.draw(texture, position.x, position.y, width, height);
    }
}

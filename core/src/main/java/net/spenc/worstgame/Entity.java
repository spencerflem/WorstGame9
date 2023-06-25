package net.spenc.worstgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Entity {
    public float width = 1;
    public float height = 1;

    public int layer = 0;

    public final Vector2 position = new Vector2();
    public Texture texture;

    public Entity WithTexture(Texture texture) {
        this.texture = texture;
        return this;
    }

    public Entity WithPosition(Vector2 position) {
        this.position.set(position.x, position.y);
        return this;
    }

    public Entity WithSize(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public Entity WithLayer(int layer) {
        this.layer = layer;
        return this;
    }

    public void draw(Batch batch) {
        batch.draw(texture, position.x, position.y, width, height);
    }

    public void update(float delta) {
    }
}

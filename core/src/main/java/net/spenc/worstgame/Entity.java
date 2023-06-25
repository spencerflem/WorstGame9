package net.spenc.worstgame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Entity {
    public UUID id = UUID.randomUUID();
    public float width = 1;
    public float height = 1;

    public int layer = 0;

    public final Vector2 position = new Vector2();
    public Texture texture;

    private HashSet<UUID> collidingWith = new HashSet<UUID>();

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

    // get hitbox (from position, width, height)
    private Rectangle getHitbox() {
        return new Rectangle(position.x, position.y, width, height);
    }

    private boolean overlaps(Entity other) {
        return this.getHitbox().overlaps(other.getHitbox());
    }

    public void updateCollsions(ArrayList<Entity> entities) {

        for (Entity other : entities) {
            if (collidingWith.contains(other.id)) {
                if (!overlaps(other)) {
                    onCollisionExit(other);
                    collidingWith.remove(other.id);
                }
            } else {
                if (overlaps(other)) {
                    onCollisionEnter(other);
                    collidingWith.add(other.id);
                }
            }

            if (other != this && overlaps(other)) {
                onCollision(other);
            }
        }
    }

    // on collision events
    public void onCollision(Entity other) {
        // Gdx.app.log("Entity", "collision with " + other);
    }

    public void onCollisionEnter(Entity other) {
        // Gdx.app.log("Entity", "collision enter with " + other);
    }

    public void onCollisionExit(Entity other) {
        // Gdx.app.log("Entity", "collision exit with " + other);
    }

    public void draw(Batch batch) {
        batch.draw(texture, position.x, position.y, width, height);
    }

    public void update(float delta) {
    }
}

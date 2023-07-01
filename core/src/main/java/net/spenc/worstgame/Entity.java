package net.spenc.worstgame;

import java.util.HashSet;
import java.util.UUID;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Entity {
    public UUID id = UUID.randomUUID();
    public float width = 1;
    public float height = 1;

    public int layer = 0;

    public final Vector2 position = new Vector2();
    public final Vector2 spawnPosition = new Vector2();
    public Texture texture;

    private final HashSet<UUID> collidingWith = new HashSet<>();

    private Sound sound = null;

    public Entity WithTexture(Texture texture) {
        this.texture = texture;
        return this;
    }

    /** sets the spawn position of the entity, (re)-starting position */
    public Entity WithSpawnPosition(Vector2 position) {
        this.position.set(position.x, position.y);
        this.spawnPosition.set(position.x, position.y);
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

    public void updateCollisions(Array<Entity> entities) {
        for (int i = 0; i < entities.size; i++) {
            Entity other = entities.get(i);
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

    public Entity WithSound(Sound sound) {
        this.sound = sound;
        return this;
    }

    public void playSound() {
        if (sound != null && System.getenv("DEV") == null) {
            sound.play();
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

    public void dispose() {
    }
}

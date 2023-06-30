package net.spenc.worstgame.entities;

import com.badlogic.gdx.graphics.g2d.Batch;

import net.spenc.worstgame.Entity;

public class Clamper extends Entity {
    // the clamper consists of two imaages that start at ymax and ymin
    // the objects will then meet in the middle and then go back to their original
    // positions

    private float ymin;
    private float ymax;

    private float secondY;

    private float speed = 0.5f;
    private boolean reverse = false;

    public Clamper WithYPositions(float ymin, float ymax) {
        this.ymin = ymin;
        this.ymax = ymax;
        return this;
    }

    public Clamper WithSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // both positions should move towards the midpoint (+ height offset), after
        // reaching the midpoint
        // they move back towards their initial y positions
        float midpoint = (ymin + ymax) / 2 - height / 2;

        if (reverse) {
            if (position.y > ymin) {
                position.y -= speed;
            } else {
                reverse = false;
            }
        } else {
            if (position.y < midpoint) {
                position.y += speed;
            } else {
                reverse = true;
            }
        }

        secondY = ymax - (position.y - ymin);

    }

    // we must draw twice for this entity
    @Override
    public void draw(Batch batch) {
        batch.draw(texture, position.x, position.y, width, height);
        batch.draw(texture, position.x, secondY, width, height);
    }

    @Override
    public void onCollisionEnter(Entity other) {
        // do nothing
    }

    @Override
    public void onCollision(Entity other) {
        // if this is the player, then we need to move the player with the platform
        if (other instanceof Player) {
            Player player = (Player) other;
            player.position.y = position.y + height;
            player.velocity.y = 0;

            // if the player is within range of the second position, then reset it
            if (player.position.y > secondY - height / 4 && player.position.y < secondY + height / 4) {
                if (player.root == null) {
                    this.playSound();
                }
                player.respawn();
            }

        }
    }
}

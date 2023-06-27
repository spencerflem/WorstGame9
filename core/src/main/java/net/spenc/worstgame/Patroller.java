package net.spenc.worstgame;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

public class Patroller extends Entity {
    public Vector2[] waypoints;
    public int currentWaypoint = 0;
    public float speed = 1f;
    Sound collideWithPlayerSound;

    public Patroller WithWaypoints(Vector2[] waypoints) {
        this.waypoints = waypoints;
        return this;
    }

    public Patroller WithSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public Patroller WithCollideWithPlayerSound(Sound sound) {
        this.collideWithPlayerSound = sound;
        return this;
    }

    @Override
    public void update(float delta) {
        Vector2 target = waypoints[currentWaypoint];
        Vector2 direction = target.cpy().sub(position);
        if (direction.len() < 0.1f) {
            currentWaypoint = (currentWaypoint + 1) % waypoints.length;
        } else {
            direction.nor();
            position.add(direction.scl(speed * delta));
        }
    }

    public void playSound() {
        if (collideWithPlayerSound != null) {
            collideWithPlayerSound.play();
        }
    }
}

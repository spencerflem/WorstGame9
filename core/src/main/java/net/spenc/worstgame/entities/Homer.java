package net.spenc.worstgame.entities;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import net.spenc.worstgame.ClientApp;
import net.spenc.worstgame.Entity;
import net.spenc.worstgame.HostApp;

import java.util.Random;

public class Homer extends Entity {
    private float dist = 0;
    private final float firstSpeed = 1.5f;
    private final float speed = 15f;
    private boolean landed = false;
    private HostApp host;
    private final Vector2 target = new Vector2();
    public boolean moveRight = true;

    public final float targetHeight = new Random().nextFloat();

    public Homer WithHost(HostApp host, ClientApp.ClientScreen screen) {
        this.host = host;
        host.addToOverlay(this, screen);
        return this;
    }

    @Override
    public void update(float delta) {
        if (!landed) {
            dist += firstSpeed * delta;
            moveRight = host.getMainWindowTargetMoveRight(this.spawnPosition);
            host.getMainWindowTarget(this.target, moveRight, targetHeight); // TODO: If target changes will be janky
            position.set(spawnPosition.cpy().interpolate(target, dist, Interpolation.linear));
            if (dist >= 1) {
                host.moveOverlayToMain(this);
                landed = true;
            }
        } else {
            if (moveRight) {
                position.x += speed * delta;
            } else {
                position.x -= speed * delta;
            }
            if (position.x > 1000 || position.x < -1000) {
                host.destroyEntityInMain(this);
            }
        }
        super.update(delta);
    }

}

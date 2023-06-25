package net.spenc.worstgame;

import com.badlogic.gdx.math.Vector2;

public class Spring extends Entity {
    private float springiness = 0.5f;
    private Vector2 impulseDir = new Vector2(0, 0);

    public Spring WithImpulseDir(Vector2 impulseDir) {
        this.impulseDir = impulseDir;
        return this;
    }

    public Spring WithSpringiness(float springiness) {
        this.springiness = springiness;
        return this;
    }

    @Override
    public void onCollisionEnter(Entity other) {
        if (other instanceof Player) {
            Player player = (Player) other;
            Vector2 impulse = impulseDir.cpy().scl(springiness);
            player.velocity.x = impulse.x;
            player.velocity.y = impulse.y;
        }
    }

}

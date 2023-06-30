package net.spenc.worstgame.entities;

import net.spenc.worstgame.Entity;

public class Homer extends Entity {
    private float ymin = -1000;
    private float ymax = 1000;

    private float speed = 333f;
    private boolean reverse = false;


    @Override
    public void update(float delta) {
        super.update(delta);
        if (reverse) {
            if (position.y > ymin) {
                position.y -= speed * delta;
            } else {
                reverse = false;
            }
        } else {
            if (position.y < ymax) {
                position.y += speed * delta;
            } else {
                reverse = true;
            }
        }
    }

}

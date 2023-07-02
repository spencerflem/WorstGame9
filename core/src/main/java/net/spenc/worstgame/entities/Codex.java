package net.spenc.worstgame.entities;

import com.badlogic.gdx.graphics.g2d.Batch;

import net.spenc.worstgame.Entity;
import net.spenc.worstgame.HostApp;

public class Codex extends Entity {
    private HostApp host;
    private boolean hidden = false;

    public Codex WithHost(HostApp host) {
        this.host = host;
        if (!host.hasRemainingCodexes()) {
            this.hidden = true;
        }
        return this;
    }

    @Override
    public void onCollisionEnter(Entity other) {
        if (!hidden) {
            if (other instanceof Player) {
                if (((Player) other).root == null) {
                    hidden = true;
                    host.openCodex();
                }
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        if (!hidden) {
            super.draw(batch);
        }
    }

    public void unhide() {
        this.hidden = !host.hasRemainingCodexes();
    }
}

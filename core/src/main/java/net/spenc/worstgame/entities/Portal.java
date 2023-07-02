package net.spenc.worstgame.entities;

import net.spenc.worstgame.Entity;
import net.spenc.worstgame.HostApp;

public class Portal extends Entity {
    private HostApp host;

    public Portal WithHost(HostApp host) {
        this.host = host;
        return this;
    }

    @Override
    public void onCollisionEnter(Entity other) {
        if (other instanceof Player) {
            if (((Player) other).root == null) {
                host.advanceLevel();
            }
        }
    }

}

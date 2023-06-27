package net.spenc.worstgame;

public class Portal extends Entity {
    private HostApp host;
    private String target;

    public Portal WithHost(HostApp host) {
        this.host = host;
        return this;
    }

    public Portal WithLevelTarget(String target) {
        this.target = target;
        return this;
    }

    @Override
    public void onCollisionEnter(Entity other) {
        if (other instanceof Player) {
            if (((Player) other).root == null) {
                host.setLevel(target);
            }
        }
    }

}

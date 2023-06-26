package net.spenc.worstgame;

public class Portal extends Entity {
    private String target;


    public Portal WithLevelTarget(String target) {
        this.target = target;
        return this;
    }

    @Override
    public void onCollisionEnter(Entity other) {
        if (other instanceof Player) {
            // TODO!!! new way of portaling
        }
    }

}

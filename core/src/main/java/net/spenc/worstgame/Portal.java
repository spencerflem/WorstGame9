package net.spenc.worstgame;

public class Portal extends Entity {
    private WorstGame game;
    private String target;

    public Portal WithGame(WorstGame game) {
        this.game = game;
        return this;
    }

    public Portal WithLevelTarget(String target) {
        this.target = target;
        return this;
    }

    @Override
    public void onCollisionEnter(Entity other) {
        if (other instanceof Player) {
            game.setScreen(new WorstScreen(game, target));
        }
    }

}

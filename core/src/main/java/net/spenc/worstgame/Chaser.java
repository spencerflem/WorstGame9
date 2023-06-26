package net.spenc.worstgame;

import com.badlogic.gdx.math.Vector2;

//An entity that chases down the player character with the intent to kill or maim
public class Chaser extends Entity {
    public float speed = 1f;

    public Player target;

    public Chaser WithSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public Chaser WithTarget(Player target) {
        this.target = target;
        return this;
    }

    @Override
    public void update(float delta) {
        Vector2 direction = new Vector2();
        //Causes the target to die if the Chaser lifts them high enough.
        if(target.position.y > 20f)
        {
            target.position.x = target.spawnPosition.x;
            target.position.y = target.spawnPosition.y;
        }
        else if(target.position.cpy().sub(position).len() < 0.6f)
        {
            //Causes the Chaser to grapple the target and then lift it into the sky.
            direction.set(0, 25);
            direction.nor();
            position.add(direction.scl(speed * delta));
            target.position.add(0,0.5f);
            target.velocity.set(0,0);
        }
        else{
            //Sets the direction of the Chaser so that it will move towards its target
            direction = this.target.position.cpy().sub(position);
            direction.nor();
            position.add(direction.scl(speed * delta));
        }

    }
}

package net.spenc.worstgame;

import com.badlogic.gdx.math.Vector2;

//An entity that chases down the player character with the intent to kill or maim
public class Chaser extends Entity {
    public float speed = 1f;

    public Entity target;

    public Chaser WithSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    //Constructs a Chaser with a built-in target and speed
    public Chaser(float speed, Entity target){
        this.speed = speed;
        this.target = target;
    }

    @Override
    public void update(float delta) {
        //Sets the direction of the Chaser so that it will move towards its target
        Vector2 direction = this.target.position.cpy().sub(position);
        //Causes the target to die if the Chaser lifts them high enough.
        if(target.position.y > 20f)
        {
            target.position.x = target.spawnPosition.x;
            target.position.y = target.spawnPosition.y;
        }
        else if(direction.len() < 0.3f)
        {
            //Causes the Chaser to grapple the target and then lift it into the sky.
            direction.set(0, 25);
            this.target.position.x = this.position.x - 0.5f;
            this.target.position.y = this.position.y - 0.5f;
        }
        //Moves the Chaser towards the set direction.
        direction.nor();
        position.add(direction.scl(speed * delta));
    }
}

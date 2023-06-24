package net.spenc.worst;

import com.badlogic.gdx.math.Vector2;

public class Player {
    public static float WIDTH; // will be set in create()
    public static float HEIGHT; // Will be set in create()
    public static float MAX_VELOCITY = 10f;
    public static float JUMP_VELOCITY = 40f;
    public static float DAMPING = 0.87f;

    enum State {
        Standing, Walking, Jumping
    }

    public final Vector2 position = new Vector2();
    public final Vector2 velocity = new Vector2();
    public State state = State.Walking;
    public float stateTime = 0;
    public boolean facesRight = true;
    public boolean grounded = false;
}

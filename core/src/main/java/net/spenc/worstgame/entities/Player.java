package net.spenc.worstgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import net.spenc.worstgame.Entity;
import net.spenc.worstgame.HostApp;

public class Player extends Entity {
    public Player root = null;
    private int cloneNumber = 0;
    private int level = 0;

    public static float MAX_VELOCITY = 10f;
    public static float JUMP_VELOCITY = 60f;
    public static float DAMPING = 0.87f;

    enum State {
        Standing, Walking, Jumping
    }

    public final Vector2 velocity = new Vector2();
    public State state = State.Walking;
    public float stateTime = 0;
    public boolean facesRight = true;
    public boolean grounded = false;

    private TextureRegion textureRegion;

    private TiledMap mapRef; // @TODO remove this
    private OrthographicCamera cameraRef; // @TODO remove this
    private HostApp hostRef; // @TODO remove this
    private Array<Entity> entitiesRef; // @TODO remove this

    // BEGIN @TODO: may be able to be handled in the world scope, fix later
    private final Pool<Rectangle> rectPool = new Pool<>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    private final Array<Rectangle> tiles = new Array<>();
    private float acc = 0;
    private float frameTime = 0.2f;
    private boolean frame1 = false;

    private static final float GRAVITY = -2.5f;

    public Player WithMapRef(TiledMap map) {
        this.mapRef = map;
        return this;
    }

    public Player WithCameraRef(OrthographicCamera camera) {
        this.cameraRef = camera;
        return this;
    }

    public Player WithHostRef(HostApp host) {
        this.hostRef = host;
        return this;
    }

    public Player WithEntitiesRef(Array<Entity> entities) {
        this.entitiesRef = entities;
        return this;
    }

    public Player WithRoot(Player root, int cloneNumber) {
        this.root = root;
        this.cloneNumber = cloneNumber;
        return this;
    }

    public Player WithLevel(int level) {
        this.level = level;
        return this;
    }

    @Override
    public Entity WithTexture(Texture texture) {
        this.textureRegion = new TextureRegion(texture, 50, 50);
        return super.WithTexture(texture);
    }

    @Override
    public Entity WithSize(float width, float height) {
        this.width = 47 / 16f;
        this.height = 48 / 16f;
        return this;
    }

    @Override
    public void draw(Batch batch) {
        // create a copy fot texture region
        TextureRegion tmpTextureRegion = new TextureRegion(textureRegion);

        if (state == State.Standing) {
            tmpTextureRegion = new TextureRegion(this.textureRegion, 0, 0, 50, 50);
        }
        if (state == State.Walking) {
            tmpTextureRegion = new TextureRegion(this.textureRegion, frame1 ? 100 : 50, 0, 50, 50);
        }
        if (state == State.Jumping) {
            tmpTextureRegion = new TextureRegion(this.textureRegion, 150, 0, 50, 50);
        }
        batch.draw(tmpTextureRegion, position.x, position.y - 0.5f, width, height);
    }

    // END @TODO

    @Override
    public void update(float deltaTime) {
        Controller controller = Controllers.getCurrent();

        acc += deltaTime;
        while (acc > frameTime) {
            frame1 = !frame1;
            acc -= frameTime;
        }

        if (deltaTime == 0)
            return;

        if (deltaTime > 0.1f)
            deltaTime = 0.1f;

        this.stateTime += deltaTime;

        boolean jump = hostRef.focusedInput().isKeyPressed(Input.Keys.SPACE) || isTouched(0.5f, 1)
                || (controller != null && controller.getButton(controller.getMapping().buttonA))
                || (controller != null && controller.getButton(controller.getMapping().buttonDpadUp));

        boolean right = hostRef.focusedInput().isKeyPressed(Input.Keys.RIGHT)
                || hostRef.focusedInput().isKeyPressed(Input.Keys.D) || isTouched(0.25f, 0.5f)
                || (controller != null && controller.getAxis(controller.getMapping().axisLeftX) > 0.1)
                || (controller != null && controller.getButton(controller.getMapping().buttonDpadRight));

        boolean left = hostRef.focusedInput().isKeyPressed(Input.Keys.LEFT)
                || hostRef.focusedInput().isKeyPressed(Input.Keys.A) || isTouched(0, 0.25f)
                || (controller != null && controller.getAxis(controller.getMapping().axisLeftX) < -0.1)
                || (controller != null && controller.getButton(controller.getMapping().buttonDpadLeft));

        // check input and apply to velocity & state
        if (jump && this.grounded) {
            this.velocity.y += Player.JUMP_VELOCITY;
            this.grounded = false;
        }

        if (left) {
            this.velocity.x = -Player.MAX_VELOCITY;
            this.facesRight = false;
        }

        if (right) {
            this.velocity.x = Player.MAX_VELOCITY;
            this.facesRight = true;
        }

        // apply gravity if we are falling
        this.velocity.add(0, GRAVITY);

        // clamp the velocity to the maximum, x-axis only
        this.velocity.x = MathUtils.clamp(this.velocity.x, -Player.MAX_VELOCITY, Player.MAX_VELOCITY);

        // multiply by delta time so we know how far we go
        // in this frame
        this.velocity.scl(deltaTime);

        // perform collision detection & response, on each axis, separately
        // if the koala is moving right, check the tiles to the right of it's
        // right bounding box edge, otherwise check the ones to the left
        Rectangle playerRect = rectPool.obtain();
        playerRect.set(this.position.x, this.position.y, this.width, this.height);
        int startX, startY, endX, endY;
        if (this.velocity.x > 0) {
            startX = endX = (int) (this.position.x + this.width + this.velocity.x);
        } else {
            startX = endX = (int) (this.position.x + this.velocity.x);
        }
        startY = (int) (this.position.y);
        endY = (int) (this.position.y + this.height);
        getTiles(startX, startY, endX, endY, tiles);
        playerRect.x += this.velocity.x;
        for (Rectangle tile : tiles) {
            if (playerRect.overlaps(tile)) {
                this.velocity.x = 0;
                break;
            }
        }
        playerRect.x = this.position.x;

        // if the koala is moving upwards, check the tiles to the top of its
        // top bounding box edge, otherwise check the ones to the bottom
        if (this.velocity.y > 0) {
            startY = endY = (int) (this.position.y + this.height + this.velocity.y);
        } else {
            startY = endY = (int) (this.position.y + this.velocity.y);
        }
        startX = (int) (this.position.x);
        endX = (int) (this.position.x + this.width);
        getTiles(startX, startY, endX, endY, tiles);
        playerRect.y += this.velocity.y;
        for (Rectangle tile : tiles) {
            if (playerRect.overlaps(tile)) {
                // we actually reset the koala y-position here
                // so it is just below/above the tile we collided with
                // this removes bouncing :)
                if (this.velocity.y > 0 && this.level == 3) {
                    this.position.y = tile.y - this.height;
                    // we hit a block jumping upwards, let's destroy it!
                    TiledMapTileLayer layer = (TiledMapTileLayer) mapRef.getLayers().get("walls");
                    layer.setCell((int) tile.x, (int) tile.y, null);
                } else {
                    this.position.y = tile.y + tile.height;
                    // if we hit the ground, mark us as grounded so we can jump
                    this.grounded = true;
                }
                this.velocity.y = 0;
                break;
            }
        }
        rectPool.free(playerRect);

        // unscale the velocity by the inverse delta time and set
        // the latest position
        this.position.add(this.velocity);
        this.velocity.scl(1 / deltaTime);

        // Apply damping to the velocity on the x-axis so we don't
        // walk infinitely once a key was pressed
        this.velocity.x *= Player.DAMPING;

        // if pos.y is below -5 respawn
        if (this.position.y < -5) {
            respawn();
        }

        if (cameraRef != null) {
            cameraRef.position.x = this.position.x;
        }

        // set the state based on the current velocity
        this.state = Math.abs(velocity.x) <= 0.1 ? State.Standing : State.Walking;
        this.state = Math.abs(velocity.y) >= 0.1 || !this.grounded ? State.Jumping : this.state;
    }

    // Controller controller = Controllers.getCurrent();

    private boolean isTouched(float startX, float endX) {
        // Check for touch inputs between startX and endX
        // startX/endX are given between 0 (left edge of the screen) and 1 (right edge
        // of the screen)
        for (int i = 0; i < 2; i++) {
            float x = hostRef.focusedInput().getX(i) / (float) Gdx.graphics.getWidth();
            if (hostRef.focusedInput().isTouched(i) && (x >= startX && x <= endX)) {
                return true;
            }
        }
        return false;
    }

    private void getTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
        TiledMapTileLayer layer = (TiledMapTileLayer) mapRef.getLayers().get("walls");
        rectPool.freeAll(tiles);
        tiles.clear();
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
                }
            }
        }
    }

    public void respawn() {
        if (System.getenv("INVINCIBLE") == null || this.root != null) {
            if (this.root != null) {
                this.position.x = root.position.x + cloneNumber;
                this.position.y = this.spawnPosition.y;
                return;
            }
            this.position.x = this.spawnPosition.x;
            this.position.y = this.spawnPosition.y;
            for (int i = 0; i < entitiesRef.size; i++) {
                Entity entity = entitiesRef.get(i);
                if (entity instanceof Codex) {
                    ((Codex) entity).refresh();
                }
            }
        } else {
            this.position.y = this.spawnPosition.y;
        }
    }

    @Override
    public void onCollisionEnter(Entity other) {
        if (this.root == null && !(other instanceof Clamper)) {
            other.playSound();
        }
        if (other instanceof Patroller) {
            respawn();
        }
        if (other instanceof Homer) {
            respawn();
        }
        if (other instanceof Player) {
            Player otherPlayer = (Player) other;
            if (otherPlayer.root == null) {
                respawn();
            }
        }
    }

}

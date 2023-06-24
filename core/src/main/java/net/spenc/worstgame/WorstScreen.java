package net.spenc.worstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class WorstScreen implements Screen {
    private Texture img;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Player player;

    private final Pool<Rectangle> rectPool = new Pool<>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    private final Array<Rectangle> tiles = new Array<>();

    private static final float GRAVITY = -2.5f;
    private static final float TILE2PIXEL = 16f;
    private static final float PIXEL2TILE = 1 / TILE2PIXEL;

    @Override
    public void show() {
        img = new Texture("tentacle_guy.png");
        map = new TmxMapLoader().load("level1.tmx");

        camera = new OrthographicCamera();
        camera.position.y = 10;
        renderer = new OrthogonalTiledMapRenderer(map, PIXEL2TILE);
        viewport = new FitViewport(30, 20, camera);

        player = new Player();
        player.width = img.getWidth() * PIXEL2TILE;
        player.height = img.getHeight() * PIXEL2TILE;
        player.texture = img;
        player.position.set(20, 20);

        Music music = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        music.setLooping(true);
        music.setVolume(.02f);
        if (System.getenv("DEV") == null) { // example: DEV=1 sh gradlew run
            music.play();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // update positions
        updatePlayer(delta);

        // move the camera
        camera.position.x = player.position.x;
        // TODO: I kinda like the celeste aesthetic of keeping the level all shown on
        // one screen
        // when we make our own levels, I propose we do that, and remove this line
        camera.update();

        // draw the map
        renderer.setView(camera);
        renderer.render();

        // draw the player
        Batch batch = renderer.getBatch();
        batch.begin();
        player.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        img.dispose();
        map.dispose();
        renderer.dispose();
        // TODO: I think I'm missing something here
    }

    private void updatePlayer(float deltaTime) {
        if (deltaTime == 0)
            return;

        if (deltaTime > 0.1f)
            deltaTime = 0.1f;

        player.stateTime += deltaTime;

        // check input and apply to velocity & state
        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE) || isTouched(0.5f, 1)) && player.grounded) {
            player.velocity.y += Player.JUMP_VELOCITY;
            player.state = Player.State.Jumping;
            player.grounded = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) || isTouched(0, 0.25f)) {
            player.velocity.x = -Player.MAX_VELOCITY;
            if (player.grounded)
                player.state = Player.State.Walking;
            player.facesRight = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) || isTouched(0.25f, 0.5f)) {
            player.velocity.x = Player.MAX_VELOCITY;
            if (player.grounded)
                player.state = Player.State.Walking;
            player.facesRight = true;
        }

        // apply gravity if we are falling
        player.velocity.add(0, GRAVITY);

        // clamp the velocity to the maximum, x-axis only
        player.velocity.x = MathUtils.clamp(player.velocity.x, -Player.MAX_VELOCITY, Player.MAX_VELOCITY);

        // If the velocity is < 1, set it to 0 and set state to Standing
        if (Math.abs(player.velocity.x) < 1) {
            player.velocity.x = 0;
            if (player.grounded)
                player.state = Player.State.Standing;
        }

        // multiply by delta time so we know how far we go
        // in this frame
        player.velocity.scl(deltaTime);

        // perform collision detection & response, on each axis, separately
        // if the koala is moving right, check the tiles to the right of it's
        // right bounding box edge, otherwise check the ones to the left
        Rectangle playerRect = rectPool.obtain();
        playerRect.set(player.position.x, player.position.y, player.width, player.height);
        int startX, startY, endX, endY;
        if (player.velocity.x > 0) {
            startX = endX = (int) (player.position.x + player.width + player.velocity.x);
        } else {
            startX = endX = (int) (player.position.x + player.velocity.x);
        }
        startY = (int) (player.position.y);
        endY = (int) (player.position.y + player.height);
        getTiles(startX, startY, endX, endY, tiles);
        playerRect.x += player.velocity.x;
        for (Rectangle tile : tiles) {
            if (playerRect.overlaps(tile)) {
                player.velocity.x = 0;
                break;
            }
        }
        playerRect.x = player.position.x;

        // if the koala is moving upwards, check the tiles to the top of its
        // top bounding box edge, otherwise check the ones to the bottom
        if (player.velocity.y > 0) {
            startY = endY = (int) (player.position.y + player.height + player.velocity.y);
        } else {
            startY = endY = (int) (player.position.y + player.velocity.y);
        }
        startX = (int) (player.position.x);
        endX = (int) (player.position.x + player.width);
        getTiles(startX, startY, endX, endY, tiles);
        playerRect.y += player.velocity.y;
        for (Rectangle tile : tiles) {
            if (playerRect.overlaps(tile)) {
                // we actually reset the koala y-position here
                // so it is just below/above the tile we collided with
                // this removes bouncing :)
                if (player.velocity.y > 0) {
                    player.position.y = tile.y - player.height;
                    // we hit a block jumping upwards, let's destroy it!
                    TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("walls");
                    layer.setCell((int) tile.x, (int) tile.y, null);
                } else {
                    player.position.y = tile.y + tile.height;
                    // if we hit the ground, mark us as grounded so we can jump
                    player.grounded = true;
                }
                player.velocity.y = 0;
                break;
            }
        }
        rectPool.free(playerRect);

        // unscale the velocity by the inverse delta time and set
        // the latest position
        player.position.add(player.velocity);
        player.velocity.scl(1 / deltaTime);

        // Apply damping to the velocity on the x-axis so we don't
        // walk infinitely once a key was pressed
        player.velocity.x *= Player.DAMPING;
    }

    private boolean isTouched(float startX, float endX) {
        // Check for touch inputs between startX and endX
        // startX/endX are given between 0 (left edge of the screen) and 1 (right edge
        // of the screen)
        for (int i = 0; i < 2; i++) {
            float x = Gdx.input.getX(i) / (float) Gdx.graphics.getWidth();
            if (Gdx.input.isTouched(i) && (x >= startX && x <= endX)) {
                return true;
            }
        }
        return false;
    }

    private void getTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("walls");
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
}

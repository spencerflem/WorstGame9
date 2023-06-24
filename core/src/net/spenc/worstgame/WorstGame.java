package net.spenc.worstgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WorstGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img;
    private Music music;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Player player;
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject () {
            return new Rectangle();
        }
    };
    private Array<Rectangle> tiles = new Array<Rectangle>();

    private static final float GRAVITY = -2.5f;
    private static final float TILE2PIXEL = 16f;
    private static final float PIXEL2TILE = 1 / TILE2PIXEL;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("tentacle_guy.png");
        Player.WIDTH = img.getWidth() * PIXEL2TILE;
        Player.HEIGHT = img.getHeight() * PIXEL2TILE;
		camera = new OrthographicCamera();
		viewport = new FitViewport(30, 20, camera);
        map = new TmxMapLoader().load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, PIXEL2TILE);
        player = new Player();
        player.position.set(20, 20);
		music = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
		music.setLooping(true);
		music.play();
	}

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        // update positions
        float deltaTime = Gdx.graphics.getDeltaTime();
        Vector2 mousePos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())).sub(Math.round(Player.WIDTH / 2.0), Math.round(Player.HEIGHT / 2.0));
        player.position.set(mousePos);

        // move the camera
        //camera.position.sub(player.position.x * 0.01f, player.position.y * 0.01f, 0);
        camera.update();

        // draw the map
        renderer.setView(camera);
        renderer.render();

        // draw the player
        Batch batch = renderer.getBatch();
        batch.begin();
        batch.draw(img, player.position.x, player.position.y, Player.WIDTH, Player.HEIGHT);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
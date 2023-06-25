package net.spenc.worstgame;

import java.util.ArrayList;
// import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class WorstScreen extends ScreenAdapter {
    private final WorstGame game;
    private Texture playerImg;
    private Texture biblImg;
    private Texture doNUTImg;
    private Viewport viewport;
    private Music music;
    public static OrthographicCamera MainCamera; // this is dumb, fix later
    public static TiledMap map; // this is dumb, fix later
    private OrthogonalTiledMapRenderer renderer;
    private ArrayList<Entity> entities = new ArrayList<Entity>(); // this isn't cache friendly, but if it's good enough
                                                                  // for unity it is good enough for us

    private static final float TILE2PIXEL = 16f;
    private static final float PIXEL2TILE = 1 / TILE2PIXEL;

    // private Stack<AutoCloseable> disposables = new Stack<AutoCloseable>();

    public WorstScreen(WorstGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        playerImg = new Texture("tentacle_guy.png");
        biblImg = new Texture("bibl.png");
        doNUTImg = new Texture("doNUT.png");
        map = new TmxMapLoader().load("level1.tmx");

        MainCamera = new OrthographicCamera();
        MainCamera.position.y = 10;
        renderer = new OrthogonalTiledMapRenderer(map, PIXEL2TILE);
        viewport = new FitViewport(30, 20, MainCamera);

        Player player = (Player) new Player()
                .WithPosition(new Vector2(20, 20))
                .WithTexture(playerImg)
                .WithSize(playerImg.getWidth() * PIXEL2TILE, playerImg.getHeight() * PIXEL2TILE)
                .WithLayer(1);

        entities.add(player);

        Patroller bibl = (Patroller) new Patroller()
                .WithSpeed(5)
                .WithWaypoints(new Vector2[] {
                        new Vector2(30, 2),
                        new Vector2(36, 2),
                })
                .WithPosition(new Vector2(32, 2))
                .WithTexture(biblImg)
                .WithSize(biblImg.getWidth() * PIXEL2TILE, biblImg.getHeight() * PIXEL2TILE);

        entities.add(bibl);

        Patroller doNUT = (Patroller) new Patroller()
                .WithSpeed(5)
                .WithWaypoints(new Vector2[] {
                        new Vector2(46, 5),
                        new Vector2(46, 12),
                })
                .WithPosition(new Vector2(46, 4))
                .WithTexture(doNUTImg)
                .WithSize(doNUTImg.getWidth() * PIXEL2TILE, doNUTImg.getHeight() * PIXEL2TILE);

        entities.add(doNUT);

        // after creating all entities, sort them by layer for rendering
        entities.sort((a, b) -> a.layer - b.layer);

        music = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        music.setLooping(true);
        music.setVolume(.02f);
        if (System.getenv("DEV") == null) { // example: DEV=1 sh gradlew run
            music.play();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);

        // entity logic
        for (Entity entity : entities) {
            entity.update(delta);
        }

        // TODO: I kinda like the celeste aesthetic of keeping the level all shown on
        // one screen
        // when we make our own levels, I propose we do that, and remove this line
        MainCamera.update();

        // draw the map
        renderer.setView(MainCamera);
        renderer.render();

        // draw the player
        Batch batch = renderer.getBatch();
        batch.begin();
        for (Entity entity : entities) {
            entity.draw(batch);
        }
        batch.end();

        // if 'P' just pressed - make a pop-up
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            game.popupWindowCreator.newPopup();
        }

        // if 'O' just pressed - make an overlay pop-up
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            game.popupWindowCreator.newPopup(true);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        playerImg.dispose();
        map.dispose();
        renderer.dispose();
        music.stop();
        music.dispose();
        // TODO: I think I'm missing something here
    }
}

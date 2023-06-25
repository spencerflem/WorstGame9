package net.spenc.worstgame;

import java.util.ArrayList;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
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
    private final float TILE2PIXEL = 16f;
    private final float PIXEL2TILE = 1f / TILE2PIXEL;

    private AssetManager manager = new AssetManager();
    private PrefabLoader prefabLoader;

    public static OrthographicCamera MainCamera;
    private Viewport viewport;
    private OrthogonalTiledMapRenderer renderer;
    private ArrayList<Entity> entities = new ArrayList<Entity>(); // this isn't cache friendly, but if it's good enough
    // for unity it is good enough for us

    private static final float TIMESTEP = 0.01f;
    private static final float MAX_ACCUMULATOR = 0.1f;
    private double accumulator = 0.0;

    public WorstScreen(WorstGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        if (game.type == WorstGame.GameType.HOST) {
            game.popupWindowCreator.newPopup(WorstGame.GameType.MAIN);
            return;
        }

        // load all of the assets
        manager.load(Filenames.PLAYER.getFilename(), Texture.class);
        manager.load(Filenames.BIBL.getFilename(), Texture.class);
        manager.load(Filenames.DONUT.getFilename(), Texture.class);
        manager.load(Filenames.SPIKE.getFilename(), Texture.class);
        manager.load(Filenames.SPRING.getFilename(), Texture.class);
        manager.load(Filenames.MUSIC.getFilename(), Music.class);

        manager.setLoader(TiledMap.class, ".tmx", new TmxMapLoader());

        manager.load(Filenames.MAP.getFilename(), TiledMap.class);
        manager.finishLoading(); // can make async later

        prefabLoader = new PrefabLoader(manager, this.PIXEL2TILE);

        TiledMap map = manager.get(Filenames.MAP.getFilename(), TiledMap.class);

        renderer = new OrthogonalTiledMapRenderer(map, PIXEL2TILE);

        MainCamera = new OrthographicCamera();
        MainCamera.position.y = 10;

        viewport = new FitViewport(30, 20, MainCamera);

        entities.add((prefabLoader.NewPlayerPrefab()
                .WithMapRef(map)));

        entities.add(prefabLoader.NewBiblPrefab());

        entities.add(prefabLoader.NewDoNUTPrefab());

        for (int i = 0; i < 3; i++) {
            entities.add(
                    prefabLoader.NewSpikePrefab().WithSpawnPosition(new Vector2(51 + i, 10)));
        }

        entities.add(prefabLoader.NewSpringPrefab());

        // after creating all entities, sort them by layer for rendering
        entities.sort(Comparator.comparingInt(a -> a.layer));

        Music music = manager.get(Filenames.MUSIC.getFilename(), Music.class);
        music.setLooping(true);
        music.setVolume(.02f);
        if (System.getenv("DEV") == null) { // example: DEV=1 sh gradlew run
            if (game.type == WorstGame.GameType.MAIN) {
                music.play();
            }
        }
    }

    @Override
    public void render(float delta) {
        if (game.type == WorstGame.GameType.HOST) {
            return;
        }

        if (game.type == WorstGame.GameType.OVERLAY) {
            ScreenUtils.clear(0, 0, 0, 0);
        } else {
            ScreenUtils.clear(0.7f, 0.7f, 1, 1);
        }

        accumulator = Math.min(accumulator + delta, MAX_ACCUMULATOR);
        while (accumulator >= TIMESTEP) {
            accumulator -= TIMESTEP;
            // entity logic
            for (Entity entity : entities) {
                entity.update(TIMESTEP);
                entity.updateCollsions(entities);
            }
        }

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
            game.popupWindowCreator.newPopup(WorstGame.GameType.POPUP);
        }

        // if 'O' just pressed - make an overlay pop-up
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            game.popupWindowCreator.newPopup(WorstGame.GameType.OVERLAY, true);
        }
    }

    @Override
    public void resize(int width, int height) {
        if (game.type == WorstGame.GameType.HOST) {
            return;
        }

        render(Gdx.graphics.getDeltaTime());
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        // log the number of items being disposed
        manager.dispose();
        if (game.type == WorstGame.GameType.MAIN) {
            Gdx.app.exit();
        }
    }
}

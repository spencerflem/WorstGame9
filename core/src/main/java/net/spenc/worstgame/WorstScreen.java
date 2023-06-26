package net.spenc.worstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Comparator;

public class WorstScreen extends ScreenAdapter {
    private static final float TIMESTEP = 0.01f;
    private static final float MAX_ACCUMULATOR = 0.1f;

    private final WorstGame game;
    private final TiledMap map;
    private final float tiles2pixels = 16f;
    private final float pixels2tiles = 1 / tiles2pixels;
    private final OrthogonalTiledMapRenderer renderer;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private double accumulator = 0.0;
    private final ArrayList<Entity> entities = new ArrayList<>();

    public WorstScreen(WorstGame game) {
        this.game = game;
        this.map = game.assets.get("maps/" + game.level + ".tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map, pixels2tiles);
        this.camera = new OrthographicCamera();
        this.camera.position.y = 10;
        this.viewport = new FitViewport(30, 20, camera);
        createEntities();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.7f, 0.7f, 1, 1);

        // update entity positions & animations
        accumulator = Math.min(accumulator + delta, MAX_ACCUMULATOR);
        while (accumulator >= TIMESTEP) {
            accumulator -= TIMESTEP;
            for (Entity entity : entities) {
                entity.update(TIMESTEP);
                entity.updateCollisions(entities);
            }
        }

        camera.update();

        // draw the map
        renderer.setView(camera);
        renderer.render();

        // draw the entities
        Batch batch = renderer.getBatch();
        batch.begin();
        for (Entity entity : entities) {
            entity.draw(batch);
        }
        batch.end();

        // if 'P' just pressed - make a pop-up
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            game.popupWindowCreator.newPopup(game.assets, game.level);
        }

        // if 'O' just pressed - make an overlay pop-up
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            game.popupWindowCreator.newOverlay(game.assets);
        }
    }

    @Override
    public void resize(int width, int height) {
        render(Gdx.graphics.getDeltaTime());
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        for (Entity entity : entities) {
            entity.dispose();
        }
        if (game.main) {
            Gdx.app.exit();
        }
    }

    private void createEntities() {
        Texture playerTex = game.assets.get("textures/tentacle_guy.png");
        Player player = (Player) new Player()
            .WithSpawnPosition(new Vector2(20, 20))
            .WithTexture(playerTex)
            .WithSize(playerTex.getWidth() * pixels2tiles, playerTex.getHeight() * pixels2tiles)
            .WithLayer(1);
        entities.add(player);

        Texture biblTex = game.assets.get("textures/bibl.png");
        Patroller bibl = (Patroller) new Patroller()
            .WithSpeed(5)
            .WithWaypoints(new Vector2[] {
                new Vector2(30, 2),
                new Vector2(36, 2),
            })
            .WithSpawnPosition(new Vector2(32, 2))
            .WithTexture(biblTex)
            .WithSize(biblTex.getWidth() * pixels2tiles, biblTex.getHeight() * pixels2tiles);

        entities.add(bibl);

        Texture doNUTTex = game.assets.get("textures/doNUT.png");
        Patroller doNUT = (Patroller) new Patroller()
            .WithSpeed(5)
            .WithWaypoints(new Vector2[] {
                new Vector2(46, 5),
                new Vector2(46, 12),
            })
            .WithSpawnPosition(new Vector2(46, 4))
            .WithTexture(doNUTTex)
            .WithSize(doNUTTex.getWidth() * pixels2tiles, doNUTTex.getHeight() * pixels2tiles);

        entities.add(doNUT);

        Texture spikeTex = game.assets.get("textures/spike.png");
        for (int i = 0; i < 3; i++) {
            Entity spike = new Entity()
                .WithSpawnPosition(new Vector2(51 + i, 10))
                .WithSize(1, 1)
                .WithTexture(spikeTex);

            entities.add(spike);
        }

        Texture springTex = game.assets.get("textures/spring.png");
        Spring spring = (Spring) new Spring()
            .WithSpringiness(100)
            .WithImpulseDir(Vector2.Y)
            .WithSpawnPosition(new Vector2(10, 2))
            .WithSize(1, 1)
            .WithTexture(springTex);
        entities.add(spring);

        // after creating all entities, sort them by layer for rendering
        entities.sort(Comparator.comparingInt(a -> a.layer));
    }
}

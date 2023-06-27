package net.spenc.worstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Comparator;

public class WorstScreen extends ScreenAdapter {
    private final Music music;
    private final Array<ManagedWindow> windows = new Array<>();
    private static final float TIMESTEP = 0.01f;
    private static final float MAX_ACCUMULATOR = 0.1f;
    private double accumulator = 0.0;
    private final WorstGame game;
    private final TiledMap map;
    private final float tiles2pixels = 16f;
    private final float pixels2tiles = 1 / tiles2pixels;
    private final OrthogonalTiledMapRenderer renderer;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final ArrayList<Entity> entities = new ArrayList<>();
    private final PrefabLoader prefabLoader;
    private final InputCollector input = new InputCollector();

    public WorstScreen(WorstGame game) {
        this.game = game;
        this.map = game.assets.get("maps/level1.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map, pixels2tiles, game.batch);
        WindowListener app = newWindowApp(true);
        game.windowManager.newMain(app, window -> {
            windows.add(window);
            window.setPositionListener((x, y) -> Gdx.app.log("loc", "x: " + x + ", y: " + y));
        });
        music = Gdx.audio.newMusic(Gdx.files.internal(Filenames.MUSIC.getFilename()));
        music.setLooping(true);
        music.setVolume(.02f);
        if (System.getenv("DEV") == null) { // example: DEV=1 sh gradlew run
            music.play();
        }
        this.camera = new OrthographicCamera();
        this.camera.position.y = 10;
        this.viewport = new FitViewport(30, 20, camera);
        this.prefabLoader = new PrefabLoader(game.assets, pixels2tiles);
        createEntities();
    }

    @Override
    public void render(float delta) {
        accumulator = Math.min(accumulator + delta, MAX_ACCUMULATOR);
        while (accumulator >= TIMESTEP) {
            accumulator -= TIMESTEP;
            for (Entity entity : entities) {
                entity.update(TIMESTEP);
                entity.updateCollisions(entities);
            }
        }
    }

    private WindowListener newWindowApp(boolean main) {
        return new WindowApplication() {
            @Override
            public void render() {
                if (getWindow() != null && getWindow().isFocused()) {
                    collectInput();
                }
                renderClient();
            }

            @Override
            public void dispose() {
                if (main) {
                    Gdx.app.exit();
                }
            }
        };
    }

    private void collectInput() {
        Controller controller = Controllers.getCurrent();

        input.jump = Gdx.input.isKeyPressed(Input.Keys.SPACE) || isTouched(0.5f, 1)
                || (controller != null && controller.getAxis(controller.getMapping().axisLeftY) > 0);

        input.right = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)
                || isTouched(0.25f, 0.5f)
                || (controller != null && controller.getAxis(controller.getMapping().axisLeftX) > 0);

        input.left = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)
                || isTouched(0, 0.25f)
                || (controller != null && controller.getAxis(controller.getMapping().axisLeftX) > 0);
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

    private void renderClient() {
        ScreenUtils.clear(0.7f, 0.7f, 1, 1);

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
            newPopup();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        music.stop();
        music.dispose();
        renderer.dispose();
        for (Entity entity : entities) {
            entity.dispose();
        }
    }

    private void newPopup() {
        WindowListener app = newWindowApp(false);
        game.windowManager.newPopup(app, window -> {
            app.setWindow(window);
            windows.add(window);
        });
    }

    private void createEntities() {
        // loads entities from map @TODO replace all the other loaded prefabs
        map.getLayers().forEach(layer -> {
            // log the layer name
            Gdx.app.log("Layer Name", layer.getName());
            layer.getObjects().forEach(obj -> {
                // log the type
                if (!obj.getProperties().containsKey("type")) {
                    return;
                }

                // get the position
                float x = obj.getProperties().get("x", Float.class) * pixels2tiles;
                float y = obj.getProperties().get("y", Float.class) * pixels2tiles;

                String type = obj.getProperties().get("type").toString();

                Gdx.app.log("Parsed Type", type);

                if (type.toLowerCase().equals("player")) {
                    Gdx.app.log("Player", "Found a player");
                    entities.add(prefabLoader.NewPlayerPrefab().WithMapRef(map).WithCameraRef(camera)
                            .WithInputRef(input).WithSpawnPosition(new Vector2(x, y)));
                }

                if (type.toLowerCase().equals("patroller")) {
                    Gdx.app.log("Patroller", "Found a patroller");

                    int pathLength = obj.getProperties().get("PathLength", Integer.class);
                    Vector2[] path = new Vector2[pathLength];
                    for (int i = 0; i < pathLength; i++) {
                        String pathPointStr = obj.getProperties().get("Path" + i, String.class); // this is "x,y"
                        String[] pathPointStrSplit = pathPointStr.split(",");
                        float pathX = Float.parseFloat(pathPointStrSplit[0]) * pixels2tiles;
                        // remember for y that the map is upside down
                        float pathY = map.getProperties().get("height", Integer.class)
                                - Float.parseFloat(pathPointStrSplit[1]) * pixels2tiles;
                        // log the xy
                        Gdx.app.log("Path Point", pathX + "," + pathY);
                        path[i] = new Vector2(pathX, pathY);
                    }

                    Patroller prefab = new Patroller();
                    // parse the TexturerEnum
                    String textureStr = obj.getProperties().get("TextureEnum", String.class);
                    if (textureStr.equals("BIBL")) {
                        prefab = prefabLoader.NewBiblPrefab();
                    } else if (textureStr.equals("DONUT")) {
                        prefab = prefabLoader.NewDoNUTPrefab();
                    }

                    entities.add(prefab.WithWaypoints(path).WithSpawnPosition(new Vector2(x, y)));

                }

                if (type.toLowerCase().equals("spring")) {
                    // log found a spring
                    Gdx.app.log("Spring", "Found a spring");

                    // parse the springiness
                    float springiness = obj.getProperties().get("springiness", Float.class);
                    // parse the impulseDir this is a string "x,y"
                    String impulseDirStr = obj.getProperties().get("impulseDir", String.class);
                    String[] impulseDirStrSplit = impulseDirStr.split(",");
                    float impulseDirX = Float.parseFloat(impulseDirStrSplit[0]);
                    float impulseDirY = Float.parseFloat(impulseDirStrSplit[1]);
                    Vector2 impulseDir = new Vector2(impulseDirX, impulseDirY);

                    entities.add(prefabLoader.NewSpringPrefab().WithSpringiness(springiness).WithImpulseDir(impulseDir)
                            .WithSpawnPosition(new Vector2(x, y)));
                }

                if (type.toLowerCase().equals("entity")) {
                    // parse the TexturerEnum
                    String textureStr = obj.getProperties().get("TextureEnum", String.class);
                    if (textureStr.equals("SPIKE")) {
                        Entity prefab = prefabLoader.NewSpikePrefab().WithSpawnPosition(new Vector2(x, y));
                        entities.add(prefab);
                    }
                }

                if (type.toLowerCase().equals("portal")) {
                    // parse the level target
                    String target = obj.getProperties().get("target", String.class);
                    entities.add(prefabLoader.NewPortalPrefab().WithLevelTarget(target)
                            .WithSpawnPosition(new Vector2(x, y)));
                }

                if (type.toLowerCase().equals("chaser")) {
                    // parse the TexturerEnum
                    String textureStr = obj.getProperties().get("TextureEnum", String.class);
                    if (textureStr.equals("BfChicken")) {
                        Entity prefab = prefabLoader.NewBuffChickPrefab().WithSpawnPosition(new Vector2(x, y));
                        entities.add(prefab);
                    }
                }
            });
        });

        // consider giving entities access to the world,
        // since this is the only case where they need it (for initialization)
        // we can just set references in here
        Player playerRef = null; // we use a search to find the player
        ArrayList<Chaser> chaserRefs = new ArrayList<Chaser>(); // we also use a search to find the chasers

        // get the entity that is the player
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            if (entity instanceof Player) {
                playerRef = (Player) entity;
            } else if (entity instanceof Chaser) {
                chaserRefs.add((Chaser) entity);
            }
        }
        // set every chaserRefs target to the player
        for (Chaser chaser : chaserRefs) {
            chaser = chaser.WithTarget(playerRef);
        }

        // after creating all entities, sort them by layer for rendering
        entities.sort(Comparator.comparingInt(a -> a.layer));
    }
}

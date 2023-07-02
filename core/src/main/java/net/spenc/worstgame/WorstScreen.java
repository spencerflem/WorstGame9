package net.spenc.worstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.spenc.worstgame.entities.Chaser;
import net.spenc.worstgame.entities.Patroller;
import net.spenc.worstgame.entities.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class WorstScreen extends ScreenAdapter implements ClientApp.ClientScreen {
    private final HostApp host;
    private final Music music;
    private final TiledMap map;
    private final float tiles2pixels = 16f;
    private final float pixels2tiles = 1 / tiles2pixels;
    private final OrthogonalTiledMapRenderer renderer;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Array<Entity> entities = new Array<>();
    private final PrefabLoader prefabLoader;
    private final Random random = new Random();
    private int level = 0;
    private int minAdTimer = 0;
    private int maxAdTimer = 0;

    private float popupTime = 15;

    public WorstScreen(HostApp host, String level) {
        this.host = host;
        this.map = host.assets.get("maps/" + level + ".tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map, pixels2tiles, host.batch);
        music = host.assets.get(Filenames.BACKGROUND_MUSIC.getFilename());
        music.setLooping(true);
        music.setVolume(.02f);
        this.camera = new OrthographicCamera();
        this.camera.position.y = 10;
        this.viewport = new FitViewport(26, 20, camera);
        this.prefabLoader = new PrefabLoader(host.assets, pixels2tiles);
        createEntities();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.7f, 0.7f, 1, 1);
        if (System.getenv("DEV") == null) { // example: DEV=1 sh gradlew run
            popupTime -= delta;
            if (popupTime < 0) {
                popupTime = random.nextInt(minAdTimer, maxAdTimer);
                host.newPopup(PopupScreen.PopupType.randomPopup());
            }
        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                host.newPopup(PopupScreen.PopupType.WIN);
            }
        }

        // THIS SHOULDN'T BE NECESSARY
        // but the viewport gets very wierd without it, whenever a new popup is made
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("RESIZE", "width: " + width + " , height: " + height);
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        music.stop();
        renderer.dispose();
        host.disposeClient(this);
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

                if (type.equalsIgnoreCase("player")) {
                    Gdx.app.log("Player", "Found a player");
                    int level = obj.getProperties().get("level", Integer.class);
                    this.level = level;

                    String adTimerRange = obj.getProperties().get("adTimerRange", String.class);
                    String[] adTimerRangeSplit = adTimerRange.split(",");
                    int minAdTimer = Integer.parseInt(adTimerRangeSplit[0]);
                    int maxAdTimer = Integer.parseInt(adTimerRangeSplit[1]);
                    this.minAdTimer = minAdTimer;
                    this.maxAdTimer = maxAdTimer;
                    Player root = (Player) prefabLoader.NewPlayerPrefab().WithLevel(level).WithMapRef(map)
                        .WithCameraRef(camera)
                        .WithEntitiesRef(entities)
                        .WithHostRef(host).WithSpawnPosition(new Vector2(x, y));
                    entities.add(root);
                    if (level >= 2) {
                        // spawn 40 more players, spaced evenly around the root player
                        for (int i = 0; i < 40; i++) {
                            // first 20 are on the left, second 20 are on the right
                            int adjustedI = (-40 / 2) + i;
                            float spawnX = x + (adjustedI);
                            Player clone = (Player) prefabLoader.NewPlayerPrefab().WithRoot(root, adjustedI)
                                .WithLevel(level)
                                .WithMapRef(map)
                                .WithEntitiesRef(entities)
                                .WithHostRef(host)
                                .WithSpawnPosition(new Vector2(spawnX, y));
                            entities.add(clone);
                        }
                    }
                }

                if (type.equalsIgnoreCase("patroller")) {
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
                        Sound sound = host.assets.get(Filenames.BIBL_SFX.getFilename(), Sound.class);
                        prefab = (Patroller) prefabLoader.NewBiblPrefab().WithSound(sound);
                    } else if (textureStr.equals("DONUT")) {
                        Sound sound = host.assets.get(Filenames.DONUT_SFX.getFilename(), Sound.class);
                        prefab = (Patroller) prefabLoader.NewDoNUTPrefab().WithSound(sound);
                    }

                    entities.add(prefab.WithWaypoints(path).WithSpawnPosition(new Vector2(x, y)));

                }

                if (type.equalsIgnoreCase("spring")) {
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

                    // random string either Filenames.SPRING_0_SFX or Filenames.SPRING_1_SFX
                    String soundFile = MathUtils.randomBoolean() ? Filenames.SPRING_0_SFX.getFilename()
                        : Filenames.SPRING_1_SFX.getFilename();

                    // log the sound file
                    Gdx.app.log("Sound File", soundFile);

                    Sound sound = host.assets.get(soundFile, Sound.class);

                    entities.add(prefabLoader.NewSpringPrefab().WithSpringiness(springiness).WithImpulseDir(impulseDir)
                        .WithSpawnPosition(new Vector2(x, y)).WithSound(sound));
                }

                if (type.equalsIgnoreCase("entity")) {
                    // parse the TexturerEnum
                    String textureStr = obj.getProperties().get("TextureEnum", String.class);
                    if (textureStr.equals("SPIKE")) {
                        Sound sound = host.assets.get(Filenames.SPIKE_SFX.getFilename(), Sound.class);
                        Entity prefab = prefabLoader.NewSpikePrefab().WithSpawnPosition(new Vector2(x, y))
                            .WithSound(sound);
                        entities.add(prefab);
                    }
                }

                if (type.equalsIgnoreCase("portal")) {
                    // parse the level target
                    String target = obj.getProperties().get("target", String.class);
                    entities.add(prefabLoader.NewPortalPrefab().WithLevelTarget(target)
                        .WithHost(host)
                        .WithSpawnPosition(new Vector2(x, y)));
                }

                if (type.equalsIgnoreCase("codex")) {
                    entities.add(prefabLoader.NewCodexPrefab()
                        .WithHost(host)
                        .WithSpawnPosition(new Vector2(x, y)));
                }

                if (type.equalsIgnoreCase("chaser")) {
                    // parse the TexturerEnum
                    String textureStr = obj.getProperties().get("TextureEnum", String.class);
                    if (textureStr.equals("BfChicken")) {
                        Entity prefab = prefabLoader.NewBuffChickPrefab().WithSpawnPosition(new Vector2(x, y));
                        entities.add(prefab);
                    }
                }

                if (type.equalsIgnoreCase("clamper")) {
                    // parse the TexturerEnum
                    String textureStr = obj.getProperties().get("TextureEnum", String.class);
                    if (textureStr.equals("BRED")) {
                        Sound bredSound = host.assets.get(Filenames.BRED_SFX.getFilename(), Sound.class);
                        entities.add(
                            prefabLoader.NewBredPrefab().WithSpawnPosition(new Vector2(x, y)).WithSound(bredSound));
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
        for (Entity entity : entities) {
            if (entity instanceof Player && ((Player) entity).root == null) {
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

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }

    public Vector2 getEntrancePosition(Vector2 screenCoords) {
        return viewport.unproject(screenCoords);
    }

    @Override
    public void show () {
        if (System.getenv("DEV") == null) { // example: DEV=1 sh gradlew run
            music.setLooping(true);
            music.play();
        }
    }

    @Override
    public void hide () {
        music.pause();
    }
}

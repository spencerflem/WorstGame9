package net.spenc.worstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
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

public class WorstScreen extends ScreenAdapter implements ClientScreen {
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

    public WorstScreen(HostApp host, String level) {
        this.host = host;
        this.map = host.assets.get("maps/" + level + ".tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map, pixels2tiles, host.batch);
        music = Gdx.audio.newMusic(Gdx.files.internal(Filenames.MUSIC.getFilename()));
        music.setLooping(true);
        music.setVolume(.02f);
        if (System.getenv("DEV") == null) { // example: DEV=1 sh gradlew run
            music.play();
        }
        this.camera = new OrthographicCamera();
        this.camera.position.y = 10;
        this.viewport = new FitViewport(30, 20, camera);
        this.prefabLoader = new PrefabLoader(host.assets, pixels2tiles);
        createEntities();
    }

    @Override
    public void render(float delta) {
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
            host.newPopup(false);
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
        host.disposeClient(this);
    }

    private void createEntities() {
        Player p = prefabLoader.NewPlayerPrefab().WithMapRef(map).WithCameraRef(camera).WithHostRef(host);
        entities.add(p);
        entities.add(prefabLoader.NewBuffChickPrefab().WithTarget(p));
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
                    entities.add(prefabLoader.NewPlayerPrefab().WithMapRef(map).WithCameraRef(camera)
                            .WithHostRef(host).WithSpawnPosition(new Vector2(x, y)));
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
                        prefab = prefabLoader.NewBiblPrefab();
                    } else if (textureStr.equals("DONUT")) {
                        prefab = prefabLoader.NewDoNUTPrefab();
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

                    entities.add(prefabLoader.NewSpringPrefab().WithSpringiness(springiness).WithImpulseDir(impulseDir)
                            .WithSpawnPosition(new Vector2(x, y)));
                }

                if (type.equalsIgnoreCase("entity")) {
                    // parse the TexturerEnum
                    String textureStr = obj.getProperties().get("TextureEnum", String.class);
                    if (textureStr.equals("SPIKE")) {
                        Entity prefab = prefabLoader.NewSpikePrefab().WithSpawnPosition(new Vector2(x, y));
                        entities.add(prefab);
                    }
                }

                if (type.equalsIgnoreCase("portal")) {
                    // parse the level target
                    String target = obj.getProperties().get("target", String.class);
                    entities.add(prefabLoader.NewPortalPrefab().WithLevelTarget(target)
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
            });
        });

        // consider giving entities access to the world,
        // since this is the only case where they need it (for initialization)
        // we can just set references in here
        Player playerRef = null; // we use a search to find the player
        ArrayList<Chaser> chaserRefs = new ArrayList<Chaser>(); // we also use a search to find the chasers

        // get the entity that is the player
        for (Entity entity : entities) {
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

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }
}

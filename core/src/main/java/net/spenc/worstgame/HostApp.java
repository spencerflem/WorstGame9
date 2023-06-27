package net.spenc.worstgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

/**
 * Manages shared objects, launching windows, and updating entity state
 */
public class HostApp extends ApplicationAdapter {
    public AssetManager assets;
    public SpriteBatch batch;

    private static final float TIMESTEP = 0.01f;
    private static final float MAX_ACCUMULATOR = 0.1f;
    private double accumulator = 0.0;
    private final Array<Lwjgl3Window> windows = new Array<>();
    private Lwjgl3Window mainWindow;

    @Override
    public void create() {
        assets = new AssetManager();
        batch = new SpriteBatch();
        FileHandleResolver resolver = new InternalFileHandleResolver();
        loadAssetsFolder(assets, "textures", "png", Texture.class, resolver);
        loadAssetsFolder(assets, "sfx", "ogg", Sound.class, resolver);
        assets.setLoader(TiledMap.class, new TmxMapLoader(resolver));
        loadAssetsFolder(assets, "maps", "tmx", TiledMap.class, resolver);
        assets.finishLoading();
        mainWindow = newPopup(true);
    }

    private <T> void loadAssetsFolder(AssetManager assets, String folderName, String extension, Class<T> type,
            FileHandleResolver resolver) {
        FileHandle folder = resolver.resolve("").child(folderName);
        if (!folder.exists()) {
            return;
        }
        for (FileHandle asset : folder.list()) {
            if (Objects.equals(asset.extension(), extension)) {
                assets.load(asset.path(), type);
            }
        }
    }

    @Override
    public void render() {
        accumulator = Math.min(accumulator + Gdx.graphics.getDeltaTime(), MAX_ACCUMULATOR);
        while (accumulator >= TIMESTEP) {
            accumulator -= TIMESTEP;
            updateClients();
        }
    }

    @Override
    public void dispose() {
        assets.dispose();
        batch.dispose();
    }

    private void updateClients() {
        for (int i = 0; i < windows.size; i++) {
            Array<Entity> entities = getClientScreen(windows.get(i)).getEntities();
            for (int j = 0; j < entities.size; j++) {
                Entity entity = entities.get(j);
                entity.update(TIMESTEP);
                entity.updateCollisions(entities); // TODO: ALLOW COLLISIONS WITH OTHER WINDOWS!
            }
        }
    }

    public Lwjgl3Window newPopup(boolean main) {
        ClientApp app = new ClientApp();
        Lwjgl3Window window = ((Lwjgl3Application) Gdx.app).newWindow(app, WindowUtils.getDefaultConfiguration());
        window.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public void created(Lwjgl3Window window) {
                windows.add(window);
            }

            @Override
            public boolean closeRequested() {
                windows.removeValue(window, true);
                if (main) {
                    Gdx.app.exit();
                }
                return true;
            }
        });
        setLevel("level1", window);
        return window;
    }

    public Input focusedInput() {
        for (Lwjgl3Window window : windows) {
            if (window.isFocused()) {
                Input input = getClientApp(window).getInput();
                if (input != null) {
                    return input;
                }
            }
        }
        return Gdx.input;
    }

    public void setLevel(String level) {
        setLevel(level, mainWindow);
    }

    public void setLevel(String level, Lwjgl3Window window) {
        ClientApp app = getClientApp(window);
        Screen oldScreen = app.getScreen();
        Screen newScreen = new WorstScreen(this, level);
        app.setScreen(newScreen);
        if (oldScreen != null) {
            oldScreen.dispose();
        }
    }

    private ClientApp getClientApp(Lwjgl3Window window) {
        return (ClientApp) window.getListener();
    }

    private ClientScreen getClientScreen(Lwjgl3Window window) {
        return (ClientScreen) getClientApp(window).getScreen();
    }

    public void disposeClient(ClientScreen client) {
        for (Entity entity : client.getEntities()) {
            entity.dispose();
        }
    }
}

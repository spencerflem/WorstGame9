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
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import net.spenc.worstgame.entities.Homer;

import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    private Lwjgl3Window overlay;
    private Random random = new Random();
    private Array<Entity> escapees = new Array<>();

    @Override
    public void create() {
        assets = new AssetManager();
        batch = new SpriteBatch();
        FileHandleResolver resolver = new InternalFileHandleResolver();
        loadAssetsFolder(assets, "textures", Collections.singletonList("png"), Texture.class, resolver);
        loadAssetsFolder(assets, "sfx", List.of("ogg", "wav", "mp3"), Sound.class, resolver);
        assets.setLoader(TiledMap.class, new TmxMapLoader(resolver));
        loadAssetsFolder(assets, "maps", Collections.singletonList("tmx"), TiledMap.class, resolver);
        assets.finishLoading();
        mainWindow = newMainWindow();
        overlay = newOverlayWindow();
        getEntities(overlay).add(new Homer()
            .WithTexture(assets.get(Filenames.HELLORB.getFilename())).WithSpawnPosition(new Vector2(400, 400))
            .WithSize(100, 100)
        );
    }

    private <T> void loadAssetsFolder(AssetManager assets, String folderName, List<String> extensions, Class<T> type,
                                      FileHandleResolver resolver) {
        FileHandle folder = resolver.resolve("").child(folderName);
        if (!folder.exists()) {
            return;
        }
        for (FileHandle asset : folder.list()) {
            if (extensions.contains(asset.extension())) {
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
            handleEscapees();
            handleSettlers();
        }
    }

    @Override
    public void dispose() {
        assets.dispose();
        batch.dispose();
    }

    private void updateClients() {
//        for (int i = 0; i < windows.size; i++) {
//            Array<Entity> entities = getClientScreen(windows.get(i)).getEntities();
//            for (int j = 0; j < entities.size; j++) {
//                Entity entity = entities.get(j);
//                entity.update(TIMESTEP);
//                entity.updateCollisions(entities); // TODO: ALLOW COLLISIONS WITH OTHER WINDOWS!
//            }
//        }
    }

    private void handleEscapees() {
//        for (int i = 0; i < windows.size; i++) {
//            Array<Entity> entities = getClientScreen(windows.get(i)).getEntities();
//            for (int j = 0; j < entities.size; j++) {
//                Entity entity = entities.get(j);
//                if (entity.escapes) {
//                    if (entity.position.x < getClientScreen(mainWindow).boundsLeft()
//                        || (entity.position.x + entity.width) > getClientScreen(mainWindow).boundsRight()
//                        || entity.position.y < getClientScreen(mainWindow).boundsTop()
//                        || (entity.position.y + entity.height) > getClientScreen(mainWindow).boundsBottom() ) {
//                        escapees.add(entity);
//                    }
//                }
//            }
//            entities.removeAll(escapees, true);
//            getClientScreen(overlay).getEntities().addAll(escapees);
//            escapees.clear();
//        }
    }

    private void handleSettlers() {
        // TODO: allow any window, not just main
        // needs focus stack
        Array<Entity> entities = getClientScreen(overlay).getEntities();
        for (int j = 0; j < entities.size; j++) {
            Entity entity = entities.get(j);

            Gdx.app.log("Entity", "l:" + entity.position.x + " t:" + entity.position.y + " r:" + (entity.position.x + entity.width) + " b:" + (entity.position.y + entity.height));
            Gdx.app.log("Window", "l:" + (mainWindow.getPositionX() + getClientScreen(mainWindow).boundsLeft()) + " t:" + (mainWindow.getPositionY() + getClientScreen(mainWindow).boundsLeft())
                + " r:" + (mainWindow.getPositionX() + getClientScreen(mainWindow).boundsRight()) + " b:" + (mainWindow.getPositionY() + getClientScreen(mainWindow).boundsBottom()));

            if (entity.position.x > (mainWindow.getPositionX() + getClientScreen(mainWindow).boundsLeft())
                && (entity.position.x + entity.width) < (mainWindow.getPositionX() + getClientScreen(mainWindow).boundsRight())
                && entity.position.y > (mainWindow.getPositionY() + getClientScreen(mainWindow).boundsTop())
                && (entity.position.y + entity.height) < (mainWindow.getPositionY() + getClientScreen(mainWindow).boundsBottom())) {
                escapees.add(entity);
            }
        }
        getClientScreen(mainWindow).getEntities().addAll(escapees);
        entities.removeAll(escapees, true);
        escapees.clear();
        overlay.setVisible(entities.size > 0);
    }

    private Lwjgl3Window newMainWindow() {
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
                Gdx.app.exit();
                return true;
            }
        });
        setLevel("level1", window);
        return window;
    }

    private Lwjgl3Window newOverlayWindow() {
        ClientApp app = new ClientApp();
        Lwjgl3WindowConfiguration configuration = WindowUtils.getOverlayConfiguration();
        configuration.setWindowedMode(Gdx.graphics.getDisplayMode().width - 2, Gdx.graphics.getDisplayMode().height - 2);
        configuration.setWindowPosition(1, 1);
        Lwjgl3Window window = ((Lwjgl3Application) Gdx.app).newWindow(app, configuration);
        window.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public void created(Lwjgl3Window window) {
                windows.add(window);
                WindowUtils.setOverlay(window, true);
            }

            @Override
            public boolean closeRequested() {
                windows.removeValue(window, true);
                return false;
            }
        });
        app.setScreen(new OverlayScreen(this));
        return window;
    }

    public void newPopup(PopupScreen.PopupType type) {
//        ClientApp app = new ClientApp();
//        PopupScreen screen = PopupScreen.fromType(this, type);
//        Lwjgl3WindowConfiguration configuration = WindowUtils.getDefaultConfiguration();
//        configuration.setTitle(screen.title);
//        configuration.setWindowedMode(screen.width, screen.height);
//        configuration.setWindowPosition(
//            mainWindow.getPositionX() + screen.relativePosX + random.nextInt(-100, 100),
//            mainWindow.getPositionY() + screen.relativePosY + random.nextInt(-50, 50)
//        ); // TODO: verify fully within desktop
//        Lwjgl3Window window = ((Lwjgl3Application) Gdx.app).newWindow(app, configuration);
//        window.setWindowListener(new Lwjgl3WindowAdapter() {
//            @Override
//            public void created(Lwjgl3Window window) {
//                windows.add(window);
//            }
//
//            @Override
//            public boolean closeRequested() {
//                windows.removeValue(window, true);
//                screen.dispose();
//                return true;
//            }
//        });
//        app.setScreen(screen);
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

    private ClientApp.ClientScreen getClientScreen(Lwjgl3Window window) {
        return (ClientApp.ClientScreen) getClientApp(window).getScreen();
    }

    private Array<Entity> getEntities(Lwjgl3Window window) {
        return getClientScreen(window).getEntities();
    }

    public void disposeClient(ClientApp.ClientScreen client) {
        for (Entity entity : client.getEntities()) {
            entity.dispose();
        }
    }
}

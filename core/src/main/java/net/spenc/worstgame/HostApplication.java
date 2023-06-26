package net.spenc.worstgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.Objects;

public class HostApplication extends ApplicationAdapter {

    public final WindowCreator windowCreator;
    private Music music;
    private final SharedData shared = new SharedData();
    private boolean started = false;


    public HostApplication(WindowCreator windowCreator) {
        this.windowCreator = windowCreator;
    }

    private <T> void loadAssetsFolder(AssetManager assets, String folderName, String extension, Class<T> type, FileHandleResolver resolver) {
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
    public void create() {
        shared.assets = new AssetManager();
        shared.popupCreator = level -> windowCreator.newPopup(shared, level);
        FileHandleResolver resolver = new InternalFileHandleResolver();
        loadAssetsFolder(shared.assets, "textures", "png", Texture.class, resolver);
        shared.assets.setLoader(TiledMap.class, new TmxMapLoader(resolver));
        loadAssetsFolder(shared.assets, "maps", "tmx", TiledMap.class, resolver);
        music = Gdx.audio.newMusic(Gdx.files.internal(Filenames.MUSIC.getFilename()));
        music.setLooping(true);
        music.setVolume(.02f);
        if (System.getenv("DEV") == null) { // example: DEV=1 sh gradlew run
            music.play();
        }
    }

    @Override
    public void render() {
        if (!started) {
            if (shared.assets.update()) {
                windowCreator.newMain(shared, "level1");
                started = true;
            }
        }
    }

    @Override
    public void dispose() {
        music.stop();
        music.dispose();
    }
}
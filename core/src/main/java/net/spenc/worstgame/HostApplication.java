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

public class HostApplication extends ApplicationAdapter {

    public final PopupWindowCreator popupWindowCreator;
    private Music music;
    private AssetManager assets;
    private boolean started = false;


    public HostApplication(PopupWindowCreator popupWindowCreator) {
        this.popupWindowCreator = popupWindowCreator;
    }

    private <T> void loadAssetsFolder(AssetManager assets, String folderName, Class<T> type, FileHandleResolver resolver) {
        FileHandle folder = resolver.resolve("").child(folderName);
        if (!folder.exists()) {
            return;
        }
        for (FileHandle asset : folder.list()) {
            assets.load(asset.path(), type);
        }
    }

    @Override
    public void create() {
        assets = new AssetManager();
        FileHandleResolver resolver = new InternalFileHandleResolver();
        loadAssetsFolder(assets, "textures", Texture.class, resolver);
        assets.setLoader(TiledMap.class, new TmxMapLoader(resolver));
        loadAssetsFolder(assets, "maps", TiledMap.class, resolver);
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
            if (assets.update()) {
                popupWindowCreator.newMain(assets, "level1");
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

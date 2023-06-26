package net.spenc.worstgame;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.Objects;

public class LoadingScreen extends ScreenAdapter {
    private final WorstGame game;

    public LoadingScreen(WorstGame game) {
        this.game = game;
        FileHandleResolver resolver = new InternalFileHandleResolver();
        loadAssetsFolder(game.assets, "textures", "png", Texture.class, resolver);
        game.assets.setLoader(TiledMap.class, new TmxMapLoader(resolver));
        loadAssetsFolder(game.assets, "maps", "tmx", TiledMap.class, resolver);
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
    public void render(float delta) {
        if (game.assets.update()) {
            game.setScreen(new WorstScreen(game));
        }
    }
}

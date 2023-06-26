package net.spenc.worstgame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SharedData {
    public final AssetManager assets;
    public final PopupCreator popupCreator;
    public final SpriteBatch batch;

    public SharedData(AssetManager assets, PopupCreator popupCreator, SpriteBatch batch) {
        this.assets = assets;
        this.popupCreator = popupCreator;
        this.batch = batch;
    }
}

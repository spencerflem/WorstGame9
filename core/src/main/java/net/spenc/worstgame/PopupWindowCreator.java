package net.spenc.worstgame;

import com.badlogic.gdx.assets.AssetManager;

public interface PopupWindowCreator {
    void newPopup(AssetManager assets, String level);
    void newMain(AssetManager assets, String level);
    void newOverlay(AssetManager assets);
}

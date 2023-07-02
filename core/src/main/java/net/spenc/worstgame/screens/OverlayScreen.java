package net.spenc.worstgame.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import net.spenc.worstgame.ClientApp;
import net.spenc.worstgame.Entity;
import net.spenc.worstgame.HostApp;

public class OverlayScreen extends ScreenAdapter implements ClientApp.ClientScreen {
    private final HostApp host;
    private final OrthographicCamera camera;
    private final ScreenViewport viewport;
    private final Array<Entity> entities = new Array<>();

    public OverlayScreen(HostApp host) {
        this.host = host;
        this.camera = new OrthographicCamera();
        this.viewport = new ScreenViewport(camera);
    }

    @Override
    public void render(float delta) {
        host.setOverlayVisible(entities.size > 0);
        ScreenUtils.clear(Color.CLEAR);
        camera.update();
        host.batch.setProjectionMatrix(camera.combined);
        host.batch.begin();
        for (Entity entity : entities) {
            entity.draw(host.batch);
        }
        host.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }
}

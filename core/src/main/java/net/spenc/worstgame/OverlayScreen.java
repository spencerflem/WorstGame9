package net.spenc.worstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class OverlayScreen extends ScreenAdapter implements ClientApp.ClientScreen {
    private HostApp host;
    private OrthographicCamera camera;
    private ScreenViewport viewport;
    private Array<Entity> entities = new Array<>();

    OverlayScreen(HostApp host) {
        this.host = host;
        this.camera = new OrthographicCamera();
        this.viewport = new ScreenViewport(camera);
    }

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }

    @Override
    public int boundsLeft() {
        return viewport.getScreenX();
    }

    @Override
    public int boundsRight() {
        return viewport.getScreenX() + viewport.getScreenWidth();
    }

    @Override
    public int boundsTop() {
        return viewport.getScreenY();
    }

    @Override
    public int boundsBottom() {
        return viewport.getScreenY() + viewport.getScreenHeight();
    }

    @Override
    public void render(float delta) {
        ((Lwjgl3Input) Gdx.input).windowHandleChanged(0);
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
}

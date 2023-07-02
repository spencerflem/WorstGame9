package net.spenc.worstgame;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class InterstitialScreen extends ScreenAdapter implements ClientApp.ClientScreen {
    private final HostApp host;
    private final Array<Entity> entities = new Array<>();
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Texture texture;
    private final Sound sound;
    private final String nextLevel;
    private long soundId = 0;

    InterstitialScreen(HostApp host, String texture, String sound, String nextLevel) {
        this.host = host;
        this.texture = host.assets.get("textures/" + texture);
        this.sound = host.assets.get("sfx/" + sound);
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(640, 480, camera);
        this.nextLevel = nextLevel;
    }

    @Override
    public void show() {
        sound.stop(soundId);
        soundId = sound.play();
    }

    @Override
    public void hide() {
        sound.stop(soundId);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        camera.update();
        host.batch.setProjectionMatrix(camera.combined);
        host.batch.begin();
        host.batch.draw(texture, 0, 0, 640, 480);
        host.batch.end();
        if (host.justPressed()) {
            host.setLevel(nextLevel);
        }
    }

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}

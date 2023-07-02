package net.spenc.worstgame.screens;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.spenc.worstgame.ClientApp;
import net.spenc.worstgame.HostApp;

public class InterstitialScreen extends ClientApp.ClientScreenAdapter {
    private final HostApp host;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Texture texture;
    private final Sound sound;
    private long soundId = 0;

    public InterstitialScreen(HostApp host, Texture texture, Sound sound) {
        this.host = host;
        this.texture = texture;
        this.sound = sound;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(640, 480, camera);
    }

    @Override
    public void show() {
        if (sound != null) {
            sound.stop(soundId);
            soundId = sound.play();
        }
    }

    @Override
    public void hide() {
        if (sound != null) {
            sound.stop(soundId);
        }
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
            host.advanceLevel();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}

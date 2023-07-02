package net.spenc.worstgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.spenc.worstgame.ClientApp.ClientScreenAdapter;
import net.spenc.worstgame.HostApp;

public class TitleScreen extends ClientScreenAdapter {
    private final HostApp host;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Sound sound;
    private long soundId = 0;

    private Animation<TextureRegion> animation;
    private float stateTime = 0f;

    public TitleScreen(HostApp host, Texture texture, Sound sound) {
        this.host = host;
        this.sound = sound;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(640, 480, camera);

        // load the texture into a sprite sheet
        // all frames are laid out horizontally
        // there are 16 frames, each is an even portion of the texture width
        // the height is the full texture height
        // the frame duration is 0.1 seconds

        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 16, texture.getHeight());

        TextureRegion[] frames = new TextureRegion[16];
        int index = 0;
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++, index++) {
                frames[index] = tmp[i][j];
            }
        }
        this.animation = new Animation<TextureRegion>(0.1f, frames);

        this.stateTime = 0f;
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
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        ScreenUtils.clear(Color.BLACK);
        camera.update();
        host.batch.setProjectionMatrix(camera.combined);
        host.batch.begin();
        host.batch.draw(currentFrame, 0, 0, 640, 480);
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

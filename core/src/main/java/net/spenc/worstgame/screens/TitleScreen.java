package net.spenc.worstgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.audio.Mp3.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.spenc.worstgame.ClientApp.ClientScreenAdapter;
import net.spenc.worstgame.Filenames;
import net.spenc.worstgame.HostApp;

public class TitleScreen extends ClientScreenAdapter {
    private final HostApp host;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Music music;
    private long soundId = 0;

    private Animation<TextureRegion> animation;
    private float stateTime = 0f;

    private float holdTimerDuration = 3f;
    private float holdTimer = holdTimerDuration;

    public TitleScreen(HostApp host, Texture texture) {
        this.host = host;
        this.music = host.assets.get(Filenames.TITLE_MUSIC.getFilename());
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
        this.animation = new Animation<TextureRegion>(0.16f, frames);

        this.stateTime = 0f;
    }

    @Override
    public void show() {
        if (music != null && System.getenv("DEV") == null) { // example: DEV=1 sh gradlew run
            music.setLooping(true);
            music.play();
        }
    }

    @Override
    public void hide() {
        if (music != null) {
            music.pause();
        }
    }

    @Override
    public void render(float delta) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        // if we're on the last frame, stop looping
        if (currentFrame != animation.getKeyFrames()[animation.getKeyFrames().length - 1]) {
            stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        } else {
            if (holdTimer > 0) {
                holdTimer -= Gdx.graphics.getDeltaTime();
            } else {
                stateTime = 0;
                holdTimer = holdTimerDuration;
            }
        }

        ScreenUtils.clear(Color.BLACK);
        camera.update();
        host.batch.setProjectionMatrix(camera.combined);
        host.batch.begin();
        host.batch.draw(currentFrame, 0, 0, 640, 480);
        host.batch.end();
        if (host.justPressed()) {
            host.newPopup(PopupScreen.PopupType.CREDITS);
            host.advanceLevel();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}

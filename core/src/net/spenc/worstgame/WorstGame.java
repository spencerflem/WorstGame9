package net.spenc.worstgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WorstGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture img;
	private Music music;
	private Viewport viewport;
	private Camera camera;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("tentacle_guy.png");
		camera = new OrthographicCamera();
		viewport = new FitViewport(800, 480, camera);
		music = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
		music.setLooping(true);
		music.play();
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		Vector2 mousePos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
		batch.draw(img, mousePos.x - Math.round(img.getWidth() / 2.0), mousePos.y - Math.round(img.getHeight() / 2.0));
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}
}
package net.spenc.worstgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class WorstGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture img;
	private Music music;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		music = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
		music.setLooping(true);
		music.play();
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, Gdx.input.getX() - img.getWidth() / 2,
				Gdx.graphics.getHeight() - Gdx.input.getY() - img.getHeight() / 2);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}
}

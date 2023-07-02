package net.spenc.worstgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.tommyettinger.textra.TextraLabel;
import com.github.tommyettinger.textra.TypingLabel;

import net.spenc.worstgame.ClientApp;
import net.spenc.worstgame.CodexEntry;
import net.spenc.worstgame.Entity;
import net.spenc.worstgame.Filenames;
import net.spenc.worstgame.HostApp;

public class CodexScreen extends ScreenAdapter implements ClientApp.ClientScreen {
    private final HostApp host;
    private final Screen returnScreen;
    private final Array<Entity> entities = new Array<>();
    private int line = -1;
    private Texture tex;
    private final CodexEntry entry;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Music music;
    private final Stage stage;
    private final TextraLabel titleLabel;
    private final TextraLabel indexLabel;
    private final TypingLabel textLabel;

    public CodexScreen(HostApp host, Screen returnScreen) {
        this.host = host;
        this.returnScreen = returnScreen;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(640, 480, camera);
        FileHandle file = Gdx.files.internal("cutscenes/codex" + (host.getCurrentCodexEntry() + 1) + ".json");
        String text = file.readString();
        Json json = new Json();
        this.entry = json.fromJson(CodexEntry.class, text);
        this.tex = host.assets.get(Filenames.CODEXBG.getFilename());
        this.music = host.assets.get("music/" + entry.music);
        stage = new Stage();
        Table table = new Table();
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = host.assets.get(Filenames.DEFAULT_FONT.getFilename());
        titleLabel = new TextraLabel(entry.title, style);
        titleLabel.setPosition(20, 250);
        titleLabel.setColor(Color.BLACK);
        indexLabel = new TextraLabel("Ultra Codex " + String.format("%03d", host.getCurrentCodexEntry() + 1) + "/"
                + String.format("%03d", Gdx.files.internal("cutscenes/").list().length), style);
        indexLabel.setPosition(450, 320);
        textLabel = new TypingLabel("", style);
        textLabel.setPosition(40, 80);
        textLabel.setWidth(560);
        textLabel.wrap = true;
        table.setFillParent(true);
        stage.addActor(table);
        table.addActor(titleLabel);
        table.addActor(indexLabel);
        table.addActor(textLabel);
    }

    @Override
    public void show() {
        super.show();
        host.closeAllPopups();
        if (music != null && System.getenv("DEV") == null) { // example: DEV=1 sh gradlew run
            music.setLooping(true);
            music.play();
        }
    }

    @Override
    public void hide() {
        super.hide();
        if (music != null) {
            music.pause();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        if (host.justPressed()) {
            if (line >= 0 && !textLabel.hasEnded()) {
                textLabel.skipToTheEnd();
            } else {
                line++;
                if (line >= entry.lines.size()) {
                    host.closeCodex(returnScreen);
                } else {
                    if (entry.lines.get(line).image != null) {
                        tex = host.assets.get("textures/" + entry.lines.get(line).image);
                    }
                    textLabel.restart(entry.lines.get(line).text);
                    indexLabel.setVisible(false);
                    titleLabel.setVisible(false);
                    if (entry.lines.get(line).sfx != null) {
                        Sound sound = host.assets.get("sfx/" + entry.lines.get(line).sfx);
                        sound.play();
                    }
                }
            }
        }
        camera.update();
        host.batch.setProjectionMatrix(camera.combined);
        host.batch.begin();
        host.batch.draw(tex, 0, 0, 640, 480);
        host.batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }
}

package net.spenc.worstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Random;

public class PopupScreen extends ScreenAdapter implements ClientScreen {

    public enum PopupType {
        WAR,
        WIN,
        BABY,
        WIZARD,
        CAR;

        private static final List<PopupType> VALUES = List.of(values());
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static PopupType randomPopup() {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }

    public static PopupScreen fromType(HostApp host, PopupType type) {
        return switch (type) {
            case WAR -> new PopupScreen(
                host,
                Filenames.WAR_POPUP_1.getFilename(),
                Filenames.WAR_POPUP_2.getFilename(),
                Filenames.WAR_POPUP_1S.getFilename(),
                Filenames.WAR_POPUP_2S.getFilename(),
                Filenames.WAR_POPUP_LOOP.getFilename(),
                Filenames.WAR_POPUP_SHOOT.getFilename(),
                0.1f,
                3,
                1,
                "BUY BUY BUY BUY BUY BUY BUY BUY BUY BUY BUY BUY BUY BUY BUY BUY BUY BUY BUY BUY BUY",
                null,
                -440,
                -100,
                300,
                600,
                293,
                284
            );
            case WIN -> new PopupScreen(
                host,
                Filenames.WIN_POPUP_1.getFilename(),
                Filenames.WIN_POPUP_2.getFilename(),
                Filenames.WIN_POPUP_1S.getFilename(),
                Filenames.WIN_POPUP_2S.getFilename(),
                Filenames.WIN_POPUP_LOOP.getFilename(),
                Filenames.WIN_POPUP_SHOOT.getFilename(),
                0.15f,
                6,
                2,
                "Win big NOW Slots Gambling Casino Online Win Mega Cash Rewards ONLINE FREE",
                null,
                350,
                40,
                300,
                600,
                8,
                316
            );
            case BABY -> new PopupScreen(
                host,
                Filenames.BABY_POPUP_1.getFilename(),
                null,
                Filenames.BABY_POPUP_1S.getFilename(),
                null,
                Filenames.BABY_POPUP_LOOP.getFilename(),
                Filenames.BABY_POPUP_SHOOT.getFilename(),
                0.2f,
                1,
                4,
                "Baby",
                null,
                0,
                -250,
                970,
                250,
                20,
                40
            );
            case WIZARD -> new PopupScreen(
                host,
                Filenames.WIZARD_POPUP_1.getFilename(),
                Filenames.WIZARD_POPUP_2.getFilename(),
                null,
                null,
                Filenames.WIZARD_POPUP_LOOP.getFilename(),
                null,
                0.4f,
                0,
                0,
                "The Legend of the Wizard: Ultimate Choices - Build Your Kingdom",
                "https://tbug20.itch.io/the-legend-of-the-wizard-of-dissuading-people-from-making-really-bad-choices",
                50,
                50,
                336,
                280,
                0,
                0
            );
            case CAR -> new PopupScreen(
                host,
                Filenames.CAR_POPUP_1.getFilename(),
                null,
                null,
                null,
                Filenames.CAR_POPUP_LOOP.getFilename(),
                null,
                0,
                0,
                0,
                "Drive It Away NOW! Ultimate Cheap Car Budget Rental Free Savings Discount Dealership",
                null,
                10,
                -30,
                700,
                400,
                0,
                0
            );
        };
    }

    private final HostApp host;
    private final Array<Entity> entities = new Array<>();
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final float frameTime;
    private final float cooldown;
    private final int shootFrames;
    private final URI uri;
    private final int barrelPosX;
    private final int barrelPosY;

    private final Texture tex1;
    private final Texture tex2;
    private final Texture shootTex1;
    private final Texture shootTex2;
    private final Sound loop;
    private final Sound shoot;
    private final long loopId;
    private final PrefabLoader prefabLoader;
    private float animationElapsed = 0;
    private boolean isFrame1 = true;

    private float cooldownElapsed = 0.5f;
    private float shootFramesElapsed = 0;
    private boolean isFiring = false;

    public final String title;
    public final int relativePosX;
    public final int relativePosY;
    public final int width;
    public final int height;

    public PopupScreen(HostApp host, String texture1, String texture2, String shootingTexture1, String shootingTexture2, String loopSound, String shootingSound, float frameTime, int shootFrames, float cooldown, String title, String url, int relativePosX, int relativePosY, int width, int height, int barrelPosX, int barrelPosY) {
        this.host = host;
        this.frameTime = frameTime;
        this.shootFrames = shootFrames;
        this.cooldown = cooldown;
        this.title = title;
        this.relativePosX = relativePosX;
        this.relativePosY = relativePosY;
        this.width = width;
        this.height = height;
        this.barrelPosX = barrelPosX;
        this.barrelPosY = barrelPosY;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(width, height, camera);

        this.prefabLoader = new PrefabLoader(host.assets, 1);
        this.tex1 = host.assets.get(texture1);
        this.tex2 = texture2 == null? null : host.assets.get(texture2);
        this.shootTex1 = shootingTexture1 == null? null : host.assets.get(shootingTexture1);
        this.shootTex2 = shootingTexture2 == null? null : host.assets.get(shootingTexture2);
        this.loop = host.assets.get(loopSound);
        this.shoot = shootingSound == null? null : host.assets.get(shootingSound);
        this.uri = url == null? null : URI.create(url);
        loopId = loop.loop();
    }

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        animationElapsed += delta;
        if (frameTime > 0 && tex2 != null) {
            while (animationElapsed > frameTime) {
                isFrame1 = !isFrame1;
                animationElapsed -= frameTime;
            }
        }
        if (isFiring) {
            if (shootFramesElapsed == shootFrames) {
                shootFramesElapsed = 0;
                isFiring = false;
            } else {
                shootFramesElapsed++;
            }
        }
        cooldownElapsed += delta;
        if (cooldown > 0) {
            if (cooldownElapsed > cooldown) {
                fire();
                cooldownElapsed = 0;
                if (shootTex1 != null) {
                    isFiring = true;
                }
                if (shoot != null) {
                    shoot.play();
                }
            }
        }
        if (uri != null && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                Gdx.app.error("Popup", "open browser", e);
            }
        }
        camera.update();
        host.batch.setProjectionMatrix(camera.combined);
        host.batch.begin();
        host.batch.draw(isFiring? (isFrame1? shootTex1 : shootTex2) : (isFrame1 ? tex1 : tex2), 0, 0, width, height);
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
    public void dispose() {
        loop.stop(loopId);
    }

    private void fire() {
        entities.add(prefabLoader.NewHellorbPrefab()
            .WithSpawnPosition(new Vector2(barrelPosX, barrelPosY))
            .WithSound(host.assets.get(Filenames.HELLORBKILL.getFilename()))
        );
    }
}

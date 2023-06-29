package net.spenc.worstgame;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PopupApp extends ScreenAdapter implements ClientScreen {

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

    public static PopupApp fromType(HostApp host, PopupType type) {
        return switch (type) {
            case WAR -> new PopupApp(
                host,
                Filenames.WAR_POPUP_1.getFilename(),
                Filenames.WAR_POPUP_2.getFilename(),
                Filenames.WAR_POPUP_1S.getFilename(),
                Filenames.WAR_POPUP_2S.getFilename(),
                Filenames.WAR_POPUP_LOOP.getFilename(),
                Filenames.WAR_POPUP_SHOOT.getFilename(),
                0.2f,
                3,
                0,
                0,
                0,
                0
            );
            case WIN -> new PopupApp(
                host,
                Filenames.WIN_POPUP_1.getFilename(),
                Filenames.WIN_POPUP_2.getFilename(),
                Filenames.WIN_POPUP_1S.getFilename(),
                Filenames.WIN_POPUP_2S.getFilename(),
                Filenames.WIN_POPUP_LOOP.getFilename(),
                Filenames.WIN_POPUP_SHOOT.getFilename(),
                0.2f,
                3,
                0,
                0,
                0,
                0
            );
            case BABY -> new PopupApp(
                host,
                Filenames.BABY_POPUP_1.getFilename(),
                null,
                Filenames.BABY_POPUP_1S.getFilename(),
                null,
                Filenames.BABY_POPUP_LOOP.getFilename(),
                Filenames.BABY_POPUP_SHOOT.getFilename(),
                0.2f,
                3,
                0,
                0,
                0,
                0
            );
            case WIZARD -> new PopupApp(
                host,
                Filenames.WIZARD_POPUP_1.getFilename(),
                Filenames.WIZARD_POPUP_2.getFilename(),
                null,
                null,
                Filenames.WIZARD_POPUP_LOOP.getFilename(),
                null,
                2f,
                0,
                0,
                0,
                0,
                0
            );
            case CAR -> new PopupApp(
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
                0,
                0,
                0
            );
        };
    }

    private final Array<Entity> entities = new Array<>();

    public PopupApp(HostApp host, String texture1, String texture2, String shootingTexture1, String shootingTexture2, String loopSound, String shootingSound, float frameTime, int shootFrames, int relativePosX, int relativePosY, int barrelPosX, int barrelPosY) {

    }

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }
}

package net.spenc.worstgame;

import com.badlogic.gdx.Gdx;

import net.spenc.worstgame.screens.CodexScreen;
import net.spenc.worstgame.screens.InterstitialScreen;
import net.spenc.worstgame.screens.TitleScreen;
import net.spenc.worstgame.screens.WorstScreen;

import java.util.List;
import java.util.function.Function;

public class ScreenChoreographer {
    private static final List<Function<HostApp, ClientApp.ClientScreen>> SCREEN_ORDER = List.of(
            host -> new TitleScreen(host, host.assets.get(Filenames.TITLE_SCREEN.getFilename()), null),
            host -> new InterstitialScreen(host, host.assets.get(Filenames.LEVEL_1_TITLE.getFilename()), null),
            host -> new WorstScreen(host, host.assets.get(Filenames.LEVEL_1.getFilename())),
            host -> new InterstitialScreen(host, host.assets.get(Filenames.LEVEL_2_TITLE.getFilename()), null),
            host -> new WorstScreen(host, host.assets.get(Filenames.LEVEL_2.getFilename())),
            host -> new InterstitialScreen(host, host.assets.get(Filenames.LEVEL_3_TITLE.getFilename()), null),
            host -> new WorstScreen(host, host.assets.get(Filenames.LEVEL_3.getFilename())),
            host -> new CodexScreen(host, null),
            host -> new InterstitialScreen(host, host.assets.get(Filenames.THANKS.getFilename()), null));

    public static ClientApp.ClientScreen screenFor(HostApp host, int index) {
        if (index < SCREEN_ORDER.size()) {
            return SCREEN_ORDER.get(index).apply(host);
        } else {
            return new ClientApp.ClientScreenAdapter() {
                @Override
                public void show() {
                    Gdx.app.exit();
                }
            };
        }
    }
}

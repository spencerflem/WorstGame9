package net.spenc.worstgame.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.badlogic.gdx.graphics.Color;

import net.spenc.worstgame.OverlayApplication;
import net.spenc.worstgame.PopupWindowCreator;
import net.spenc.worstgame.WorstGame;

import org.lwjgl.glfw.GLFW;

public class DesktopPopupWindowCreator implements PopupWindowCreator {
    @Override
    public void newMain(AssetManager assets, String level) {
        ((Lwjgl3Application) Gdx.app).newWindow(new WorstGame(this, true, level, assets), getDefaultConfiguration());
    }

    @Override
    public void newPopup(AssetManager assets, String level) {
        ((Lwjgl3Application) Gdx.app).newWindow(new WorstGame(this, false, level, assets), getDefaultConfiguration());
    }

    @Override
    public void newOverlay(AssetManager assets) {
        var window = ((Lwjgl3Application) Gdx.app).newWindow(new OverlayApplication(assets), getOverlayConfiguration());
        window.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public void created(Lwjgl3Window window) {
                GLFW.glfwSetWindowAttrib(window.getWindowHandle(), GLFW.GLFW_MOUSE_PASSTHROUGH, GLFW.GLFW_TRUE);
                GLFW.glfwSetWindowAttrib(window.getWindowHandle(), GLFW.GLFW_FLOATING, GLFW.GLFW_TRUE);
            }
        });
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("WorstGame9");
        configuration.useVsync(true);
        //// Limits FPS to the refresh rate of the currently active monitor.
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate);
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
        configuration.setWindowedMode(640, 480);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }

    private static Lwjgl3ApplicationConfiguration getOverlayConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = getDefaultConfiguration();
        configuration.setTransparentFramebuffer(true);
        configuration.setDecorated(false);
        configuration.setInitialBackgroundColor(Color.CLEAR);
        return configuration;
    }
}

package net.spenc.worstgame.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;

import net.spenc.worstgame.PopupWindowCreator;
import net.spenc.worstgame.WorstGame;

import org.lwjgl.glfw.GLFW;

public class DesktopPopupWindowCreator implements PopupWindowCreator {

    //To avoid the top level one from affecting others
    //StartupHelper.startNewJvm("net.spenc.worstgame.desktop.DesktopLauncher");

    @Override
    public void newPopup() {
        newPopup(false);
    }

    @Override
    public void newPopup(boolean overlay) {
        var window = ((Lwjgl3Application) Gdx.app).newWindow(new WorstGame(this), overlay? getOverlayConfiguration() : getDefaultConfiguration());
        window.setWindowListener(new Lwjgl3WindowListener() {
            @Override
            public void created(Lwjgl3Window window) {
                if (overlay) {
                    GLFW.glfwSetWindowAttrib(window.getWindowHandle(), GLFW.GLFW_MOUSE_PASSTHROUGH, GLFW.GLFW_TRUE);
                    GLFW.glfwSetWindowAttrib(window.getWindowHandle(), GLFW.GLFW_FLOATING, GLFW.GLFW_TRUE);
                }
//                GLFW.glfwHideWindow(window.getWindowHandle());
            }

            @Override
            public void iconified(boolean isIconified) {

            }

            @Override
            public void maximized(boolean isMaximized) {

            }

            @Override
            public void focusLost() {

            }

            @Override
            public void focusGained() {

            }

            @Override
            public boolean closeRequested() {
                return true;
            }

            @Override
            public void filesDropped(String[] files) {

            }

            @Override
            public void refreshRequested() {

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
        return configuration;
    }
}

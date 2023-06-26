package net.spenc.worstgame.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.badlogic.gdx.graphics.Color;

import net.spenc.worstgame.ManagedWindow;
import net.spenc.worstgame.WindowListener;
import net.spenc.worstgame.WindowManager;

import org.lwjgl.glfw.GLFW;

public class DesktopWindowManager implements WindowManager {
    @Override
    public void newMain(WindowListener app, NewWindowListener listener) {
        Lwjgl3Window window = ((Lwjgl3Application) Gdx.app).newWindow(app, getDefaultConfiguration());
        window.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public void created(Lwjgl3Window window) {
                ManagedWindow managedWindow = new DesktopManagedWindow(window);
                app.setWindow(managedWindow);
                if (listener != null) {
                    listener.onWindowCreated(managedWindow);
                }
            }
        });
    }

    @Override
    public void newPopup(WindowListener app, NewWindowListener listener) {
        Lwjgl3Window window = ((Lwjgl3Application) Gdx.app).newWindow(app, getDefaultConfiguration());
        window.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public void created(Lwjgl3Window window) {
                ManagedWindow managedWindow = new DesktopManagedWindow(window);
                app.setWindow(managedWindow);
                if (listener != null) {
                    listener.onWindowCreated(managedWindow);
                }
            }
        });
    }

    @Override
    public void newOverlay(WindowListener app, NewWindowListener listener) {
        var window = ((Lwjgl3Application) Gdx.app).newWindow(app, getOverlayConfiguration());
        window.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public void created(Lwjgl3Window window) {
                GLFW.glfwSetWindowAttrib(window.getWindowHandle(), GLFW.GLFW_MOUSE_PASSTHROUGH, GLFW.GLFW_TRUE);
                GLFW.glfwSetWindowAttrib(window.getWindowHandle(), GLFW.GLFW_FLOATING, GLFW.GLFW_TRUE);
                ManagedWindow managedWindow = new DesktopManagedWindow(window);
                app.setWindow(managedWindow);
                if (listener != null) {
                    listener.onWindowCreated(managedWindow);
                }
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

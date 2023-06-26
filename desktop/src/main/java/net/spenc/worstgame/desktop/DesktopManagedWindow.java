package net.spenc.worstgame.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.math.Vector2;

import net.spenc.worstgame.ManagedWindow;

import org.lwjgl.glfw.GLFWWindowPosCallback;

public class DesktopManagedWindow implements ManagedWindow {
    private final Lwjgl3Window window;

    DesktopManagedWindow(Lwjgl3Window window) {
        this.window = window;
    }

    @Override
    public void setPositionListener(WindowPositionListener listener) {
        GLFWWindowPosCallback callback = new GLFWWindowPosCallback() {
            @Override
            public void invoke(long window, int xpos, int ypos) {
                listener.onPositionChanged(xpos, ypos);
            }
        };
        callback.set(window.getWindowHandle());
    }

    @Override
    public Vector2 getPosition() {
        return new Vector2(window.getPositionX(), window.getPositionY());
    }
}

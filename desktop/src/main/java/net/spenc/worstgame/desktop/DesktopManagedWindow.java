package net.spenc.worstgame.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.math.Vector2;

import net.spenc.worstgame.ManagedWindow;

import org.lwjgl.glfw.GLFW;
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

    @Override
    public boolean isFocused() {
        return window.isFocused();
    }

    // hacky as hell
    //            window.setPositionListener((x, y) -> {
    //                render(Gdx.graphics.getDeltaTime());
    //                app.render();
    //                window.swapBuffers();
    //                Gdx.app.log("loc", "x: " + x + ", y: " + y);
    //            });
    @Override
    public void swapBuffers() {
        GLFW.glfwSwapBuffers(window.getWindowHandle());
    }

    @Override
    public void setVisible(boolean visible) {
        window.setVisible(visible);
    }

    @Override
    public void setOverlay(boolean overlay) {
        if (overlay) {
            GLFW.glfwSetWindowAttrib(window.getWindowHandle(), GLFW.GLFW_MOUSE_PASSTHROUGH, GLFW.GLFW_TRUE);
            GLFW.glfwSetWindowAttrib(window.getWindowHandle(), GLFW.GLFW_FLOATING, GLFW.GLFW_TRUE);
        } else {
            GLFW.glfwSetWindowAttrib(window.getWindowHandle(), GLFW.GLFW_MOUSE_PASSTHROUGH, GLFW.GLFW_FALSE);
            GLFW.glfwSetWindowAttrib(window.getWindowHandle(), GLFW.GLFW_FLOATING, GLFW.GLFW_FALSE);
        }
    }
}

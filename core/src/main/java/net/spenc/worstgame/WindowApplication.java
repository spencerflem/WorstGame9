package net.spenc.worstgame;

import com.badlogic.gdx.ApplicationAdapter;

public class WindowApplication extends ApplicationAdapter implements WindowListener {
    private ManagedWindow window;

    @Override
    public void setWindow(ManagedWindow window) {
        this.window = window;
    }

    public ManagedWindow getWindow() {
        return window;
    }
}

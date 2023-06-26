package net.spenc.worstgame;

import com.badlogic.gdx.ApplicationListener;

public interface WindowManager {
    interface NewWindowListener {
        void onWindowCreated(ManagedWindow window);
    }
    void newPopup(WindowListener app, NewWindowListener listener);
    void newMain(WindowListener app, NewWindowListener listener);
    void newOverlay(WindowListener app, NewWindowListener listener);
}

package net.spenc.worstgame;

public interface WindowManager {
    interface WindowListener {
        void onWindowCreated(ManagedWindow window);
        void onWindowDestroyed(ManagedWindow window);
    }
    void newWindow(net.spenc.worstgame.WindowListener app, boolean overlay, WindowListener listener);
}

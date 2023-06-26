package net.spenc.worstgame;

public interface WindowManager {
    interface NewWindowListener {
        void onWindowCreated(ManagedWindow window);
    }
    void newPopup(SharedData shared, String level, NewWindowListener listener);
    void newMain(SharedData shared, String level, NewWindowListener listener);
    void newOverlay(SharedData shared, NewWindowListener listener);
}

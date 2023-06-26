package net.spenc.worstgame;

import com.badlogic.gdx.math.Vector2;

public interface ManagedWindow {
    interface WindowPositionListener {
        void onPositionChanged(int x, int y);
    }

    void setPositionListener(WindowPositionListener listener);
    Vector2 getPosition();
    boolean isFocused();
}

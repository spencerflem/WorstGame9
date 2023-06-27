package net.spenc.worstgame.desktop;

import static net.spenc.worstgame.WindowUtils.getHostConfiguration;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;

import net.spenc.worstgame.HostApp;

/** Launches the desktop (LWJGL3) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        new Lwjgl3Application(new HostApp(), getHostConfiguration());
    }
}

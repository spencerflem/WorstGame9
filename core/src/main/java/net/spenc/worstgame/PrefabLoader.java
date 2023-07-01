package net.spenc.worstgame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import net.spenc.worstgame.entities.Chaser;
import net.spenc.worstgame.entities.Clamper;
import net.spenc.worstgame.entities.Codex;
import net.spenc.worstgame.entities.Homer;
import net.spenc.worstgame.entities.Patroller;
import net.spenc.worstgame.entities.Player;
import net.spenc.worstgame.entities.Portal;
import net.spenc.worstgame.entities.Spring;

public class PrefabLoader {
    private Float PIXEL2TILE;

    private AssetManager managerRef;

    public PrefabLoader(AssetManager AssetManager, float PIXEL2TILE) {
        this.managerRef = AssetManager;
        this.PIXEL2TILE = PIXEL2TILE;
    }

    public Player NewPlayerPrefab() {
        Texture texture = managerRef.get(Filenames.PLAYER.getFilename(), Texture.class);
        return (Player) new Player()
                .WithTexture(texture)
                .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE)
                .WithLayer(1);
    }

    public Patroller NewBiblPrefab() {
        Texture texture = managerRef.get(Filenames.BIBL.getFilename(), Texture.class);
        return (Patroller) new Patroller()
                .WithSpeed(5)
                .WithTexture(texture)
                .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE);
    }

    public Chaser NewBuffChickPrefab() {
        Texture texture = managerRef.get(Filenames.BfChicken.getFilename(), Texture.class);
        return (Chaser) new Chaser()
                .WithSpeed(3)
                .WithTexture(texture)
                .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE);
    }

    public Patroller NewDoNUTPrefab() {
        Texture texture = managerRef.get(Filenames.DONUT.getFilename(), Texture.class);
        return (Patroller) new Patroller()
                .WithSpeed(5)
                .WithTexture(texture)
                .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE);
    }

    public Entity NewSpikePrefab() {
        Texture texture = managerRef.get(Filenames.SPIKE.getFilename(), Texture.class);
        return new Entity()
                .WithTexture(texture)
                .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE);
    }

    public Spring NewSpringPrefab() {
        Texture texture = managerRef.get(Filenames.SPRING.getFilename(), Texture.class);
        return (Spring) new Spring()
                .WithTexture(texture)
                .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE);
    }

    public Portal NewPortalPrefab() {
        Texture texture = managerRef.get(Filenames.PORTAL.getFilename(), Texture.class);
        return (Portal) new Portal()
                .WithTexture(texture)
                .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE);
    }

    public Codex NewCodexPrefab() {
        Texture texture = managerRef.get(Filenames.ULTRACODEX.getFilename(), Texture.class);
        return (Codex) new Codex()
            .WithTexture(texture)
            .WithSize(texture.getWidth() * PIXEL2TILE / 2, texture.getHeight() * PIXEL2TILE / 2);
    }

    public Clamper NewBredPrefab() {
        Texture texture = managerRef.get(Filenames.BRED.getFilename(), Texture.class);
        return (Clamper) new Clamper()
                .WithSpeed(0.05f)
                .WithYPositions(0, 20)
                .WithTexture(texture)
                .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE);
    }

    public Homer NewHellorbPrefab() {
        Texture texture = managerRef.get(Filenames.HELLORB.getFilename(), Texture.class);
        return (Homer) new Homer()
            .WithTexture(texture)
            .WithLayer(10)
            .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE);
    }
}

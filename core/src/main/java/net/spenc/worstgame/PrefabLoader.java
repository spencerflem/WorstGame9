package net.spenc.worstgame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class PrefabLoader {
    private Float PIXEL2TILE;

    private AssetManager managerRef = new AssetManager();

    public PrefabLoader(AssetManager AssetManager, float PIXEL2TILE) {
        this.managerRef = AssetManager;
        this.PIXEL2TILE = PIXEL2TILE;
    }

    public Player NewPlayerPrefab() {
        Texture texture = managerRef.get(Filenames.PLAYER.getFilename(), Texture.class);
        return (Player) new Player()
                .WithSpawnPosition(new Vector2(20, 20))
                .WithTexture(texture)
                .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE)
                .WithLayer(1);
    }

    public Patroller NewBiblPrefab() {
        Texture texture = managerRef.get(Filenames.BIBL.getFilename(), Texture.class);
        return (Patroller) new Patroller()
                .WithSpeed(5)
                .WithWaypoints(new Vector2[] {
                        new Vector2(30, 2),
                        new Vector2(36, 2),
                })
                .WithSpawnPosition(new Vector2(32, 2))
                .WithTexture(texture)
                .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE);
    }

    public Patroller NewDoNUTPrefab() {
        Texture texture = managerRef.get(Filenames.DONUT.getFilename(), Texture.class);
        return (Patroller) new Patroller()
                .WithSpeed(5)
                .WithWaypoints(new Vector2[] {
                        new Vector2(46, 5),
                        new Vector2(46, 12),
                })
                .WithSpawnPosition(new Vector2(46, 14))
                .WithTexture(texture)
                .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE);
    }

    public Entity NewSpikePrefab() {
        Texture texture = managerRef.get(Filenames.SPIKE.getFilename(), Texture.class);
        return new Entity()
                .WithSpawnPosition(new Vector2(0, 0))
                .WithTexture(texture)
                .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE);
    }

    public Spring NewSpringPrefab() {
        Texture texture = managerRef.get(Filenames.SPRING.getFilename(), Texture.class);
        return (Spring) new Spring()
                .WithSpringiness(100)
                .WithImpulseDir(Vector2.Y)
                .WithSpawnPosition(new Vector2(10, 2))
                .WithTexture(texture)
                .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE);
    }

    public Portal NewPortalPrefab() {
        Texture texture = managerRef.get(Filenames.PORTAL.getFilename(), Texture.class);
        return (Portal) new Portal()
            .WithSpawnPosition(new Vector2(4, 2))
            .WithTexture(texture)
            .WithSize(texture.getWidth() * PIXEL2TILE, texture.getHeight() * PIXEL2TILE);
    }
}

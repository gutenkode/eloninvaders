package entities;

import mote4.util.matrix.TransformationMatrix;
import mote4.util.shader.Uniform;
import mote4.util.texture.TextureMap;
import mote4.util.vertex.mesh.MeshMap;

import java.util.ArrayList;

/**
 * Created by Peter on 12/2/16.
 */
public abstract class Entity {
    protected final float[] pos = new float[] {0,0},
                          scale = new float[] {1,1};
    protected int numSprites, spriteInd;
    protected String texName;

    public abstract void update();
    public void render(TransformationMatrix mat) {
        Uniform.varFloat("spriteInfo",numSprites,1,spriteInd);
        TextureMap.bindUnfiltered(texName);
        mat.translate(pos[0],pos[1]);
        mat.scale(scale[0],scale[1],1);
        mat.makeCurrent();
        MeshMap.render("quad");
    }
    public final boolean collides(Entity e) {
        if (e == this)
            return false;
        return
            (pos[0]-scale[0] < e.pos[0]+e.scale[0] &&
             pos[0]+scale[0] > e.pos[0]-e.scale[0] &&
             pos[1]-scale[1] < e.pos[1]+e.scale[1] &&
             pos[1]+scale[1] > e.pos[1]-e.scale[1]);
    }
    public void onCollide(Entity e) {}
    public void onRemove() {}

    ////////////////////////////////////

    private static Player player;
    private static Boss boss;
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static ArrayList<PlayerBullet> pbullets = new ArrayList<>();
    private static ArrayList<EnemyBullet> ebullets = new ArrayList<>();
    private static ArrayList<Enemy> enemies = new ArrayList<>();

    private static ArrayList<Entity> add = new ArrayList<>(),
                                     remove = new ArrayList<>();

    public static void renderAll(TransformationMatrix mat) {
        for (Entity e : enemies) {
            mat.setIdentity();
            e.render(mat);
        }
        mat.setIdentity();
        player.render(mat);
        mat.setIdentity();
        boss.render(mat);
        for (Entity e : entities) {
            mat.setIdentity();
            e.render(mat);
        }
        for (Entity e : ebullets) {
            mat.setIdentity();
            e.render(mat);
        }
        for (Entity e : pbullets) {
            mat.setIdentity();
            e.render(mat);
        }
    }
    public static void updateAll() {
        for (Entity e : add)
            if (e instanceof PlayerBullet)
                pbullets.add((PlayerBullet)e);
            else if (e instanceof EnemyBullet)
                ebullets.add((EnemyBullet)e);
            else if (e instanceof Enemy)
                enemies.add((Enemy)e);
            else if (e instanceof Player)
                player = (Player)e;
            else if (e instanceof Boss)
                boss = (Boss)e;
            else
                entities.add(e);
        add.clear();

        player.update();
        boss.update();
        for (Entity e : pbullets)
            e.update();
        for (Entity e : ebullets)
            e.update();
        for (Entity e : enemies)
            e.update();
        for (Entity e : entities)
            e.update();

        // collision
        for (Entity e1 : enemies) {
            for (Entity e2 : pbullets)
                if (e1.collides(e2))
                    e1.onCollide(e2);
            if (player.collides(e1))
                player.onCollide(e1);
        }
        for (Entity e2 : ebullets)
            if (player.collides(e2))
                player.onCollide(e2);
        for (Entity e2 : pbullets) {
            if (player.collides(e2))
                player.onCollide(e2);
            if (boss.collides(e2))
                boss.onCollide(e2);
        }

        Enemy.globalUpdate();

        for (Entity e : remove) {
            e.onRemove();
            if (e instanceof PlayerBullet)
                pbullets.remove((PlayerBullet)e);
            else if (e instanceof EnemyBullet)
                ebullets.remove((EnemyBullet)e);
            else if (e instanceof Enemy)
                enemies.remove((Enemy)e);
            else if (e instanceof Player)
                player = null;
            else if (e instanceof Boss)
                boss = null;
            else
                entities.remove(e);
        }
        remove.clear();
    }
    public static void add(Entity e) { add.add(e); }
    public static void remove(Entity e) { remove.add(e); }

    public static Player player() { return player; }

    public static void resetAll() {
        player = null;
        boss = null;
        entities.clear();
        pbullets.clear();
        ebullets.clear();
        enemies.clear();
        add.clear();
        remove.clear();
    }
    public static void removeEbullets() {
        ebullets.clear();
    }

}

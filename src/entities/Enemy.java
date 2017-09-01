package entities;

import mote4.util.audio.AudioPlayback;
import mote4.util.matrix.TransformationMatrix;

/**
 * Created by Peter on 12/2/16.
 */
public class Enemy extends Entity {

    private boolean dir;
    private float vel, speed = .0475f, xpos, ypos;
    private int x, y;

    private Enemy(float xp, float yp, int x, int y, String tex, int num, float wratio) {
        pos[0] = xpos = xp;
        pos[1] = ypos = yp;
        scale[1] = .05f;
        scale[0] = scale[1]*wratio;
        this.x = x;
        this.y = y;

        texName = tex;
        numSprites = num;
        spriteInd = 0;
        //numEnemies++;
        //enemies.add(this);
    }

    @Override
    public void update() {
        pos[0] -= (pos[0]-xpos)/3;
        pos[1] -= (pos[1]-ypos)/3;
        /*
        if (Input.isKeyDown(Input.Keys.NO)) {
            Entity.remove(this);
            Entity.add(new EnemyDeath(pos[0], pos[1]));
        }*/
        if (pos[1] > .9)
            Entity.player().kill();
    }
    private void internalUpdate() {
        if (dir && xpos > 1.1f) {
            moveDown = true;
            //ypos += .1;
            newMoveDir = false;
            //vel = 0;
        } else if (!dir && xpos < -1.1f) {
            moveDown = true;
            //ypos += .1;
            newMoveDir = true;
            //vel = 0;
        }

        if (dir)
            vel = speed;
        else
            vel = -speed;
        xpos += vel;
        //vel *= .75;
        spriteInd++;
        spriteInd %= numSprites;
    }
    private void moveDownUpdate() {
        ypos += .095;
        dir = newMoveDir;
    }

    @Override
    public void onCollide(Entity e) {
        if (e instanceof PlayerBullet) {
            if (((PlayerBullet) e).isReset()) {
                Entity.player().kill();
            } else {
                ((PlayerBullet) e).deactivate();
                Entity.remove(this);
                Entity.add(new EnemyDeath(pos[0], pos[1]));
                AudioPlayback.playSfx("edeath");
            }
        }
    }

    @Override
    public void onRemove() {
        numEnemies--;
        enemies[x][y] = null;
    }
    @Override
    public void render(TransformationMatrix mat) {
        //Uniform.varFloat("colorMult",(pos[1]+1)/2,0,1-(pos[1]+1)/2,1);
        super.render(mat);
        //Uniform.varFloat("colorMult",1,1,1,1);
    }

    ////////////////////

    private static int numEnemies = 0,
            sounddelay = 0, soundind = 0;
    private static Enemy[][] enemies;
    //private static ArrayList<Enemy> enemies = new ArrayList<>(); // TODO switch from an ArrayList to a static array
    private static boolean moveDown = false, moveDownTurn = false, newMoveDir;
    private static int xt, yt;

    public static int numEnemies() { return numEnemies; }

    public static void initEnemyState(int r, int c) {
        soundind = 3;
        numEnemies = r*c;

        enemies = new Enemy[6][10];
        for (int j = 0; j < r; j++)
            for (int i = 0; i < c; i++) {
                String sname;
                float ratio;
                if (j == r-1) { sname = "e1"; ratio = 1; }
                else if (j+1 >= r/2) { sname = "e2"; ratio = 11/8f; }
                else { sname = "e3"; ratio = 12/8f; }
                enemies[j][i] = new Enemy(-1f + .22f * i, .1f - .15f * j, j, i, sname,2,ratio);
                Entity.add(enemies[j][i]);
            }
        xt = enemies.length;
        yt = enemies[0].length;
    }

    private static boolean wait = false;
    public static void globalUpdate() {
        if (numEnemies <= 0)
            return;

        sounddelay--;
        if (sounddelay <= 0) {
            sounddelay = numEnemies+5;
            AudioPlayback.playSfx("inv"+(soundind+1));
            soundind++;
            soundind %= 4;
        }

        wait = !wait;
        if (wait)
            return;

        if (Math.random() < .03) // attempt to shoot every frame
            shoot();

        do {
            yt++;
            if (yt >= enemies[0].length) {
                yt = 0;
                xt++;
                if (xt >= enemies.length) {
                    xt = 0;

                    if (moveDown) {
                        moveDownTurn = true;
                        moveDown = false;
                    } else
                        moveDownTurn = false;
                }
            }
        } while (enemies[xt][yt] == null);

        if (moveDownTurn)
            enemies[xt][yt].moveDownUpdate();
        else
            enemies[xt][yt].internalUpdate();

    }
    private static void shoot() {
        for (int a = 0; a < 50; a++) // attempt 5 times then give up (bad style)
        {
            int i = (int)(Math.random()*enemies[0].length); // pick random column
            for (int j = 0; j < enemies.length; j++) {
                if (enemies[j][i] != null) { // first enemy in that row will shoot
                    Entity.add(new EnemyBullet(enemies[j][i].pos[0], enemies[j][i].pos[1]));
                    return; // once an enemy has shot, we are done
                }
            }
        }
    }
}

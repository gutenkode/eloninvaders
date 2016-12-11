package entities;

import main.Input;
import mote4.util.audio.Audio;
import scenes.Ingame;

/**
 * Created by Peter on 12/2/16.
 */
public class PlayerBullet extends Entity {

    private static int numBullets = 0;

    private float speed = .001f, yvel, xvel;
    private boolean active = true, reset = true, hitOnce = false;
    int animcycle = 0;

    public PlayerBullet(float xp, float yp, Player p) {
        pos[0] = xp;
        pos[1] = yp;
        scale[0] = .03f;
        scale[1] = .06f;

        numBullets++;

        texName = "falcon";
        numSprites = 4;

        reset();
    }

    @Override
    public void update() {
        if (reset)
            spriteInd = 0;
        else if (active) {
            animcycle++;
            animcycle %= 10;
            if (animcycle > 5)
                spriteInd = 1;
            else
                spriteInd = 2;
            if (animcycle % 3 == 0)
                Entity.add(new Smoke(pos[0],pos[1], xvel, yvel+.005f));
        } else {
            spriteInd = 3;

            animcycle++;
            animcycle %= 30;
            if (animcycle % 15 == 0)
                Entity.add(new Smoke(pos[0],pos[1], -xvel, -yvel));
        }

        if (!reset) {
            pos[0] += xvel;
            yvel *= .995;
            xvel *= .99;
            if (active)
                yvel += speed;
            else
                yvel -= speed;
            pos[1] -= yvel;

            if (Input.isKeyNew(Input.Keys.YES))
                active = false;
        }
        if (pos[1] > 5) {
            reset = true;
            active = false;
            Ingame.looseCredit();
        } else if (pos[1] < -.1) {
            //yvel *= .5;
            active = false;
        }
        //if (pos[1]+scale[1] < -1)
        //    Entity.remove(this);
    }

    public void resetUpdate(float x, float y) {
        if (!reset)
            return;
        pos[0] =x;//-= (pos[0]-x)/4;
        pos[1] =y;//-= (pos[1]-y)/4;
    }

    public void reset() {
        if (!active) {
            Audio.playSfx("pop");
            active = true;
            reset = true;
            xvel = 0;
            yvel = 0;
        }
    }
    public void fire(float xvel) {
        if (reset) {
            Audio.playSfx("rocket");
            Audio.playSfx("pshoot");
            this.xvel = xvel;
            active = true;
            reset = false;
            yvel = .01f;

            for (int i = 0; i < 6; i++) {
                Entity.add(new Smoke(pos[0],pos[1], xvel+.0025f*i, 0));
                Entity.add(new Smoke(pos[0],pos[1], xvel-.0025f*i, 0));
            }
            hitOnce = false;
        }
    }
    public boolean isReset() { return reset; }
    public void deactivate() {
        active = false;
        yvel *= .9;
        xvel *= .5;
        hitOnce = true;
    }
    public boolean hasHit() { return hitOnce; }

    @Override
    public void onRemove() { numBullets--; }

    public static int numBullets() { return numBullets; }
}

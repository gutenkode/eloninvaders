package entities;

import main.Input;
import mote4.util.audio.Audio;
import mote4.util.audio.JavaAudio;
import mote4.util.matrix.TransformationMatrix;

/**
 * Created by Peter on 12/2/16.
 */
public class Player extends Entity {

    private float vel = 0, speed = .008f;
    private int shootcooldown = 0, health, invulnerability = 0, frame = 0;
    private PlayerBullet bullet;

    public Player() {
        pos[1] = .8f;
        scale[0] = .1f;
        scale[1] = .035f;

        texName = "pad";
        numSprites = 1;
        spriteInd = 0;

        health = 10;

        bullet = new PlayerBullet(pos[0], pos[1], this);
        Entity.add(bullet);
    }

    public void update() {
        if (health > 0) {
            if (Input.isKeyDown(Input.Keys.LEFT))
                vel -= speed;
            else if (Input.isKeyDown(Input.Keys.RIGHT))
                vel += speed;
        } else {
            frame++;
            if (frame % 15 == 0) {
                float r1 = (float)Math.random()*.2f-.1f;
                float r2 = (float)Math.random()*.1f-.05f;
                Entity.add(new Explosion(pos[0]+r1, pos[1]+r2));
                if (frame < 240)
                    Audio.playSfx("phit");
            }
            pos[1] += .002;
        }
        pos[0] += vel;
        vel *= .65;

        pos[0] = (float)Math.max(-1.15, pos[0]);
        pos[0] = (float)Math.min( 1.15, pos[0]);

        bullet.resetUpdate(pos[0], pos[1]-.05f);
        if (health > 0)
            if (Input.isKeyNew(Input.Keys.YES)) {
                if (PlayerBullet.numBullets() < 1 || shootcooldown <= 0)
                {
                    shootcooldown = 35;
                    bullet.fire(vel);
                    //Entity.add(new PlayerBullet(pos[0], pos[1], vel/2, this));
                }
            }
        shootcooldown--;

        if (health > 0 && invulnerability > 0)
            invulnerability--;
    }

    @Override
    public void onCollide(Entity e) {
        if (e instanceof Enemy) {
            health = 0;
            invulnerability = 50;
            //Entity.remove(e);
        } if (e instanceof EnemyBullet) {
            if (invulnerability == 0) {
                if (health > 0)
                    health--;
                Entity.add(new Explosion(e.pos[0],e.pos[1]));
                invulnerability = 50;
                Audio.playSfx("phit");
            }
            Entity.remove(e);
        } else if (e instanceof PlayerBullet) {
            PlayerBullet pb = (PlayerBullet)e;
            pb.reset();
        }
    }

    @Override
    public void render(TransformationMatrix mat) {
        if (invulnerability/5 % 2 == 0)
            super.render(mat);
    }

    public int health() { return health; }

    public void kill() {
        JavaAudio.stopAudio("dayonedark");
        health = 0;
        invulnerability = 50;
    }
}

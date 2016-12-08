package entities;

import mote4.util.audio.Audio;
import mote4.util.audio.JavaAudio;
import mote4.util.matrix.TransformationMatrix;
import mote4.util.shader.Uniform;
import mote4.util.texture.TextureMap;
import mote4.util.vertex.mesh.MeshMap;

/**
 * Created by Peter on 12/4/16.
 */
public class Boss extends Entity {

    private enum State {
        WAIT,FILL,BATTLE,DEAD,WIN;
    }
    private double cycle;
    private int frame,
            health, maxhealth,
            invulnerability = 0,
            attack1count = 3, attack1cooldown,
            phase;
    private State state;

    public Boss() {
        pos[0] = 0;
        pos[1] = -3;

        scale[0] = .3f*(52/60f);
        scale[1] = .3f;

        texName = "endeavor";
        numSprites = 1;
        spriteInd = 0;

        phase = 0;
        maxhealth = 7;
        health = 0;
        state = State.WAIT;
    }

    @Override
    public void update() {
        if (invulnerability > 0)
            invulnerability--;

        switch (state) {
            case WAIT:
                if (Enemy.numEnemies() == 0) {
                    if (pos[1] < -.4)
                        pos[1] += .005; // enter the screen
                    else {
                        JavaAudio.playOgg("murph");
                        frame = 0;
                        state = State.FILL;
                    }
                }
                break;
            case FILL:
                frame++; // global frame count
                if (frame > 100 && frame % 10 == 0) {
                    health++;
                    Audio.playSfx("ping");
                    if (health == maxhealth) {
                        frame = 0;
                        state = State.BATTLE;
                    }
                }
                break;
            case BATTLE:
                if (frame == 0 && phase == 0)
                    JavaAudio.playOgg("dayonedark");
                frame++; // global frame count

                if (frame < 30) return;

                // move left/right
                pos[0] = (float)Math.sin(cycle)*.5f;
                cycle += .01;

                if (frame < 55) return;

                // radial attack
                if (frame % 40 == 0) {
                    Audio.playSfx("bshoot");
                    for (int i = 0; i < 6; i++) {
                        double angle = (i / 6d) * Math.PI * 2;
                        Entity.add(new BossBullet(pos[0], pos[1], angle + cycle, "bbullet", .007f + .005f * phase));
                    }
                }

                if (phase <= 0) return;

                // directed attack
                if (attack1cooldown <= 0)
                    if (frame % 10 == 0)
                    {
                        attack1count--;
                        if (attack1count == 0)
                        {
                            attack1cooldown = 90;
                            attack1count = 3;
                        }
                        Player p = Entity.player();
                        double angle = Math.atan2(p.pos[1]-pos[1], p.pos[0]-pos[0]);

                        Entity.add(new BossBullet(pos[0], pos[1], angle+.33f, "bbullet2", .017f));
                        Entity.add(new BossBullet(pos[0], pos[1], angle-.33f, "bbullet2", .017f));

                        Audio.playSfx("bshoot");
                    }
                attack1cooldown--;
                break;
            case DEAD:
                frame++;
                if (frame % 5 == 0) {
                    float r1 = (float)Math.random()*.6f-.3f;
                    float r2 = (float)Math.random()*.6f-.3f;
                    Entity.add(new Explosion(pos[0]+r1, pos[1]+r2));
                    if (frame % 15 == 0)
                        Audio.playSfx("bhit");
                }
                if (invulnerability <= 0) {
                    phase++;
                    frame = 0;
                    if (phase >= 2)
                        state = State.WIN;
                    else {
                        maxhealth += 4;
                        state = State.FILL;
                    }
                }
                break;
            case WIN:
                JavaAudio.stopAudio("dayonedark");
                invulnerability = 99999;
                break;
        }
    }

    @Override
    public void onCollide(Entity e) {
        if (state != State.BATTLE)
            return;
        if (e instanceof PlayerBullet) {
            PlayerBullet pb = (PlayerBullet)e;
            if (!pb.hasHit()) {
                if (invulnerability == 0) {
                    invulnerability = 30;
                    pb.deactivate();
                    Entity.add(new Explosion(e.pos[0], e.pos[1]));
                    Audio.playSfx("bhit");
                    health--;
                    if (health <= 0) {
                        Entity.removeEbullets();
                        health = 0;
                        frame = 0;
                        state = State.DEAD;
                        invulnerability = 220;
                    }
                }
            }
        }
    }

    @Override
    public void render(TransformationMatrix mat) {
        if (invulnerability > 0)
            Uniform.varFloat("colorMult",1,.5f,.5f,1);
        if (invulnerability/5 % 2 == 0)
            super.render(mat);
        if (invulnerability > 0)
            Uniform.varFloat("colorMult",1,1,1,1);

        if (health > 0) {
            Uniform.varFloat("spriteInfo", 1, 1, 0);
            TextureMap.bindUnfiltered("healthbar");
            mat.setIdentity();
            mat.translate(pos[0], pos[1] - .36f);
            mat.scale(.035f, .05f, 1);
            mat.translate(-maxhealth + 1, 0);
            for (int i = 0; i < health; i++) {
                mat.makeCurrent();
                float cval = (float) i / maxhealth;
                Uniform.varFloat("colorMult", 1 - cval, cval, 0, 1);
                MeshMap.render("quad");
                mat.translate(2, 0);
            }
            Uniform.varFloat("colorMult", 1, 1, 1, 1);
        }
    }

    public int phase() { return phase; }
}

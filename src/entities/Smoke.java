package entities;

/**
 * Created by Peter on 12/4/16.
 */
public class Smoke extends Entity {

    private float[] vel = new float[2];
    private int life;

    public Smoke(float xp, float yp, float xv, float yv) {
        float rand = (float)Math.random()*.01f-.005f;
        pos[0] = xp +rand;
        pos[1] = yp +rand;
        vel[0] = xv +rand;
        vel[1] = yv +rand;

        scale[0] = .035f;
        scale[1] = .035f;

        texName = "smoke";
        numSprites = 4;

        life = 60;
    }


    @Override
    public void update() {
        life--;
        spriteInd = 3-life/15;
        if (life <= 0)
            Entity.remove(this);

        pos[0] += vel[0];
        pos[1] += vel[1];
        vel[0] *= .95;
        vel[1] *= .95;
        vel[1] -= .0001;
    }
}

package entities;

/**
 * Created by Peter on 12/3/16.
 */
public class EnemyBullet extends Entity {

    private float speed = .01f;
    private int cycle;

    public EnemyBullet(float xp, float yp) {
        pos[0] = xp;
        pos[1] = yp;
        scale[0] = .015f;
        scale[1] = .05f;

        texName = "ebullet";
        numSprites = 4;
    }

    @Override
    public void update() {
        cycle++;
        cycle %= 20;
        spriteInd = cycle/5;

        pos[1] += speed;
        if (pos[1]-scale[1] > 1)
            Entity.remove(this);
    }
}

package entities;

/**
 * Created by Peter on 12/4/16.
 */
public class EnemyDeath extends Entity {

    private int life;

    public EnemyDeath(float xp, float yp) {
        pos[0] = xp;
        pos[1] = yp;

        scale[0] = .075f;
        scale[1] = .05f;

        texName = "edeath";
        numSprites = 1;

        life = 25;
    }


    @Override
    public void update() {
        life--;
        if (life <= 0)
            Entity.remove(this);
    }
}

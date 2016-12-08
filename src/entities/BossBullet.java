package entities;

import mote4.util.matrix.TransformationMatrix;
import mote4.util.shader.Uniform;
import mote4.util.texture.TextureMap;
import mote4.util.vertex.mesh.MeshMap;

/**
 * Created by Peter on 12/4/16.
 */
public class BossBullet extends EnemyBullet {

    private float speed;
    private double angle;
    private int cycle;

    public BossBullet(float xp, float yp, double a, String sprite, float spd) {
        super(xp, yp);

        scale[0] = .01f;
        scale[1] = .01f;
        angle = a;
        speed = spd;

        texName = sprite;
        numSprites = 4;
    }

    @Override
    public void update() {
        cycle++;
        cycle %= 20;
        spriteInd = cycle/5;

        pos[0] += Math.cos(angle)*speed;
        pos[1] += Math.sin(angle)*speed;

        if (pos[1]-scale[1] > 1 || pos[1]+scale[1] < -1 ||
            pos[0]-scale[0] > 1.3 || pos[0]+scale[0] < -1.3)
            Entity.remove(this);
    }
    @Override
    public void render(TransformationMatrix mat) {
        Uniform.varFloat("spriteInfo",numSprites,1,spriteInd);
        TextureMap.bindUnfiltered(texName);
        mat.translate(pos[0],pos[1]);
        mat.scale(scale[0]*5,scale[1]*5,1);
        mat.makeCurrent();
        MeshMap.render("quad");
    }
}

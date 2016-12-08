package entities;

import mote4.util.matrix.TransformationMatrix;
import mote4.util.shader.Uniform;
import mote4.util.texture.TextureMap;
import mote4.util.vertex.mesh.MeshMap;

/**
 * Created by Peter on 12/4/16.
 */
public class Explosion extends Entity {

    private int life;

    public Explosion(float xp, float yp) { this(xp,yp,.2f); }
    public Explosion(float xp, float yp, float s) {
        float rand = (float)Math.random()*.02f-.01f;
        pos[0] = xp +rand;
        rand = (float)Math.random()*.02f-.01f;
        pos[1] = yp +rand;

        scale[0] = s;
        scale[1] = s;

        texName = "expl";
        numSprites = 6;

        life = 12;
    }


    @Override
    public void update() {
        life--;
        spriteInd = 5-life/2;
        /*
        if (Math.random() < .1) {
            if (scale[0] > .1)
                for (int i = 0; i < 5; i++) {
                    float r1 = (float) Math.random() * .3f - .15f;
                    float r2 = (float) Math.random() * .3f - .15f;
                    Entity.add(new Explosion(pos[0]+r1, pos[1]+r2, scale[0] * .5f));
                }
        }
        */
        if (life <= 0) {
            Entity.remove(this);
        }
    }
    /*
    public void render(TransformationMatrix mat) {
        Uniform.varFloat("spriteInfo",2,2,spriteInd);
        TextureMap.bindUnfiltered(texName);
        mat.translate(pos[0],pos[1]);
        mat.scale(scale[0],scale[1],1);
        mat.makeCurrent();
        MeshMap.render("quad");
    }*/
}

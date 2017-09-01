package main;

import mote4.scenegraph.Window;
import mote4.util.audio.ALContext;
import mote4.util.audio.AudioLoader;
import mote4.util.shader.ShaderUtils;
import mote4.util.texture.TextureMap;
import mote4.util.vertex.FontUtils;
import mote4.util.vertex.builder.StaticMeshBuilder;
import mote4.util.vertex.mesh.MeshMap;
import scenes.Title;

import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeLimits;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Peter on 12/2/16.
 */
public class Main {

    public static void main(String[] args) {
        if (System.getProperty("os.name").toLowerCase().contains("mac"))
            System.setProperty("java.awt.headless", "true"); // prevents ImageIO from hanging on OS X

        Window.setTitle("Elon Invaders");
        Window.initWindowedPercent(.9, 171/128f);
        Window.setVsync(true);

        Input.createKeyCallback();
        Input.pushLock(Input.Lock.PLAYER);
        loadResources();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //glEnable(GL_CULL_FACE);

        glfwSetWindowSizeLimits(Window.getWindowID(), 640, 360, GLFW_DONT_CARE, GLFW_DONT_CARE);
        //glfwSetWindowAspectRatio(Window.getWindowID(), 171,128);

        Window.addScene(new Title());
        Window.loop();
    }
    private static void loadResources() {
        ShaderUtils.addProgram("texture.vert", "texture.frag", "texture");
        ShaderUtils.addProgram("sprite.vert", "sprite.frag", "sprite");
        TextureMap.loadIndex("index.txt");

        //FontUtils.loadMetric("font/terminal_metric","font_1");
        FontUtils.useMetric("monospace");
        //FontUtils.useMetric("font_1");

        MeshMap.add(StaticMeshBuilder.loadQuadMesh(), "quad");

        ALContext.initContext();
        AudioLoader.loadIndex("index.txt");
        /*
        AudioLoader.loadWav("invaderkilled","edeath");
        AudioLoader.loadWav("firework","phit");
        AudioLoader.loadWav("shoot","pshoot");
        AudioLoader.loadWav("bigboom","bhit");
        AudioLoader.loadWav("pew","bshoot");
        AudioLoader.loadWav("rocket");
        AudioLoader.loadWav("ping");
        AudioLoader.loadWav("pop");
        AudioLoader.loadWav("charge");
        AudioLoader.loadWav("laser");

        AudioLoader.loadWav("inv1");
        AudioLoader.loadWav("inv2");
        AudioLoader.loadWav("inv3");
        AudioLoader.loadWav("inv4");*/
    }
}

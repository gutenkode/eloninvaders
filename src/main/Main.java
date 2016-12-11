package main;

import mote4.scenegraph.Window;
import mote4.util.audio.ALContext;
import mote4.util.audio.Audio;
import mote4.util.audio.JavaAudio;
import mote4.util.shader.ShaderUtils;
import mote4.util.texture.TextureMap;
import mote4.util.vertex.FontUtils;
import mote4.util.vertex.builder.StaticMeshBuilder;
import mote4.util.vertex.mesh.MeshMap;
import scenes.*;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowAspectRatio;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeLimits;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Peter on 12/2/16.
 */
public class Main {

    public static void main(String[] args) {
        if (System.getProperty("os.name").toLowerCase().contains("mac"))
            System.setProperty("java.awt.headless", "true"); // prevents ImageIO from hanging on OS X

        //Window.initWindowed(1920/2,1080/2);
        Window.initWindowedPercent(.9, 171/128f);
        Window.setVsync(true);

        //main.Input.createCharCallback();
        Input.createKeyCallback();
        Input.pushLock(Input.Lock.PLAYER);
        loadResources();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //glEnable(GL_CULL_FACE);

        glfwSetWindowSizeLimits(Window.getWindowID(), 640, 360, GLFW_DONT_CARE, GLFW_DONT_CARE);
        //glfwSetWindowAspectRatio(Window.getWindowID(), 171,128);


        //FBO ingame = new FBO(853,480,true,false,null); // 640x480, PS1 resolution
        //FBO ingame = new FBO(1280/2,720/2,true,false,null);
        //FBO ingame = new FBO(1920/3,1080/3,true,false,null);
        //ingame.addToTextureMap("fbo_scene");
        /*
        Layer l = new Layer(new EmptyTarget());
        l.addScene(new Ingame());
        Window.addLayer(l);

        Window.addScene(new Post());*/
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
        Audio.loadWav("invaderkilled","edeath");
        Audio.loadWav("firework","phit");
        Audio.loadWav("shoot","pshoot");
        Audio.loadWav("bigboom","bhit");
        Audio.loadWav("pew","bshoot");
        Audio.loadWav("rocket");
        Audio.loadWav("ping");
        Audio.loadWav("pop");
        Audio.loadWav("charge");
        Audio.loadWav("laser");

        Audio.loadWav("inv1");
        Audio.loadWav("inv2");
        Audio.loadWav("inv3");
        Audio.loadWav("inv4");
    }
}

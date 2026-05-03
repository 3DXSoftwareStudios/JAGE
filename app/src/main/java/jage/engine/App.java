package jage.engine;

import jage.engine.api.API;
import org.lwjgl.opengl.GL11;

public class App extends EngineApplication {

    public static void main(String[] args) {
        API api = new API(32);
        App app = new App(api, "Default-App");

        api.getAppEngine().setApp(app, 0);
        api.startEngine();
    }

    public App(API EngineAPI, String name) {
        super(EngineAPI, name);
    }

    @Override
    public boolean init() {
        System.out.println("Initing Engine Default App");
        return true;
    }

    @Override
    public boolean startRequest() {
        System.out.println("Starting Engine Default App");
        
        System.out.println("Initing GFX System");
        api.initEMT("JAGE", 1240, 720);
        
        return true;
    }

    @Override
    public boolean stopRequest() {
        System.out.println("Stoping Engine Default App");
        return true;
    }

    @Override
    public boolean pauseRequest() {
        System.out.println("Pauseing Engine Default App");
        return true;
    }

    @Override
    public boolean resumeRequest() {
        System.out.println("Resumeing Engine Default App");
        return true;
    }

    @Override
    public boolean versionCheck(int currentEngineVersion) {
        return true;
    }

    @Override
    public void render() {
        GL11.glViewport(0, 0, api.getWidth(), api.getHeight());
        
        GL11.glClearColor(0f, 0f, 0f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
    }

    @Override
    public void update() {
        
    }
    
}
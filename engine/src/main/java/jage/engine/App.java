/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.engine;

import jage.engine.api.API;
import jage.engine.api.GLGraphics;
import java.io.File;

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
        if(!new File("./resources/lang").exists()){
            api.getLangResource().buildLanguages();
        }
        api.getLangResource().initLangs();
        api.getLangResource().switchLang("EN");
        return true;
    }

    @Override
    public boolean startRequest() {
        System.out.println("Starting Engine Default App");
        
        System.out.println("Initing GFX System");
        api.initEMT(api.getLangResource().lang_CURRENT[0] + " - " + api.getLangResource().lang_CURRENT[1], 1240, 720);
        
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
        GLGraphics gfx = api.getGraphics();
        api.getGraphics().setColor(0,0,0,255);
        api.getGraphics().fillRect(0, 0, api.getWidth(), api.getHeight());
        
    }

    @Override
    public void update() {
        
    }
    
}
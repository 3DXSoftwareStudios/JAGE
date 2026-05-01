/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class App extends EngineApplication{
    
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
        System.out.println("Asking Engine to Create Window");
        api.initGFXContext("JAGE", 1240, 720);
        
        System.out.println("Initing GFX System");
        api.initGFXPanel();
        api.initEMT();
        
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
        return true;}

    @Override
    public boolean resumeRequest() {
        System.out.println("Resumeing Engine Default App");
        return true;}

    @Override
    public boolean versionCheck(int currentEngineVersion) { //Practicly not relevant, since Default will always be latest Version
        
        return true;
    }
    
    @Override
    public void render() {
        BufferStrategy bs = api.getDrawItem();
        Graphics gfx = bs.getDrawGraphics();
        gfx.setColor(Color.black);
        gfx.fillRect(0, 0, 1240, 720);
        
        //Show Content
        gfx.dispose();
        bs.show();
    }
    
    @Override
    public void update() {
        
    }
    
}
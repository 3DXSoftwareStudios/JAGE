/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.engine;

public abstract class EngineApplication {
    
    protected API    api;
    private   String name;
    
    public EngineApplication(API EngineAPI, String name){
        this.api  = EngineAPI;
        this.name = name;
    }
    
    public abstract boolean init();
    
    public abstract boolean startRequest();     // -> start
    public abstract boolean stopRequest();      // -> stop
    public abstract boolean pauseRequest();     // -> pause
    public abstract boolean resumeRequest();    // -> resume
    
    public abstract boolean versionCheck(int currentEngineVersion); // -> Version Check
    
    public String  getName(){
        return name;
    }
    
    //Engine Things
    public abstract void render();
    public abstract void update();
    
}
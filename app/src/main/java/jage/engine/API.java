/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.engine;

public class API {
    
    private ApplicationEngine appEngine;
    private boolean           EngineStarted;
    
    public API(int appCount){
        appEngine = new ApplicationEngine(appCount);
    }
    
    public ApplicationEngine getAppEngine(){
        return appEngine;
    }

    public boolean startEngine() {
        if(EngineStarted || !appEngine.checkApp(0)) return false;
        
        appEngine.startApp(0);
        return true;
    }
    
}
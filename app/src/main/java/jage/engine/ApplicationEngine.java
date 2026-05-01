/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.engine;

public class ApplicationEngine {
    
    private final int maxApps;
    private EngineApplication[] apps;
    
    public ApplicationEngine(int maxApps){
        this.maxApps = maxApps;
        apps = new EngineApplication[maxApps];
    }
    
    public void setApp(EngineApplication app, int appID){
        if(appID < 0 || appID >= maxApps){
            return;
        }
        apps[appID] = app;
        if(apps[appID].init()){
            System.out.println("Succesfully Inited App " + app.getName() + " with ID " + appID);
        }else{
            System.err.println("Failed Inited App " + app.getName() + " with ID " + appID);
        }
    }
    
    public void startApp(int appID){
        if(appID < 0 || appID >= maxApps){
            return;
        }
        if(apps[appID].startRequest()){
            System.out.println("Succesfully Started App " + apps[appID].getName() + " with ID " + appID);
        }else{
            System.err.println("Failed Starting App " + apps[appID].getName() + " with ID " + appID);
        }
    }
    
    public void stopApp(int appID){
        if(appID < 0 || appID >= maxApps){
            return;
        }
        if(apps[appID].stopRequest()){
            System.out.println("Succesfully Stoped App " + apps[appID].getName() + " with ID " + appID);
        }else{
            System.err.println("Failed Stoped App " + apps[appID].getName() + " with ID " + appID);
        }
    }
    
    public void globalRender(){
        for(int i = 0; i < maxApps; i++){
            if(!checkApp(i)){
                apps[i].render();
            }
        }
    }
    
    public void globalUpdate(){
        for(int i = 0; i < maxApps; i++){
            if(!checkApp(i)){
                apps[i].update();
            }
        }
    }
    
    public boolean checkApp(int appID) { //true -> null; false -> existes
        return (apps[appID] == null);
    }
    
}
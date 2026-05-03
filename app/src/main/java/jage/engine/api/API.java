/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.engine.api;

import jage.engine.ApplicationEngine;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

public class API {

    private ApplicationEngine appEngine;
    private boolean           EngineStarted;
    private boolean           EngineRunningState;

    // GFX (LWJGL)
    private long              windowHandle;
    private int               width;
    private int               height;

    // Rates
    private int               fps;
    private int               ups;

    // Threads
    private Thread            EMT; // Engine Management Thread

    public API(int appCount){
        appEngine = new ApplicationEngine(appCount);

        // Default Values
        ups  = 60;
        fps  = 60;
    }

    public ApplicationEngine getAppEngine(){
        return appEngine;
    }

    public boolean startEngine() {
        if (EngineStarted || appEngine.checkApp(0)) return false;

        appEngine.startApp(0);

        EngineStarted      = true;
        EngineRunningState = true;
        return true;
    }

    public void setRate(int FPS, int UPS){
        this.fps  = FPS;
        this.ups  = UPS;
    }
    
    /**
     * WARNING: DO NOT CALL MANUALY!
     */
    @Deprecated
    public void initGFXContext(String title, int pWidth, int pHeight) {
        this.width  = pWidth;
        this.height = pHeight;

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        windowHandle = GLFW.glfwCreateWindow(pWidth, pHeight, title, 0, 0);
        if (windowHandle == 0L) {
            throw new IllegalStateException("Failed to create GLFW window");
        }

        GLFW.glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();

        GLFW.glfwSwapInterval(1);
        GLFW.glfwSetWindowPos(windowHandle, 100, 100);
        GLFW.glfwShowWindow(windowHandle);
    }
    
    public void initEMT(String title, int WindowWidth, int WindowHeight){
        EMT = new Thread(new Runnable(){
            @Override
            public void run() {
                initGFXContext(title, WindowWidth, WindowHeight);
                
                long initialTime = System.nanoTime();
                final double timeU = 1_000_000_000.0 / ups;
                final double timeF = 1_000_000_000.0 / fps;
                double deltaU = 0, deltaF = 0;
                int frames = 0, ticks = 0;
                long timer = System.currentTimeMillis();
                
                while (EngineRunningState) {

                    long currentTime = System.nanoTime();
                    deltaU += (currentTime - initialTime) / timeU;
                    deltaF += (currentTime - initialTime) / timeF;
                    initialTime = currentTime;
                    
                    
                    GLFW.glfwPollEvents();
                    
                    // Updates
                    while (deltaU >= 1) {
                        appEngine.globalUpdate();
                        ticks++;
                        deltaU--;
                    }

                    // Render
                    if (deltaF >= 1) {
                        
                        appEngine.globalRender();
                        frames++;
                        deltaF--;

                        GLFW.glfwSwapBuffers(windowHandle);
                    }
                    
                    if(GLFW.glfwWindowShouldClose(windowHandle)){
                        appEngine.KernelKill();
                        System.exit(0);
                    }
                    
                    if (System.currentTimeMillis() - timer > 1000) {
                        System.out.println(String.format("UPS: %s, FPS: %s", ticks, frames));
                        frames = 0;
                        ticks  = 0;
                        timer += 1000;
                    }
                }
                
                GLFW.glfwDestroyWindow(windowHandle);
                GLFW.glfwTerminate();
            }
        }, "EngineManagementThread");
        EMT.start();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
}
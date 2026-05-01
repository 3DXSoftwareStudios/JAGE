/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class API {
    
    private ApplicationEngine appEngine;
    private boolean           EngineStarted;
    private boolean           EngineRunningState;
    
    //GFX
    private JFrame            window;
    private Canvas            panel;
    private int               fps;
    private int               ups;
    
    //Threads
    private Thread            EMT; //Engine Managment Thread
    
    public API(int appCount){
        appEngine = new ApplicationEngine(appCount);
        
        //Default Values
        ups  = 60;
        fps  = 60;
    }
    
    public ApplicationEngine getAppEngine(){
        return appEngine;
    }

    public boolean startEngine() {
        if(EngineStarted || appEngine.checkApp(0)) return false;
        
        appEngine.startApp(0);
        
        EngineStarted      = true;
        EngineRunningState = true;
        return true;
    }
    
    public void setRate(int FPS, int UPS){
        this.fps  = FPS;
        this.ups  = UPS;
    }
    
    public void initEMT(){
        EMT = new Thread(new Runnable(){
            @Override
            public void run() {

            long initialTime = System.nanoTime();
            final double timeU = 1000000000 / ups;
            final double timeF = 1000000000 / fps;
            double deltaU = 0, deltaF = 0;
            int frames = 0, ticks = 0;
            long timer = System.currentTimeMillis();
                while (EngineRunningState) {

                    long currentTime = System.nanoTime();
                    deltaU += (currentTime - initialTime) / timeU;
                    deltaF += (currentTime - initialTime) / timeF;
                    initialTime = currentTime;
                    
                    if (deltaU >= 1) {
                        appEngine.globalUpdate();
                        ticks++;
                        deltaU--;
                    }

                    if (deltaF >= 1) {
                        appEngine.globalRender();
                        frames++;
                        deltaF--;
                    }
                    
                    if (System.currentTimeMillis() - timer > 1000) {
                        System.out.println(String.format("UPS: %s, FPS: %s", ticks, frames));
                        frames = 0;
                        ticks  = 0;
                        timer += 1000;
                    }
                    
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    System.getLogger(API.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
                    
                }
            }
        });
        EMT.start();
    }
    
    public void initGFXContext(String title, int pWidth, int pHeight) {
        window = new JFrame(title);
        window.setSize(pWidth, pHeight);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(3);
        window.setVisible(true);
    }
    
    public void initGFXPanel() {
        if (window == null) {
            throw new IllegalStateException("GFXContext not initialised jet.");
        }

        panel = new Canvas();
        panel.setPreferredSize(window.getSize());
        panel.setFocusable(false);

        window.add(panel, BorderLayout.CENTER);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        panel.createBufferStrategy(2);
    }
    
    public BufferStrategy getDrawItem(){
        return panel.getBufferStrategy();
    }
    
}
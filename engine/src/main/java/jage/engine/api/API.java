/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.engine.api;

import jage.engine.ApplicationEngine;
import jage.engine.resources.LanguageResource;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class API {

    private ApplicationEngine appEngine;
    private boolean           EngineStarted;
    private boolean           EngineRunningState;
    
    private LanguageResource  langResource;
    
    // GFX (LWJGL)
    private long              windowHandle;
    private int               width;
    private int               height;
    private Graphics          glgfx;

    // Swing Render Components
    private JFrame            frame;
    private RenderPanel       renderPanel;

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
        
        langResource = new LanguageResource();
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
    
    public void initEMT(String title, int WindowWidth, int WindowHeight){
        this.width  = WindowWidth;
        this.height = WindowHeight;

        initRenderWindow(title);

        EMT = new Thread(new Runnable(){
            @Override
            public void run() {
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
                    
                    // Updates
                    while (deltaU >= 1) {
                        appEngine.globalUpdate();
                        ticks++;
                        deltaU--;
                    }

                    // Render
                    if (deltaF >= 1) {
                        if (renderPanel != null) {
                            renderPanel.repaint();
                        }
                        frames++;
                        deltaF--;
                    }
                    
                    if (System.currentTimeMillis() - timer > 1000) {
                        //System.out.println(String.format("UPS: %s, FPS: %s", ticks, frames));
                        frames = 0;
                        ticks  = 0;
                        timer += 1000;
                    }
                }
                
            }
        }, "EngineManagementThread");
        EMT.start();
    }
    
    private void initRenderWindow(String title) {
        frame = new JFrame(title);
        renderPanel = new RenderPanel();

        renderPanel.setPreferredSize(new Dimension(width, height));
        frame.add(renderPanel);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private class RenderPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            glgfx = g;
            appEngine.globalRender();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public Graphics getGraphics(){
        return glgfx;
    }

    public ApplicationEngine getAppEngine(){
        return appEngine;
    }
    
    public LanguageResource getLangResource(){
        return langResource;
    }
}
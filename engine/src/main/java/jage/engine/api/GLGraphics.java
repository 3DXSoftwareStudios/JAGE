/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.engine.api;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

public class GLGraphics {
    
    private Color currentColor = Color.WHITE;
    private Font currentFont = new Font("Arial", Font.PLAIN, 16);
    
    public GLGraphics() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }
    
    public void init(int windowWidth, int windowHeight){
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, windowWidth, windowHeight, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }
    
    public void setColor(int colorARGB) {
        currentColor = new Color(colorARGB, true);
        applyCurrentColor();
    }

    public void setColor(int r, int g, int b, int a) {
        currentColor = new Color(r, g, b, a);
        applyCurrentColor();
    }

    private void applyCurrentColor() {
        float r = currentColor.getRed() / 255.0f;
        float g = currentColor.getGreen() / 255.0f;
        float b = currentColor.getBlue() / 255.0f;
        float a = currentColor.getAlpha() / 255.0f;
        glColor4f(r, g, b, a);
    }

    public void setFont(String name, int size, int style) {
        currentFont = new Font(name, style, size);
    }
    
    public void drawRect(int x, int y, int width, int height) {
        applyCurrentColor();
        glBegin(GL_LINE_LOOP);
        glVertex2f(x,         y);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x,         y + height);
        glEnd();
    }

    public void fillRect(int x, int y, int width, int height) {
        applyCurrentColor();
        glBegin(GL_QUADS);
        glVertex2f(x,         y);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x,         y + height);
        glEnd();
    }
    
    public void drawString(String text, int x, int y) {
        if (text == null || text.isEmpty()) return;
        
        BufferedImage img = renderTextToImage(text);
        int w = img.getWidth();
        int h = img.getHeight();

        int texId = uploadBufferedImageToTexture(img);

        applyCurrentColor();

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texId);

        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); glVertex2f(x,     y);
        glTexCoord2f(1f, 0f); glVertex2f(x + w, y);
        glTexCoord2f(1f, 1f); glVertex2f(x + w, y + h);
        glTexCoord2f(0f, 1f); glVertex2f(x,     y + h);
        glEnd();

        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);

        glDeleteTextures(texId);
    }

    private BufferedImage renderTextToImage(String text) {
        BufferedImage tmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tmp.createGraphics();
        g2d.setFont(currentFont);
        FontMetrics fm = g2d.getFontMetrics();
        int w = fm.stringWidth(text);
        int h = fm.getHeight();
        g2d.dispose();

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setFont(currentFont);
        g2d.setColor(currentColor);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int ascent = fm.getAscent();
        g2d.drawString(text, 0, ascent);
        g2d.dispose();
        return img;
    }
    
    public void drawImage(int[] colorsARGB, int x, int y, int width, int height) {
        if (colorsARGB == null || colorsARGB.length != width * height) return;

        int texId = uploadARGBToTexture(colorsARGB, width, height);

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texId);
        
        glColor4f(1f, 1f, 1f, 1f);

        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); glVertex2f(x,         y);
        glTexCoord2f(1f, 0f); glVertex2f(x + width, y);
        glTexCoord2f(1f, 1f); glVertex2f(x + width, y + height);
        glTexCoord2f(0f, 1f); glVertex2f(x,         y + height);
        glEnd();

        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);

        glDeleteTextures(texId);
        
        applyCurrentColor();
    }
    
    private int uploadARGBToTexture(int[] argb, int width, int height) {
        // ARGB -> RGBA ByteBuffer
        ByteBuffer buffer = memAlloc(width * height * 4);
        for (int i = 0; i < argb.length; i++) {
            int c = argb[i];
            int a = (c >> 24) & 0xFF;
            int r = (c >> 16) & 0xFF;
            int g = (c >> 8)  & 0xFF;
            int b = (c)       & 0xFF;
            buffer.put((byte) r);
            buffer.put((byte) g);
            buffer.put((byte) b);
            buffer.put((byte) a);
        }
        buffer.flip();

        int texId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0,
                     GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glBindTexture(GL_TEXTURE_2D, 0);
        memFree(buffer);
        return texId;
    }
    
    private int uploadBufferedImageToTexture(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        int[] pixels = new int[width * height];
        img.getRGB(0, 0, width, height, pixels, 0, width);

        return uploadARGBToTexture(pixels, width, height);
    }
}
/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.engine.resources;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZInputStream;
import org.tukaani.xz.XZOutputStream;

public class LanguageResource {
    
    private String[] lang_DE;
    private String[] lang_EN;
    public  String[] lang_CURRENT;
    
    public void buildLanguages(String langSRC, String langDIST){
        FileOutputStream fout = null;
        try {
            System.out.println("Building Languages for Engine use");
            new File(langDIST).mkdirs();
            System.out.print("Compressing DE-DE: " + new File(langSRC + "/DE-DE.txt").length());
            byte[] comp = compressResource(langSRC + "/DE-DE.txt");
            fout = new FileOutputStream(langDIST + "/German.pck");
            fout.write(comp);
            fout.close();
            System.out.println(" -> " + comp.length);
            System.out.print("Compressing EN-EN: " + new File(langSRC + "/EN-EN.txt").length());
            comp = compressResource(langSRC + "/EN-EN.txt");
            fout = new FileOutputStream(langDIST + "/English.pck");
            fout.write(comp);
            fout.close();
            System.out.println(" -> " + comp.length);
            System.out.println("Finished Building Langs");
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }
    
    public void initLangs(String langRoot){
        try {
            lang_DE = loadLang(langRoot + "/German.pck");
            lang_EN = loadLang(langRoot + "/English.pck");
        } catch (IOException ex) {
        }
    }
    
    public void switchLang(String LangCode){
        switch(LangCode){
            case "DE":
                lang_CURRENT = lang_DE;
                break;
            case "EN":
                lang_CURRENT = lang_EN;
                break;
            default:
                lang_CURRENT = lang_EN;
                break;
        }
    }
    
    public static String[] loadLang(String path) throws IOException {
        byte[] compressed = Files.readAllBytes(Paths.get(path));
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (XZInputStream xzIn = new XZInputStream(new java.io.ByteArrayInputStream(compressed))) {
            byte[] buffer = new byte[4096];
            int n;
            while ((n = xzIn.read(buffer)) != -1) {
                baos.write(buffer, 0, n);
            }
        }
        
        String fullText = baos.toString("UTF-8");
        return fullText.split("\n");
    }
    
    public static byte[] compressResource(String sourcepath) throws IOException {
        byte[] input = Files.readAllBytes(Paths.get(sourcepath));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LZMA2Options options = new LZMA2Options();
        options.setPreset(6);

        try (XZOutputStream xzOut = new XZOutputStream(baos, options)) {
            xzOut.write(input);
        }

        return baos.toByteArray();
    }
    
}
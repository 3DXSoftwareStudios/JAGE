/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.editor;

import jage.editor.projectManager.ProjectManagerUI;
import jage.engine.resources.LanguageResource;
import java.io.File;

public class Main {
    
    private static LanguageResource lang;
    
    public static void main(String[] args){
        System.out.println("Launching JAGE Editor");
        lang = new LanguageResource();
        if(!new File("./resources").exists()){
            lang.buildLanguages("./editorRSCsrc/lang", "./resources/editor/lang");
        }
        lang.initLangs("./resources/editor/lang");
        lang.switchLang("DE");
        
        new ProjectManagerUI(lang).setVisible(true);
    }
    
}
/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.editor;

import jage.engine.App;
import jage.engine.resources.LanguageResource;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.eclipse.jgit.api.errors.GitAPIException;



public class Main {
    
    private static LanguageResource lang;
    
    public static void main(String[] args){
        System.out.println("Launching JAGE Editor");
        lang = new LanguageResource();
        
        try {
            new ProjectManager().createProject("../testproject/Test");
        } catch (IOException ex) {
            System.getLogger(Main.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (GitAPIException ex) {
            System.getLogger(Main.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
}
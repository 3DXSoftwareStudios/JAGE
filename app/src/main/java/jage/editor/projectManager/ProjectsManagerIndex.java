/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.editor.projectManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ProjectsManagerIndex {
    
    private Project[] projects;
    
    public ProjectsManagerIndex(){
        projects = new Project[32];
    }
    
    public void saveProjects() throws FileNotFoundException{
        FileOutputStream fout = new FileOutputStream(new File("./JAGEcfg"));
    }
    
    public class Project{
        private String root;
        private String name;
        
        public Project(){
        }
        
        public void setRoot(String root){
            this.root = root;
        }
        
        public void setName(String name){
            this.name = name;
        }
        
        public String getRoot(){
            return root;
        }
        
        public String getName(){
            return name;
        }
    }
    
}
/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.editor.projectManager;

import java.io.File;
import java.io.FileOutputStream;

public class ProjectsManagerIndex {
    
    private Project[] projects;
    
    public ProjectsManagerIndex(){
        projects = new Project[32];
    }
    
    public void saveProjects() throws Exception{
        FileOutputStream fout = new FileOutputStream(new File("./JAGEcfg/Projects.ini"));
        for(int i = 0; i < projects.length; i++){
            if(projects[i] != null){
                fout.write("|".getBytes());
                fout.write(projects[i].getName().getBytes());
                fout.write(":".getBytes());
                fout.write(projects[i].getRoot().getBytes());
                fout.write("|".getBytes());
            }
        }
        fout.close();
    }
    
    public void loadProjects() throws Exception {
        File cfg = new File("./JAGEcfg/Projects.ini");
        if (!cfg.exists()) {
            return;
        }

        String content = java.nio.file.Files.readString(cfg.toPath());
        if (content.isBlank()) {
            return;
        }
        
        projects = new Project[32];
        
        String[] entries = content.split("\\|+");

        int index = 0;
        for (String entry : entries) {
            if (entry.isBlank()) continue;

            String[] parts = entry.split(":", 2);
            if (parts.length != 2) continue;

            Project p = new Project();
            p.setName(parts[0].trim());
            p.setRoot(parts[1].trim());

            if (index < projects.length) {
                projects[index++] = p;
            }
        }
    }
    
    public boolean addProject(String text, String text0) {
        boolean found = false;
        
        for(int i = 0; i < projects.length; i++){
            if(projects[i] == null && found == false){
                projects[i] = new Project();
                projects[i].setName(text);
                projects[i].setRoot(text0);
                found = true;
                
                try {
                    saveProjects();
                } catch (Exception ex) {
                    System.getLogger(ProjectsManagerIndex.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
        }
        
        return found;
    }
    
    public void removeProject(int index) {
        Project p = projects[index];
        if (p != null) {
            File rootDir = new File(p.getRoot());
            deleteDirectory(rootDir); // <--- rekursiv löschen
        }

        projects[index] = null;

        try {
            saveProjects();
        } catch (Exception ex) {
            System.getLogger(ProjectsManagerIndex.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    private boolean deleteDirectory(File dir) {
        if (!dir.exists()) return false;

        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteDirectory(f);
                } else {
                    f.delete();
                }
            }
        }
        return dir.delete();
    }
    
    public Project[] getProjects() {
        return projects;
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
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class ProjectManager {
    
    public void createProject(String ProjectRoot) throws FileNotFoundException, IOException, GitAPIException{
        System.out.println("Creating new Project");
        
        //Folder Structure
        new File(ProjectRoot).mkdirs();
        new File(ProjectRoot + "/.project").mkdirs();
        new File(ProjectRoot + "/src").mkdirs();
        new File(ProjectRoot + "/src/extension").mkdirs();
        new File(ProjectRoot + "/src/pscript").mkdirs();
        new File(ProjectRoot + "/rsc").mkdirs();
        new File(ProjectRoot + "/localenv").mkdirs();
        
        //Files
        
        //.gitignore
        FileOutputStream ign = new FileOutputStream(ProjectRoot + "/.gitignore");
        ign.write("build\n".getBytes());
        ign.write("dist\n".getBytes());
        ign.close();
        
        ign = new FileOutputStream(ProjectRoot + "/.project/index.service");
        String[] indexed = runIndexer(ProjectRoot);
        for(int i = 0; i < indexed.length; i++){
            ign.write((indexed[i] + "\n").getBytes());
        }
        ign.close();
        
        //init Git for Project -> JGit
        System.out.println("Preparing Versioning");
        Git git = Git.init()
                .setDirectory(new File(ProjectRoot))
                .call();
        
        git.add()
                .addFilepattern(".")
                .call();

        // Commit
        git.commit()
                .setMessage("Initial commit")
                .call();
        git.close();
        
        System.out.println("Project Creation Finished!");
    }
    
    public String[] runIndexer(String path) {
        File root = new File(path);

        try {
            String base = root.getCanonicalPath();
            List<String> result = new ArrayList<>();

            collect(root, base, result);

            return result.toArray(new String[0]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Helper
    private void collect(File file, String base, List<String> result) {
        try {
            if (file == null || !file.exists()) return;

            String absolute = file.getCanonicalPath();
            String relative = absolute.substring(base.length());
            if (relative.isEmpty()) {
                relative = ".";
            }
            
            if(!relative.contains(".project")){
                result.add(relative);
            }

            if (file.isDirectory()) {
                File[] children = file.listFiles();
                if (children != null) {
                    for (File child : children) {
                        collect(child, base, result);
                    }
                }
            }
        } catch (IOException ex) {
            System.getLogger(ProjectManager.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
}
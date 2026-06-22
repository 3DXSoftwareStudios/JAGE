/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.editor.projectManager;

import jage.engine.resources.LanguageResource;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProjectManagerUI extends javax.swing.JFrame {
    
    private static LanguageResource     lang;
    private static ProjectsManagerIndex PMI;
    
    public ProjectManagerUI(LanguageResource lang) {
        initComponents();
        this.lang = lang;

        PMI = new ProjectsManagerIndex();
        
        setLocationRelativeTo(null);

        jButton2.setText(lang.lang_CURRENT[3]);
        
        try {
            PMI.loadProjects();
        } catch (Exception ex) {
            System.getLogger(ProjectManagerUI.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        setupProjectsListUI();
        refreshProjectList();

        initLang();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DE", "EN" }));
        jComboBox1.addItemListener(this::jComboBox1ItemStateChanged);

        jLabel1.setText("Language:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 507, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton2.setText("jButton2");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addGap(0, 445, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        lang.switchLang(jComboBox1.getItemAt(jComboBox1.getSelectedIndex()));
        initLang();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        ProjectSetupUI PSUI = new ProjectSetupUI(lang, PMI, this);
        PSUI.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    //Post Var Block (Modifiable)
    private javax.swing.JScrollPane projectsScrollPane;
    private javax.swing.JPanel projectsListPanel;
    //End of Post Var Block
    
    private void initLang(){
        setTitle(lang.lang_CURRENT[1]);
        
        jLabel1.setText(lang.lang_CURRENT[2] + ":");
        jButton2.setText(lang.lang_CURRENT[3]);
    }
    
    private void setupProjectsListUI() {
        jPanel1.setLayout(new java.awt.BorderLayout());

        projectsListPanel = new javax.swing.JPanel();
        projectsListPanel.setLayout(new javax.swing.BoxLayout(projectsListPanel, javax.swing.BoxLayout.Y_AXIS));

        projectsScrollPane = new javax.swing.JScrollPane(projectsListPanel);
        projectsScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanel1.add(projectsScrollPane, java.awt.BorderLayout.CENTER);
    }
    
    public void refreshProjectList() {
        projectsListPanel.removeAll();

        ProjectsManagerIndex.Project[] projects = PMI.getProjects();
        if (projects == null) {
            projectsListPanel.revalidate();
            projectsListPanel.repaint();
            return;
        }

        for (int i = 0; i < projects.length; i++) {
            ProjectsManagerIndex.Project p = projects[i];
            if (p == null) continue;
            
            JPanel entry = new JPanel();
            entry.setPreferredSize(new java.awt.Dimension(0, 75));
            entry.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 75));
            entry.setMinimumSize(new java.awt.Dimension(0, 75));

            entry.setLayout(new BorderLayout());
            entry.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
            entry.setBackground(new java.awt.Color(235, 235, 235));
            
            JPanel grid = new JPanel(new GridLayout(2, 2));
            grid.setOpaque(false);

            JLabel nameLabel = new JLabel(p.getName());
            nameLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));

            JLabel titleLabel = new JLabel(p.getRoot());
            titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
            
            JButton editButton = new JButton(lang.lang_CURRENT[8]);
            JButton deleteButton = new JButton(lang.lang_CURRENT[9]);
            
            int index = i;
            
            editButton.addActionListener(e -> {
                // TODO: Edit Projects
            });
            
            deleteButton.addActionListener(e -> {
                PMI.removeProject(index);
                refreshProjectList();
            });
            
            grid.add(nameLabel);
            grid.add(editButton);
            grid.add(titleLabel);
            grid.add(deleteButton);

            entry.add(grid, BorderLayout.CENTER);

            projectsListPanel.add(entry);
            projectsListPanel.add(Box.createVerticalStrut(8));
        }

        projectsListPanel.revalidate();
        projectsListPanel.repaint();
    }
    
}
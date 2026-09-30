package com.mycompany.wordtype;
public class Home extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Home.class.getName());

    private Runnable onUserButtonClick;

    // ตำแหน่งปุ่ม btnUser (x ตายตัว, ปุ่มจะขยายไปทางขวาตามความยาวข้อความ)
    private static final int USER_BTN_X = 940;
    private static final int USER_BTN_Y = 48;
    private static final int USER_BTN_H = 28;
    private static final int USER_BTN_MAX_RIGHT = 1180; // ขอบขวาสุดที่ปุ่มไปได้ (หน้าต่างกว้าง 1200)

    /**
     * Creates new form Home
     */
    public Home() {
        initComponents();
        // เซ็ต font
        jLabel1.setFont(new java.awt.Font("Space Grotesk Medium", java.awt.Font.PLAIN, 32));
        jLabel2.setFont(new java.awt.Font("Space Grotesk Medium", java.awt.Font.PLAIN, 32));
        
        // ===== btnUser: ไอคอน + ข้อความชิดซ้ายสุดของปุ่ม =====
        btnUser.setForeground(new java.awt.Color(128, 128, 128));

        btnUser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnUser.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnUser.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnUser.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        resizeUserButton();

        // 2. คำสั่งเปลี่ยนสีตัวหนังสือตอนเอาเมาส์ไปชี้
        btnUser.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // ตอนเอาเมาส์ไปชี้: ให้คำว่า Login เปลี่ยนเป็น "สีดำเข้ม"
                btnUser.setForeground(new java.awt.Color(0, 0, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // ตอนเอาเมาส์ออก: ให้คำว่า Login กลับไปเป็น "สีเทา" เหมือนเดิม
                btnUser.setForeground(new java.awt.Color(128, 128, 128));
            }
        });
        
        // เสกปุ่ม Time ให้กลายเป็นแคปซูล
        styleCapsuleButton(btnTimeMode);
        styleCapsuleButton(btnWordsMode);
        styleCapsuleButton(btnTime15);
        styleCapsuleButton(btnTime30);
        styleCapsuleButton(btnTime60);
        styleCapsuleButton(btnTime120);

        // บังคับให้ปุ่ม Time เริ่มต้นมาเป็นสีเขียว (ถูกกดไว้)
        btnTimeMode.setSelected(true);
    }

    /**
     * โหลดไอคอนจาก classpath แบบไม่ crash ถ้าหาไฟล์ไม่เจอ
     * (จะพิมพ์ชื่อไฟล์ที่หายลง console แล้วคืน null -> ปุ่มแสดงแค่ข้อความ)
     */
    private javax.swing.ImageIcon loadIcon(String path) {
        java.net.URL url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Icon not found on classpath: " + path);
            return null;
        }
        return new javax.swing.ImageIcon(url);
    }

    /** ให้ MainFrame ดึง panel หลักของหน้านี้ไปใส่ใน CardLayout */
    public javax.swing.JPanel getRootPanel() {
        return jPanel1;
    }
    
    /** ให้ MainFrame กำหนดว่ากดปุ่ม user แล้วจะทำอะไร */
    public void setOnUserButtonClick(Runnable onUserButtonClick){
        this.onUserButtonClick = onUserButtonClick;
    }

    public void styleCapsuleButton(javax.swing.JToggleButton btn) {
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setFont(new java.awt.Font("Roboto Mono", java.awt.Font.BOLD, 15));

        btn.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                btn.setForeground(btn.isSelected() ? java.awt.Color.WHITE : java.awt.Color.BLACK);
            }
        });
        btn.setForeground(btn.isSelected() ? java.awt.Color.WHITE : java.awt.Color.BLACK);

        btn.setUI(new javax.swing.plaf.basic.BasicToggleButtonUI() {
            @Override
            public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                if (((javax.swing.AbstractButton) c).isSelected()) {
                    g2.setColor(new java.awt.Color(0, 103, 91)); // เขียวเข้ม
                } else {
                    g2.setColor(new java.awt.Color(213, 214, 216)); // เทาอ่อน
                }
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);
                g2.dispose();
                super.paint(g, c);
            }
        });
    }

    public void setUsername(String username) {
        boolean loggedIn = username != null && !username.isEmpty();
        btnUser.setText(loggedIn ? username : "Login");
        btnUser.setToolTipText(loggedIn ? "Click to log out" : null);
        resizeUserButton(); // ขยาย/หดปุ่มตามจำนวนตัวอักษร
    }

    /**
     * ปรับความกว้าง btnUser ให้พอดีกับไอคอน + ข้อความ
     * x ตายตัว ปุ่มจะขยายไปทางขวา และไม่เกิน USER_BTN_MAX_RIGHT
     * (ถ้าชื่อยาวมาก ข้อความจะถูกตัดเป็น ...)
     */
    private void resizeUserButton() {
        int width = btnUser.getPreferredSize().width + 4; // เผื่อขอบขวาเล็กน้อย
        int maxWidth = USER_BTN_MAX_RIGHT - USER_BTN_X;
        btnUser.setBounds(USER_BTN_X, USER_BTN_Y, Math.min(width, maxWidth), USER_BTN_H);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnLeader = new javax.swing.JButton();
        btnUser = new javax.swing.JButton();
        btnTimeMode = new javax.swing.JToggleButton();
        btnWordsMode = new javax.swing.JToggleButton();
        btnTime120 = new javax.swing.JToggleButton();
        btnTime15 = new javax.swing.JToggleButton();
        btnTime30 = new javax.swing.JToggleButton();
        btnTime60 = new javax.swing.JToggleButton();
        Typeping = new javax.swing.JScrollPane();
        TextTypeing = new javax.swing.JTextPane();
        btnRestart = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1200, 700));
        setResizable(false);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(225, 225, 227));
        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 700));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Heiti TC", 0, 32)); // NOI18N
        jLabel1.setText(">_word");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(150, 36, 191, 41);

        jLabel2.setFont(new java.awt.Font("Heiti TC", 0, 32)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 100));
        jLabel2.setText("type");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(270, 36, 191, 41);

        btnLeader.setForeground(new java.awt.Color(142, 144, 148));
        btnLeader.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/wordtype/images/LeaderIcon_OFF.png"))); // NOI18N
        btnLeader.setBorderPainted(false);
        btnLeader.setContentAreaFilled(false);
        btnLeader.setFocusPainted(false);
        btnLeader.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/wordtype/images/LeaderIcon_ON.png"))); // NOI18N
        jPanel1.add(btnLeader);
        btnLeader.setBounds(898, 52, 18, 19);

        btnUser.setFont(new java.awt.Font("Roboto Mono", 0, 16)); // NOI18N
        btnUser.setForeground(new java.awt.Color(142, 144, 148));
        btnUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/wordtype/images/userIcon.png"))); // NOI18N
        btnUser.setText("Login");
        btnUser.setBorderPainted(false);
        btnUser.setContentAreaFilled(false);
        btnUser.setFocusPainted(false);
        btnUser.setIconTextGap(8);
        btnUser.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/wordtype/images/userIcon_hover.png"))); // NOI18N
        btnUser.addActionListener(this::btnUserActionPerformed);
        jPanel1.add(btnUser);
        btnUser.setBounds(940, 48, 220, 28);

        buttonGroup1.add(btnTimeMode);
        btnTimeMode.setFont(new java.awt.Font("Roboto Mono", 1, 15)); // NOI18N
        btnTimeMode.setIcon(loadIcon("/com/mycompany/wordtype/images/TImeModeIcon_OFF.png")); // NOI18N
        btnTimeMode.setText("time");
        btnTimeMode.setIconTextGap(8);
        btnTimeMode.setSelectedIcon(loadIcon("/com/mycompany/wordtype/images/TImeModeIcon_ON.png")); // NOI18N
        btnTimeMode.addActionListener(this::btnTimeModeActionPerformed);
        jPanel1.add(btnTimeMode);
        btnTimeMode.setBounds(313, 150, 110, 38);

        buttonGroup1.add(btnWordsMode);
        btnWordsMode.setFont(new java.awt.Font("Roboto Mono", 1, 15)); // NOI18N
        btnWordsMode.setText("A words");
        btnWordsMode.addActionListener(this::btnWordsModeActionPerformed);
        jPanel1.add(btnWordsMode);
        btnWordsMode.setBounds(433, 150, 120, 38);

        buttonGroup2.add(btnTime120);
        btnTime120.setFont(new java.awt.Font("Roboto Mono", 0, 15)); // NOI18N
        btnTime120.setText("120");
        btnTime120.addActionListener(this::btnTime120ActionPerformed);
        jPanel1.add(btnTime120);
        btnTime120.setBounds(808, 150, 80, 38);

        buttonGroup2.add(btnTime15);
        btnTime15.setFont(new java.awt.Font("Roboto Mono", 0, 15)); // NOI18N
        btnTime15.setText("15");
        btnTime15.addActionListener(this::btnTime15ActionPerformed);
        jPanel1.add(btnTime15);
        btnTime15.setBounds(583, 150, 65, 38);

        buttonGroup2.add(btnTime30);
        btnTime30.setFont(new java.awt.Font("Roboto Mono", 0, 15)); // NOI18N
        btnTime30.setText("30");
        btnTime30.addActionListener(this::btnTime30ActionPerformed);
        jPanel1.add(btnTime30);
        btnTime30.setBounds(658, 150, 65, 38);

        buttonGroup2.add(btnTime60);
        btnTime60.setFont(new java.awt.Font("Roboto Mono", 0, 15)); // NOI18N
        btnTime60.setText("60");
        btnTime60.addActionListener(this::btnTime60ActionPerformed);
        jPanel1.add(btnTime60);
        btnTime60.setBounds(733, 150, 65, 38);

        Typeping.setBorder(null);
        Typeping.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TextTypeing.setEditable(false);
        TextTypeing.setBackground(new java.awt.Color(225, 225, 227));
        TextTypeing.setBorder(null);
        TextTypeing.setFont(new java.awt.Font("Menlo", 0, 36)); // NOI18N
        TextTypeing.setForeground(new java.awt.Color(150, 150, 150));
        Typeping.setViewportView(TextTypeing);

        jPanel1.add(Typeping);
        Typeping.setBounds(150, 280, 900, 130);

        btnRestart.setIcon(loadIcon("/com/mycompany/wordtype/images/btnRestart.png")); // NOI18N
        btnRestart.setBorderPainted(false);
        btnRestart.setContentAreaFilled(false);
        btnRestart.setFocusPainted(false);
        jPanel1.add(btnRestart);
        btnRestart.setBounds(588, 465, 24, 24);

        jLabel5.setFont(new java.awt.Font("Roboto Mono", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("<html><font color=\"#00675B\">[ ESC ]</font> <font color=\"#999999\">- restart test</font></html>");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(150, 644, 900, 20);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 1200, 700);

        setSize(new java.awt.Dimension(1200, 728));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserActionPerformed
        if (onUserButtonClick != null) onUserButtonClick.run();
    }//GEN-LAST:event_btnUserActionPerformed

    private void btnTimeModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimeModeActionPerformed
        // TODO add your handling code here:
        btnTime15.setText("15");
        btnTime30.setText("30");
        btnTime60.setText("60");
        btnTime120.setText("120");
    }//GEN-LAST:event_btnTimeModeActionPerformed

    private void btnWordsModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWordsModeActionPerformed
        // TODO add your handling code here:
        btnTime15.setText("10");
        btnTime30.setText("25");
        btnTime60.setText("50");
        btnTime120.setText("100");
    }//GEN-LAST:event_btnWordsModeActionPerformed

    private void btnTime120ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTime120ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTime120ActionPerformed

    private void btnTime15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTime15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTime15ActionPerformed

    private void btnTime30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTime30ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTime30ActionPerformed

    private void btnTime60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTime60ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTime60ActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Home().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane TextTypeing;
    private javax.swing.JScrollPane Typeping;
    private javax.swing.JButton btnLeader;
    private javax.swing.JButton btnRestart;
    private javax.swing.JToggleButton btnTime120;
    private javax.swing.JToggleButton btnTime15;
    private javax.swing.JToggleButton btnTime30;
    private javax.swing.JToggleButton btnTime60;
    private javax.swing.JToggleButton btnTimeMode;
    private javax.swing.JButton btnUser;
    private javax.swing.JToggleButton btnWordsMode;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
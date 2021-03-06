
package iqq.ui;

import iqq.comm.Auth;
import iqq.model.Group;
import iqq.model.Member;
import iqq.model.Message;
import iqq.service.MemberService;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;


/**
 *
 * @author chenzhihui
 */
public class NewMessageFrame extends javax.swing.JFrame {

    private MainFrame mainFrame = MainFrame.getMainFrame();
    private Map map = mainFrame.getMap();
    private MainPanel mainPanel = (MainPanel) map.get("mainPanel");
    private Map messagePrompt = new HashMap();
    private int defaultX;
    private int defaultY;
    private int defaultWidth;

    /**
     * Creates new form NewMessageFrame
     */
    public NewMessageFrame() {
        initComponents();
        this.setIconImage(Auth.getMember().getFace().getImage());
        int w = Toolkit.getDefaultToolkit().getScreenSize().width;
        Integer sort = (Integer)map.get("loginCount");
        defaultY = sort  * 50;
        defaultX = w - this.getWidth();
        defaultWidth = this.getWidth();
        this.setBounds(defaultX, defaultY, defaultWidth, 30);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        messageToolBar = new javax.swing.JToolBar();
        btnNewMessage = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("新信息来了...");
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusCycleRoot(false);
        setFocusable(false);
        setFocusableWindowState(false);
        setUndecorated(true);

        messageToolBar.setFloatable(false);
        messageToolBar.setRollover(true);
        messageToolBar.setAutoscrolls(true);
        messageToolBar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        messageToolBar.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        messageToolBar.setOpaque(false);

        btnNewMessage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iqq/res/images/icon/minilogo.png"))); // NOI18N
        btnNewMessage.setToolTipText("菜单");
        btnNewMessage.setFocusable(false);
        btnNewMessage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewMessage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        messageToolBar.add(btnNewMessage);
        messageToolBar.add(jSeparator1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 64, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(messageToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(1, 1, 1)
                    .addComponent(messageToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(2, 2, 2)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewMessageFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewMessageFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewMessageFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewMessageFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                }
                new NewMessageFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewMessage;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar messageToolBar;
    // End of variables declaration//GEN-END:variables

    public void addMessagePrompt(final List<Message> msgList) throws Exception {
        //Log.println("addMessagePrompt");
        if (msgList == null) {
            return;
        }

        Message message = msgList.get(0);
        Member member = null;
        Group group = null;
        long id = -2;
        ImageIcon imageIcon = null;
        String name = null;
        if (message.getMember() != null) {
            member = message.getMember();
            id = member.getUin();
            imageIcon = member.getFace();
            name = member.toString();
        } else if (message.getGroup() != null) {
            group = message.getGroup();
            id = group.getId();
            imageIcon = group.getFace();
            name = group.getName();
        } else {
            return;
        }
        String key = "messagePrompt" + id;
        //Log.println(key);
        if (member != null) {
            //download the member infomation and face.
            member.setFace(MemberService.getInstance().downloadFace(member));
            if (member.getStat() <= 0 && member.getClient_type() <= 0) {
                try {
                    //下载个人信息
                    member = MemberService.getInstance().getMemberInfo(member);
                } catch (Exception ex) {
                    Logger.getLogger(NewMessageFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        Icon icon = new ImageIcon(imageIcon.getImage().getScaledInstance(16, 16, imageIcon.getImage().SCALE_DEFAULT));
        JButton btnPrompt = null;
        btnPrompt = (JButton) messagePrompt.get(key);
        if (btnPrompt != null) {
            btnPrompt.setToolTipText(name + ":" + msgList.size() + " 条新信息");
            return;
        } else {
            btnPrompt = new JButton();
            btnPrompt.setToolTipText(1 + " 条新信息");
            btnPrompt.setIcon(icon);
            messagePrompt.put(key, btnPrompt);
        }

        this.messageToolBar.add(btnPrompt);
        setFrameWidth();

        btnPrompt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Log.println("btnPrompt clicked!");
                if (msgList == null || msgList.isEmpty()) {
                    return;
                }
                ChatDialog c = (ChatDialog) map.get("chatDialog");
                if (c == null) {
                    c = new ChatDialog(mainFrame, false);
                    map.put("chatDialog", c);
                }
                try {
                    for (Message message : msgList) {
                        Member member = null;
                        Group group = null;
                        long id = -2;
                        if (message.getMember() != null) {
                            member = message.getMember();
                            id = member.getUin();
                        } else if (message.getGroup() != null) {
                            group = message.getGroup();
                            id = group.getId();
                        } else {
                            return;
                        }
                        String key = "messagePrompt" + id;
                        JButton btnPrompt = (JButton) messagePrompt.get(key);
                        if (btnPrompt != null) {
                            messageToolBar.remove(btnPrompt);
                            messageToolBar.validate();
                            messageToolBar.repaint();
                            messagePrompt.remove(key);
                            mainPanel.removeButton(key);
                        }

                        c.addChat(message);
                    }

                } catch (Exception ex) {
                    Logger.getLogger(NewMessageFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        this.setVisible(true);
        newMessage();
    }
    Thread t = null;

    private void newMessage() {
        if (t != null) {
            return;
        }
        t = new Thread(new Runnable() {

            @Override
            public void run() {
                //Icon defIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(this.getClass().getClassLoader().getResource("iqq/res/images/icon/minilogo.png")));
                Icon defIcon = new ImageIcon(Auth.getMember().getFace().getImage().getScaledInstance(16, 16, NORMAL));
                Icon flashIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(this.getClass().getClassLoader().getResource("iqq/res/images/icon/flash.gif")));
                Icon[] icons = new Icon[]{defIcon, flashIcon};

                int current = 0;
                while (true) {
                    if (messagePrompt.isEmpty()) {
                        stopFlash(this);
                        btnNewMessage.setIcon(icons[0]);
                    }
                    btnNewMessage.setIcon(icons[current]);
                    current++;
                    if (current == icons.length) {
                        current = 0;
                    }
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        });

        if (t != null) {
            //t.start();
            t.start();
        }
    }

    private synchronized void stopFlash(Runnable r) {
        this.setVisible(false);
        Rectangle rectangle = new Rectangle(this.defaultX, defaultY, this.defaultWidth, 30);
        this.setBounds(rectangle);
    }

    public void removeButton(String key) {
        JButton btnPrompt = (JButton) messagePrompt.get(key);
        if (btnPrompt != null) {
            messageToolBar.remove(btnPrompt);
            messageToolBar.validate();
            messageToolBar.repaint();
            messagePrompt.remove(key);
        }
    }

    public void setFrameWidth() {
        int w = Toolkit.getDefaultToolkit().getScreenSize().width;
        Rectangle rectangle = new Rectangle(w - this.getWidth() - 28, defaultY, this.getWidth() + 28, 30);
        //this.messageToolBar.setBounds(r);
        this.setBounds(rectangle);
    }
}

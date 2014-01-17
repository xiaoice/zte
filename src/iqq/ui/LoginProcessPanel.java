
package iqq.ui;

import iqq.comm.Auth;
import iqq.model.Category;
import iqq.model.Member;
import iqq.service.CategoryService;
import iqq.service.GroupService;
import iqq.service.LoginService;
import iqq.service.MemberService;
import iqq.util.*;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author chenzhihui
 */
public class LoginProcessPanel extends javax.swing.JPanel {

    private MainFrame mainFrame = MainFrame.getMainFrame();
    private Map map = mainFrame.getMap();
    private LoginService loginService = LoginService.newInstance();
    private MemberService memberService = MemberService.getInstance();
    private CategoryService categoryService = CategoryService.getInstance();
    private Member member = null;
    private List<Category> categoryList = null;
    private boolean isCancel = false;

    /**
     * Creates new form LoginPanel
     */
    public LoginProcessPanel() {
        ResourceBundle bundle = ResourceBundle.getBundle("iqq/res/res"); // NOI18N
        MainFrame.getMainFrame().setIBackground(bundle.getString("iqq.loginProcessBg"));
        initComponents();
        this.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblProcess = new JLabel("请稍后...", SwingConstants.CENTER);

        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(255, 555));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("iqq/res/res"); // NOI18N
        btnCancel.setText(bundle.getString("iqq.cancel")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iqq/res/images/process.gif"))); // NOI18N

        lblProcess.setText("请稍后...");
        lblProcess.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                .addGap(97, 97, 97))
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProcess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(216, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblProcess)
                .addGap(31, 31, 31)
                .addComponent(btnCancel)
                .addGap(226, 226, 226))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        cancel();
    }//GEN-LAST:event_btnCancelActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblProcess;
    // End of variables declaration//GEN-END:variables

    public void login() throws Exception {
        Log.println("loginProcess to mainPanel");

        Runnable toLoginRunnable = new Runnable() {

            @Override
            public void run() {
                Map loginMap = (Map) map.get("loginMap");
                boolean isLogin = loginService.login(loginMap);
                loginStatus(isLogin);
                
            }
        };
        ThreadUtil.submit(toLoginRunnable);
        Log.println("loginProcess to mainPanel 2");
    }

    void loginStatus(boolean isLogin) {

        Runnable loginRunnable = new Runnable() {

            @Override
            public void run() {
                if(isCancel) {
                    return ;
                }
                List<Member> onlineFriends = null;
                try {
                    lblProcess.setText("正在下载用户信息...");
                    member = memberService.getMemberInfo(Auth.getMember());
                    lblProcess.setText("正在下载用户头像...");
                    member.setFace(memberService.getFace());
                    lblProcess.setText("正在加载好友列表...");
                    categoryList = categoryService.getFriends();
                    lblProcess.setText("正在加载在线好友...");
                    onlineFriends = categoryService.getOnlineFriends();
                } catch (Exception ex) {
                    Logger.getLogger(LoginProcessPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                //下载群列表到缓存，等待加载...
                lblProcess.setText("正在加载群组列表...");
                GroupService.getInstance().downloadGroupList();
                Log.println("** " + categoryList.size());
                
                
                
                lblProcess.setText("正初始化主界面...");
                MainPanel mainPanel = new MainPanel();
                map.put("mainPanel", mainPanel);
                //map.put("loginProcessPanel", this);
                map.put("member", member);
                map.put("categoryList", categoryList);
                map.put("onlineFriends", onlineFriends);
                try {
                    mainPanel.initQQ();
                } catch (Exception ex) {
                    Logger.getLogger(LoginProcessPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                mainFrame.setContentPane(mainPanel);
                mainFrame.validate();

            }
        };

        if (isLogin) {
            map.remove("loginPanel");
            ThreadUtil.submit(loginRunnable);
            int loginCount = FileLockUtil.getFileLockCount();
            map.put("loginCount", loginCount);
            Map loginMap = (Map) map.get("loginMap");
            boolean remember = (Boolean) loginMap.get("rememberPassword");
            if (remember) {
                PersistenceUtils.getInstance().update(loginMap);
            } else {
                loginMap.put("password", "");
                PersistenceUtils.getInstance().update(loginMap);
            }
        } else {
            ErrorMessage.show("登录失败，请检查你的账号和密码；或者你所在的网络状况。");
            cancel();
        }
    }

    void cancel() {
        isCancel = true;
        FileLockUtil.releaseFileLock();
        ThreadUtil.shutdown();
        JPanel p = (JPanel) map.get("loginPanel");
        if (p != null) {
            mainFrame.setContentPane(p);
        } else {
            mainFrame.setContentPane(new LoginPanel());
        }
        CategoryService.setCategoryList(null);
        mainFrame.getMap().clear();
        mainFrame.setMap(null);
        mainFrame.validate();
        mainFrame.repaint();
    }
}

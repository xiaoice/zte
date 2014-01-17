
package iqq.ui;

import iqq.model.Message;
import iqq.util.ChatHistoryUtils;
import java.io.RandomAccessFile;

/**
 *
 * @author chenzhihui
 */
public class BaseChatPanel extends javax.swing.JPanel {

    private long id;
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public StringBuilder getMessages() {
        return null;
    }

    public void setMessages(Message message) {
        
    }   
}

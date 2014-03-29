
package iqq.model;

import iqq.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

/**
 *
 * @author chenzhihui
 */
public class Message {

    private long id;
    private Member member;
    private Group group;
    private HTMLDocument message;
    private Date createDate;
    private boolean isRead;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public HTMLDocument getMessage() {
        return message;
    }

    public void setMessage(HTMLDocument message) {
        this.message = message;
    }

    @Override
    public String toString() {
        Member m = null;
        Group g = null;
        long id = -2;
        String name = null;
        if (this.getMember() != null) {
            m = this.getMember();
            id = member.getUin();
            name = member.getNickname();
        } else if (this.getGroup() != null) {
            g = this.getGroup();
            id = group.getId();
            if (group.getMember() != null) {
                name = group.getMember().getNickname();
            }
        } else {
            return null;
        }

        if (this.getCreateDate() == null || this.getMessage() == null) {
            return "";
        }

        StringBuilder messages = new StringBuilder();

        StringBuilder info = new StringBuilder();
        info.append("&nbsp;&nbsp;<font color='green'>");
        info.append(name);
        info.append("</font> ");
        String dateStr = sdf.format(getCreateDate());
        info.append(dateStr);

        StringBuilder msg = new StringBuilder();
        msg.append("<div style='margin:5px;margin-bottom:8px'>");
        String text = null;
        try {
            text = getMessage().getText(0, getMessage().getLength());
        } catch (BadLocationException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        Log.println(text);
        
        msg.append(text);
        msg.append("</div>");

        messages.append(info);

        messages.append(msg);

        return messages.toString();
    }
}

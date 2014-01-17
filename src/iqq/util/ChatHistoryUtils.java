package iqq.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author savior create at 2013-02-16 14:38:51 by savior content me by send
 * email to michael.savior@gmail.com. encoding=UTF-8
 *
 */
public class ChatHistoryUtils {

    public static void appendToEnd(RandomAccessFile randomFile, String str) {
        //data 的数据是html 不关心格式，值取body 中的部分。       



        String data = str.substring(str.indexOf("<body>") + 6, str.length());

        data = data.substring(0, data.indexOf("</body>") - 3);
        try {

            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(data);

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static void appendToEnd(String uid, String msg) {
        try {
            RandomAccessFile randomFile = new RandomAccessFile(QQEnvironment.getChatHistroyDir() + uid, "rw");
            String data = msg.substring(msg.indexOf("<body>") + 6, msg.length());

            data = data.substring(0, data.indexOf("</body>") - 3);
            
            
             long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(data);
            randomFile.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ChatHistoryUtils.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}

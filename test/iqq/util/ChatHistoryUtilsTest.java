

package iqq.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author savior
 * create at 2013-02-16 14:41:58 by savior
 * content me by send email to michael.savior@gmail.com.
 * encoding=UTF-8 
 *
 */
public class ChatHistoryUtilsTest {
    
    
    
    
    public static  void test() throws FileNotFoundException, IOException{
        File file = new File("/home/savior/.iQQ/515827368/chatHistory/274674434");
        BufferedReader br = new BufferedReader(new FileReader(file));
        
        
        
        StringBuffer sb = new StringBuffer();
        String s = null;
        while((s = br.readLine())!= null){
            sb.append(s).append("\n");
        }
        
        String str = sb.toString();
        System.out.println("str = " + str);
//        Pattern p = Pattern.compile("ha");
//        Matcher m = p.matcher(str);
//        if(m.matches()){
//            System.out.println(" m.group= " + m.group(1));
//        }
        //System.out.println("str = " + str);
        
        
        
        String ss = str.substring(str.indexOf("<body>")+6,str.length());
        
        ss  = ss.substring(0,ss.indexOf("</body>")-3);
        System.out.println("ss = " + ss);
        
        
    }
    
    public static void main(String[] args) throws Exception{
        test();
    }
    
}

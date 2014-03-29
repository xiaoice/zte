
package iqq.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author chenzhihui
 */
public class Log {
    // 记录日志
    public static void println(Object msg) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " : " + msg);
    }
}

import com.abcodeworks.webshortcututil.read.ShortcutReadUtil;
import java.io.File;

// javac -classpath webshortcututil-1.0.1.jar test.java
// java -cp .:./webshortcututil-1.0.1.jar test www.ÖÐ¹úÕþ¸®.ÕþÎñ.cn.url

public class test {
    public static void main(String[] args)
    throws Exception {
        File file = new File(args[0]);
        String url = ShortcutReadUtil.readUrlString(file);
        System.out.println(url);
    }
}

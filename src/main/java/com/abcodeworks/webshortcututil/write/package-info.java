
/**
 * Provides classes for creating web shortcut files
 *
 * <p>
 * To create a shortcut you first need to instantiate the appropriate writer class (
 * {@link com.abcodeworks.webshortcututil.write.DesktopShortcutWriter DesktopShortcutReader}
 * {@link com.abcodeworks.webshortcututil.write.UrlShortcutWriter UrlShortcutReader},
 * {@link com.abcodeworks.webshortcututil.write.WeblocBinaryShortcutWriter WeblocBinaryShortcutWriter},
 * or {@link com.abcodeworks.webshortcututil.write.WeblocXmlShortcutWriter WeblocXmlShortcutWriter}).
 * If you want to create a file, call the
 * {@link com.abcodeworks.webshortcututil.write.ShortcutWriter#write(File, String, String) write}
 * method.  You can use the
 * {@link com.abcodeworks.webshortcututil.write.ShortcutWriter#createFilename(String) createFilename}
 * method to generate a file name.  It takes the provided base file name, adds the appropriate extension,
 * and removes any special characters in the name.  The generated file name should be compatible with most
 * operating systems.  If you need to write the shortcut to a stream, use the 
 * {@link com.abcodeworks.webshortcututil.write.ShortcutWriter#write(OutputStream, String, String) write} method.
 * A usage example follows:
 * <pre>
 * {@code
 * import com.abcodeworks.webshortcututil.write.ShortcutWriter;
 * import com.abcodeworks.webshortcututil.write.DesktopShortcutWriter;
 * import com.abcodeworks.webshortcututil.write.UrlShortcutWriter;
 * import com.abcodeworks.webshortcututil.write.WeblocBinaryShortcutWriter;
 * import com.abcodeworks.webshortcututil.write.WeblocXmlShortcutWriter;
 * 
 * ShortcutWriter writer = new DesktopShortcutWriter();
 *    OR
 * ShortcutWriter writer = new UrlShortcutWriter();
 *    OR
 * ShortcutWriter writer = new WeblocBinaryShortcutWriter();
 *    OR
 * ShortcutWriter writer = new WeblocXmlShortcutWriter();
 * 
 * String name = "My Shortcut";
 * String url = "http://myurl.com/";
 * 
 * String filename = writer.createFilename(name);
 * File file = new File(path, filename);
 * writer.write(file, name, url); 
 * }
 * </pre>
 * </p>
 * 
 * <p>
 * Note that you have two options when creating a Webloc shortcut:
 * {@link com.abcodeworks.webshortcututil.write.WeblocBinaryShortcutWriter binary} or 
 * {@link com.abcodeworks.webshortcututil.write.WeblocXmlShortcutWriter XML}
 * format.  They both are
 * recognized by OSX.  Also note that there is no way to create a Website shortcut.  This is due to the
 * complexity of the file format.  If you want to generate a Windows-compatible shortcut,
 * use {@link com.abcodeworks.webshortcututil.write.UrlShortcutWriter UrlShortcutReader}.
 * </p>
 * 
 * <p>
 * The methods that write to streams will automatically close the streams when finished with them.
 * </p>
 *
 */

package com.abcodeworks.webshortcututil.write;
import java.io.File;
import java.io.OutputStream;


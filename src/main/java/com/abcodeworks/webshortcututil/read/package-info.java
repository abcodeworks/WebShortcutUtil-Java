
/**
 * Provides classes for reading web shortcut files
 *
 * <p>
 * Ideally, you should have direct access to the shortcut file.
 * In this case, the preferred way to read files is to use the 
 * {@link com.abcodeworks.webshortcututil.read.ShortcutReadUtil#read read} or
 * {@link com.abcodeworks.webshortcututil.read.ShortcutReadUtil#readUrlString readUrlString}
 * methods in the 
 * {@link com.abcodeworks.webshortcututil.read.ShortcutReadUtil ShortcutReadUtil} class.
 * Note that these methods determine the type of shortcut by looking at the file extension
 * in the file name, so they will not work properly if the extension is incorrect.
 * A usage example follows:
 * <pre>
 * {@code
 * import com.abcodeworks.webshortcututil.read.ShortcutReadUtil;
 * 
 * File myFile = new File("shortcut.desktop");
 * 
 * String url = ShortcutReadUtil.readUrlString(myFile);
 * 
 * ShortcutContents contents = ShortcutReadUtil.read(myFile);
 * String name = contents.getName();
 * String url = contents.getUrlString();
 * }
 * </pre>
 * </p>
 * 
 * <p>
 * Note that you can retrieve the name/title of the shortcut.  The name returned by the readers is a guess.  Many shortcut files
 * do not contain a name/title embedded in the file.  ".desktop" shortcuts
 * may contain several embedded names with different encodings.  Unfortunately,
 * these names are not necessarily updated when the shortcut is renamed.
 * It is difficult, if not impossible, to determine which is the correct name.
 * As of right now, the reader will always return the name of the file as the
 * name of the shortcut, although this may change in the future.
 * </p>
 * 
 * <p>
 * Methods are also provided for retrieving the appropriate reader class for a given file or to
 * check if a file has a valid extension (see
 * {@link com.abcodeworks.webshortcututil.read.ShortcutReadUtil#getShortcutReader ShortcutReadUtil.getShortcutReader} and
 * {@link com.abcodeworks.webshortcututil.read.ShortcutReadUtil#hasValidExtension ShortcutReadUtil.hasValidExtension}).
 * </p>
 * 
 * <p>
 * If you are reading the shortcut from a stream and know the type of shortcut, then you
 * should instantiate the appropriate reader class (
 * {@link com.abcodeworks.webshortcututil.read.DesktopShortcutReader DesktopShortcutReader}
 * {@link com.abcodeworks.webshortcututil.read.UrlShortcutReader UrlShortcutReader},
 * {@link com.abcodeworks.webshortcututil.read.WebsiteShortcutReader WebsiteShortcutReader},
 * or {@link com.abcodeworks.webshortcututil.read.WeblocShortcutReader WeblocShortcutReader})
 * and then call the {@link com.abcodeworks.webshortcututil.read.ShortcutReader#readUrlString readUrlString} method.
 * A usage example follows:
 *  <pre>
 * {@code
 * import com.abcodeworks.webshortcututil.read.ShortcutReader;
 * import com.abcodeworks.webshortcututil.read.DesktopShortcutReader;
 * import com.abcodeworks.webshortcututil.read.UrlShortcutReader;
 * import com.abcodeworks.webshortcututil.read.WebsiteShortcutReader;
 * import com.abcodeworks.webshortcututil.read.WeblocShortcutReader;
 * 
 * ShortcutReader reader = new DesktopShortcutReader();
 *   OR
 * ShortcutReader reader = new UrlShortcutReader();
 *   OR
 * ShortcutReader reader = new WebsiteShortcutReader();
 *   OR
 * ShortcutReader reader = new WeblocShortcutReader();
 * 
 * String url = reader.readUrlString(inputStream);
 * }
 * </pre>
 * </p>
 * 
 * <p>
 * If you are reading the shortcut from a stream but do not know the type of shortcut, then
 * you must use the
 * {@link com.abcodeworks.webshortcututil.read.ShortcutReadUtil#readUrlStringTrialAndError(InputStream) ShortcutReadUtil.readUrlStringTrialAndError}
 * method.  As the name implies, this method tries reading the stream as each type of shortcut
 * to see which one works.  Due to buffering limitations, there is a small chance that this method will not work.
 * A usage example follows:
 *  <pre>
 * {@code
 * import com.abcodeworks.webshortcututil.read.ShortcutReadUtil;
 * 
 * String url = ShortcutReadUtil.readUrlStringTrialAndError(inputStream);
 * }
 * </pre>
 * </p>
 * 
 * <p>
 * The methods that read from streams do not close the streams (this is the responsibility of the caller).
 * </p>
 * 
 */

package com.abcodeworks.webshortcututil.read;

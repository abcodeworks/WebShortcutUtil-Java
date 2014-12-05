/**
 * Copyright 2014 by Andre Beckus
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.abcodeworks.webshortcututil.read;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Provides helper functions for reading shortcuts.
 * 
 * <p>
 * This is the preferred class to use for reading shortcuts, because
 * it automatically figures out which shortcut reader class to use.  If required,
 * you may also instantiate the specific reader classes and use them directly.
 * </p>
 * 
 * <p>
 * There is no need to instantiate this class - all methods are static.
 * </p>
 * 
 */

public class ShortcutReadUtil {
    /** 
     * The default buffer size used by {@link #readUrlStringTrialAndError(InputStream)}.
     */
    public static final int DEFAULT_TRIAL_AND_ERROR_BUFFER_SIZE = 8192;
    
    /**
     * Returns a shortcut reader class appropriate for reading the specified file (based on the extension).
     * 
     * @param file The shortcut file.
     * @return The appropriate reader class, or null if the file does not have a valid shortcut extension.
     */
    public static ShortcutReader getShortcutReader(File file) {
        String filename = file.getName();
        
        // Find the last dot (the one right before the extension).
        int last_dot = filename.lastIndexOf('.');
        if(last_dot == -1) {
            return null;
        }

        // Get the extension start position and make sure the extension actually exists.
        int extension_start = last_dot + 1;
        if((extension_start + 1) > filename.length()) {
            return null;
        }

        // Extract the extension and get the appropriate reader class.
        String extension = filename.substring(last_dot + 1);
        if(extension.equalsIgnoreCase("url")) {
            return new UrlShortcutReader();
        } else if(extension.equalsIgnoreCase("website")) {
            return new WebsiteShortcutReader();
        } else if(extension.equalsIgnoreCase("webloc")) {
            return new WeblocShortcutReader();
        } else if(extension.equalsIgnoreCase("desktop")) {
            return new DesktopShortcutReader();
        }

        return null;
    }
    
    /**
     * Checks to see if the file is a valid shortcut (based on the extension).
     * 
     * @param file The shortcut file.
     * @return True if the file extension indicates the file is a shortcut.
     */
    public static boolean hasValidExtension(File file) {
        // Let getShortcutReader decide whether the file is valid
        return getShortcutReader(file) != null;
    }
    
    /**
     * Reads the specified file and extracts the contents.  The type of
     * shortcut file is determined by the file extension.
     *
     * For ".desktop" and ".url" files, the reader can handle unicode characters
     * in the name and URL.  ".webloc" files may contain unicode characters as well,
     * although this functionality still requires more testing.
     *
     * The name returned by the readers is a guess.  Many shortcut files
     * do not contain a name/title embedded in the file.  ".desktop" shortcuts
     * may contain several embedded names with different encodings.  Unfortunately,
     * these names are not necessarily updated when the shortcut is renamed.
     * It is difficult, if not impossible, to determine which is the correct name.
     * As of right now, the reader will always return the name of the file as the
     * name of the shortcut, although this may change in the future.
     *
     * @param file The file to read
     * @return A ShortcutContents class containing the name and URL of the shortcut.  Non-null.  The name and URL will be non-null as well.
     * @throws FileNotFoundException
     * @throws ShortcutReadException If any error occurs while reading the file.
     */
    public static ShortcutContents read(File file)
            throws FileNotFoundException,
                   ShortcutReadException {
        ShortcutReader reader = getShortcutReader(file);
        if(reader == null) {
            throw new ShortcutReadException("Invalid file extension");
        }
        return reader.read(file);
    }

    /**
     * Similar to {@link #read(File)}, but only returns the URL.
     *
     * @param file The file to read
     * @return The URL as a string.  Non-null.
     * @throws FileNotFoundException
     * @throws ShortcutReadException If any error occurs while reading the file.
     */
    public static String readUrlString(File file)
            throws FileNotFoundException,
                   ShortcutReadException {
        return read(file).getUrlString();
    }
    
    /* Attempts to read from the buffered stream using the specified reader.  If the reader throws
     * an exception then reset the stream for the next attempt.
     * If we cannot reset the buffer, assume the reader read too many characters and throw an exception.
     */
    protected static String readUrlStringAttempt(ShortcutReader reader, BufferedInputStream bufferedStream, int bufferSize)
            throws ShortcutReadException {
        try {
            bufferedStream.mark(bufferSize);
            return reader.readUrlString(bufferedStream);
        } catch(ShortcutReadException sre) {
            // The reader failed: reset the stream
            try {
                bufferedStream.reset();
                return null;
            } catch(IOException ioe) {
                throw new ShortcutReadException("Error resetting input stream - buffer size is too small", ioe);
            }
        // Default handler for any unexpected problems.
        } catch(Exception e) {
            throw new ShortcutReadException("Error when trying to read shortcut", e);
        }
    }

    /**
     * Reads a shortcut of unknown type from the specified stream.
     *
     * <p>
     * Because no file is available, here is no way to determine the shortcut type based on the extension.
     * As the name implies, this method tries reading the stream as each type of shortcut
     * to see which one works.
     * </p>
     * 
     * <p>
     * Because the input stream can only be read one time, this method uses a buffer to allow the 
     * stream contents to be repeatedly read.  The default buffer size is indicated by {@link #DEFAULT_TRIAL_AND_ERROR_BUFFER_SIZE}.
     * Due to the limited buffer size, there is a small chance that this method will not work.
     * The buffer size may be increased in size by using {@link #readUrlStringTrialAndError(InputStream, int)}.
     * </p>
     *
     * @param stream The stream to read.  The stream is not closed.
     * @return The URL as a string.  Non-null.
     * @throws FileNotFoundException
     * @throws ShortcutReadException If the shortcut type cannot be determined, or if any error occurs while reading the file.
     */
    public static String readUrlStringTrialAndError(InputStream stream)
            throws ShortcutReadException {
        return readUrlStringTrialAndError(stream, DEFAULT_TRIAL_AND_ERROR_BUFFER_SIZE);
    }

    
    /**
     * Reads a shortcut of unknown type from the specified stream.  See {@link #readUrlStringTrialAndError(InputStream)} for details.
     * 
     * @param stream The stream to read.  The stream is not closed.
     * @param bufferSize The buffer size to use when reading the stream.
     * @return The URL as a string.  Non-null.
     * @throws ShortcutReadException If the shortcut type cannot be determined, or if any error occurs while reading the file.
     */
    public static String readUrlStringTrialAndError(InputStream stream, int bufferSize)
            throws ShortcutReadException {
        String url = null;

        // Create a buffer so that we can make multiple read attempts
        BufferedInputStream bufferedStream = new BufferedInputStream(stream, bufferSize);
        assert(bufferedStream.markSupported());
        
        // Attempt reading the file as a .url
        url = readUrlStringAttempt(new UrlShortcutReader(), bufferedStream, bufferSize);
        if(url != null) {
            return url;
        }
        // We will not attempt to read the stream as a WEBSITE shortcut - the URL and WEBSITE implementations
        // are the same.
        
       // Attempt reading the file as a .desktop
        url = readUrlStringAttempt(new DesktopShortcutReader(), bufferedStream, bufferSize);
        if(url != null) {
            return url;
        }
        
        // Attempt reading the file as a .webloc.
        // We leave this for last since the plist library closes the stream.
        // This means this method is not working as specified - consider it a bug.
        // It looks like this will be changed in a future version of the library.
        // The latest version loads the entire shortcut into memory, so we probably
        // should still leave the webloc for last.
        // Once the new version is posted to the Maven repository, we should be able
        // to start using it...
        url = readUrlStringAttempt(new WeblocShortcutReader(), bufferedStream, bufferSize);
        if(url != null) {
            return url;
        }
        
        throw new ShortcutReadException("The shortcut is not recognized as a known type");
    }
}

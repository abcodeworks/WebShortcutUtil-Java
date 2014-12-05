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

package com.abcodeworks.webshortcututil.write;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parent class for writers.
 * 
 */
public abstract class ShortcutWriter {
        /**
         * Default maximum file name length used by the create filename methods.
         */
        public final int DEFAULT_MAX_FILENAME_LENGTH = 100;
        
        /* Matches characters that are invalid for file names.
         * This works by matching characters that are not in a list of valid characters.
         * Note that unicode characters are all considered valid. 
         */
        Pattern invalidCharPattern = Pattern.compile("[^ !#\\$&'\\(\\)+,\\-\\.,0-9;=\\@A-Z\\[\\]_`a-z\\{\\}~\u0080-\uFFFF]");
    
        /**
         * Indicates the standard extension used for the writer's shortcut type.  
         * 
         * @return The extension, without a period (e.g. "url", "desktop", "webloc").  Non-null.
         */
        abstract public String defaultExtension();
    
        /**
         * Similar to {@link #write(File, String, String)} but writes to a stream instead of a file.
         * 
         * @param stream The stream to write to.  The stream is automatically closed when finished writing.
         * @param name The name of the shortcut.  This may be ignored depending on the shortcut type.
         * @param url The URL.
         * @throws ShortcutWriteException If any error occurs while writing to the stream.
         */
        abstract public void write(OutputStream stream, String name, String url)
            throws ShortcutWriteException;
        
        /**
         * Creates a file name based on the specified shortcut name.
         * The goal is to allow the file to be stored on a wide variety
         * of operating systems without issues (however, there are no guarantees
         * that the file name will work on all operating systems).  The following rules are used:
         * 
         * <ul>
         * <li>An appropriate extension is added based on the shortcut type (e.g. ".url").</li>
         * <li>Removes characters which are prohibited in some file systems (such as "?" and ":").
         * Note there may still be characters left that will cause difficulty,
         * such as spaces and single quotes.</li>
         * <li>If the resulting name (after removing characters) is an empty string,
         * the file will be named "_".</li>
         * <li>Unicode characters are not changed.  If there are unicode characters,
         * they could cause problems on some file systems.  If you do not
         * want unicode characters in the file name, you are responsible for
         * removing them or converting them to ASCII.</li>
         * <li>If the filename is longer than the length specified by
         * {@link #DEFAULT_MAX_FILENAME_LENGTH} (including the extension),
         * it will be truncated.  This maximum length was chosen somewhat
         * arbitrarily.  You may optionally override it by using
         * {@link #createFullFilename(String, int)}.</li>
         * </ul>
         * 
         * @param name The name to use as the base of the file name.
         * @return A valid file name with a complete extension.  Non-null.
         */
        public String createFullFilename(String name) {
            return createFullFilename(name, DEFAULT_MAX_FILENAME_LENGTH);
        }
        
        /**
         * Similar to {@link #createFullFilename(String)}, but the extension
         * is not included.  The same length rules are followed, and
         * this method returns the same base name as {@link #createFullFilename(String)}
         * 
         * @param name The name to use as the base of the file name.
         * @return A valid file name without an extension.  Non-null.
         */
        public String createBaseFilename(String name) {
            return createBaseFilename(name, DEFAULT_MAX_FILENAME_LENGTH);
        }
        
        /**
         * Creates a file name.  See {@link #createFullFilename(String)} for details.
         * 
         * @param name The name to use as the base of the file name.
         * @param maxLength The maximum length of the file name (including the extension).  Must be large enough to accommodate
         *                  the extension plus a one-character base file name.
         * @return A valid file name with a complete extension.  The size will be less than maxLength.  Non-null.
         */
        public String createFullFilename(String name, int maxLength) {
            // Get the base name and add an extension.
            return createBaseFilename(name, maxLength) + "." + defaultExtension();
        }

        /**
         * Similar to {@link #createFullFilename(String,int)}, but the extension
         * is not included.  The same length rules are followed, and
         * this method returns the same base name as {@link #createFullFilename(String,int)}
         * 
         * @param name The name to use as the base of the file name.
         * @param maxLength The maximum length of the file name (including the extension).  Must be large enough to accommodate
         *                  the extension plus a one-character base file name.
         * @return A valid file name without an extension.  When an extension (with a period) is
         *         added to the returned file name, the resulting string is guaranteed to
         *         be less than maxLength.  Non-null.
         */
        public String createBaseFilename(String name, int maxLength) {
            String extension = defaultExtension();
            String cleanName;
            
            /* Get the minimum length of a file name with the given extension.
               Check it against the supplied maxLength. */
            int minLength = extension.length() + 2;
            if(maxLength < minLength) {
                throw new IllegalArgumentException("maxLength must be greater than or equal to " + minLength);
            }
            
            if(name == null) {
                // If no name is given, just use _ as a default.
                cleanName = "_";
            } else {
                // Otherwise remove all invalid characters
                Matcher matcher = invalidCharPattern.matcher(name);
                cleanName = matcher.replaceAll("");
                
                // If the name has all invalid characters use _ as a default (as before).
                if(cleanName.equals("")) {
                    cleanName = "_";
                }
            }
            
            // Find out how long the base name can be to meet the specified maxLength
            // (when the extension is included).
            int nameMaxLength = Math.min(maxLength - extension.length() - 1, cleanName.length());
            
            // Truncate the base name
            String filename = cleanName.substring(0, nameMaxLength);
            
            return filename;
        }
        
        /**
         * Writes a shortcut file.
         * 
         * The shortcut will contain the specified name/title and URL.
         * Note that some shortcuts do not contain a name inside the file, in
         * which case the name parameter is ignored.
         * 
         * If your URL contains unicode characters, it is recommended that
         * you convert it to an ASCII-only URL
         * (see <a href="http://en.wikipedia.org/wiki/Internationalized_domain_name">http://en.wikipedia.org/wiki/Internationalized_domain_name</a> ).
         * That being said, DesktopShortcutWriter and UrlShortcutWriter
         * will write unicode URLs.  The webloc writers should as well,
         * although this functionality requires more testing.
         * 
         * @param file The file to write to. The file must not already exist.
         * @param name The name of the shortcut.  These may be ignored depending on the shortcut type.
         * @param url The URL.
         * @throws FileAlreadyExistsException
         * @throws ShortcutWriteException If any error occurs while writing the file.
         */
        public void write(File file, String name, String url)
                        throws FileAlreadyExistsException,
                               ShortcutWriteException {
            if(file.exists()) {
                throw new FileAlreadyExistsException();
            }

            // We need to open a stream since the core implementation deals with streams.
            FileOutputStream stream = null;
            try {
                stream = new FileOutputStream(file);
                write(stream, name, url);
            } catch (IOException e) {
                throw new ShortcutWriteException(e);
            } finally {
                try {
                    if(stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    throw new ShortcutWriteException(e);
                }
            }
        }
}
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads Desktop shortcuts (FreeDesktop shortcuts with a .desktop extension - used by Linux, etc.).
 *  
 */
public class DesktopShortcutReader extends ShortcutReader {

    // Pattern for a blank line (i.e. only whitespace) or a comment (e.g. #My Comment)
    Pattern blank_or_comment = Pattern.compile("^\\s*(#.*)?$");

    // Pattern a for a desktop entry header e.g. [Desktop Entry] or [KDE Desktop Entry].
    // Per the Desktop Entry specifications, [KDE Desktop Entry] was used at one time...
    Pattern desktopEntryHeader = Pattern.compile("^\\s*\\[(KDE )?Desktop Entry\\]\\s*$");
    
    @Override
    public String readUrlString(InputStream stream)
            throws ShortcutReadException {
        // Open a buffered reader for efficiency and so that we can do a reset to the start of the stream.
        BufferedReader reader;
        try {
            // Note that Desktop Entry files use UTF8
            reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new ShortcutReadException(e);
        }
        
        // Let's check and see if this is really a desktop shortcut before we start.
        // We want to fail fast, especially if we are not sure if this is a shortcut.
        // Peek at the first character
        assert(reader.markSupported());
        try {
            // We only need to read 1 character, but let's use a bigger buffer just in case.
            reader.mark(16); 
            // Assume that it is OK to cast the int return value to a char.
            char firstChar = (char)reader.read();
            // The first character should either be whitespace or should start a comment or a header
            if(firstChar != '#' && firstChar != '[' && !Character.isWhitespace(firstChar)) {
                throw new ShortcutReadException("Shortcut file is invalid");
            }
            reader.reset();
        } catch (IOException ioe) {
            throw new ShortcutReadException("Error checking first character", ioe);
        }
        
        // There should be a Desktop Entry Header first - go find it
        Boolean desktopEntryFound = false;
        while(true) {
            String nextline;

            try {
                nextline = reader.readLine();
            } catch (IOException e) {
                throw new ShortcutReadException(e);
            }
            
            if(nextline == null) {
                // If we reached the end of the file, exit
                break;
            } else if(matches(desktopEntryHeader, nextline)) {
                desktopEntryFound = true;
            } else if(matches(blank_or_comment, nextline)) {
                // Ignore this line it is OK to have blank line or comments before the header
            } else {
                // When we find a line that does not match the above criteria, stop looping.
                // This should never happen for a valid Desktop Entry file.
                break;
            }
        }

        if(!desktopEntryFound) {
            throw new ShortcutReadException("Desktop Entry group not found in desktop file");
        }
        
        String url = null;
        
        while(true) {
            String nextline;

            try {
                nextline = reader.readLine();
            } catch (IOException e) {
                throw new ShortcutReadException(e);
            }
            
            if(nextline == null) {
                // If we reached the end of the file, exit
                break;
            } else if(matches(header, nextline)) {
                // We shouldn't find any headers.  If we do, exit.
                break;
            } else if(matches(blank_or_comment, nextline)) {
                // Ignore this line
            } else {
                // If we find a Key/Value pair, check it for a URL
                Matcher key_value_matcher = key_value_pattern.matcher(nextline);
                if(key_value_matcher.matches()) {
                    assert(key_value_matcher.groupCount() == 4);
                    String key = key_value_matcher.group(1);
                    String value = key_value_matcher.group(4);
                    if(key == null) {
                        // The fact that we are here suggests that either the file
                        // is corrupt or this algorithm is incorrect.  Either way
                        // we will just ignore the issue and move on to the next line.
                    } else if(key.equals("URL")) {
                        // We find a URL - capture it and exit.
                        url = value;
                        break;
                    }
                }
            }
        }
        
        if(url == null) {
            throw new ShortcutReadException("URL not found in file");
        }
        
        return url;
    }

}

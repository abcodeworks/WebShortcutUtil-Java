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
import java.net.URLDecoder;
import java.nio.charset.CharsetDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads URL shortcuts (Windows shortcuts with a .url extension).
 * 
 */
public class UrlShortcutReader extends ShortcutReader {
    enum UrlSection {
        NO_SECTION,
        INTERNET_SHORTCUT_SECTION,
        INTERNET_SHORTCUT_W_SECTION,
        OTHER_SECTION
    }
    
    /* Patterns for the two headers we are looking for:
     * [InternetShortcut]   - An ASCII URL
     * [InternetShortcut.W] - A UTF7 URL
     */
    Pattern internet_shortcut_header = Pattern.compile("^\\s*\\[InternetShortcut\\]\\s*$"),
            internet_shortcut_w_header = Pattern.compile("^\\s*\\[InternetShortcut.W\\]\\s*$");
    
    // Pattern for a blank line (i.e. only whitespace) or a comment (e.g. ;My Comment)
    Pattern blank_or_comment = Pattern.compile("^\\s*(;.*)?$");
     
    @Override
    public String readUrlString(InputStream stream)
                throws ShortcutReadException {
        UrlSection curr_section;
        String parsed_url = null,
               parsed_urlw = null;
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        
        // Peak at the first character
        assert(reader.markSupported());
        try {
            // We only need to read 1 character, but let's use a bigger buffer just in case.
            reader.mark(16); 
            // Assume that it is OK to cast the int return value to a char.
            char firstChar = (char)reader.read();
            if(firstChar != ';'&& firstChar != '[' && !Character.isWhitespace(firstChar)) {
                throw new ShortcutReadException("Shortcut file is invalid");
            }
            reader.reset();
        } catch (IOException ioe) {
            throw new ShortcutReadException("Error checking first character", ioe);
        }
        
        
        curr_section = UrlSection.NO_SECTION;
        
        while(true) {
            String nextline;

            try {
                nextline = reader.readLine();
            } catch (IOException e) {
                throw new ShortcutReadException(e);
            }
            
            /* Loop through the file, keeping track of which section we are in.
             * If we find a URL, capture it.  
             * If it is an ASCII URL (i.e. in the [InternetShortcut] section),
             * store it in the parsed_url variable.
             * If it is a UTF7 URL (i.e. in the [InternetShortcut.W] section),
             * decode it and store it in the parsed_urlw variable.
             */
            if(nextline == null) {
                break;
            } else if(matches(internet_shortcut_header, nextline)) {
                curr_section = UrlSection.INTERNET_SHORTCUT_SECTION;
            } else if(matches(internet_shortcut_w_header, nextline)) {
                curr_section = UrlSection.INTERNET_SHORTCUT_W_SECTION;
            } else if(matches(header, nextline)) {
                curr_section = UrlSection.OTHER_SECTION;
            } else if(matches(blank_or_comment, nextline)) {
                // Ignore this line
            } else {
                Matcher key_value_matcher = key_value_pattern.matcher(nextline);
                if(key_value_matcher.matches()) {
                    assert(key_value_matcher.groupCount() == 4);
                    String key = key_value_matcher.group(1);
                    String value = key_value_matcher.group(4);
                    if(key == null) {
                        // The fact that we are here suggests that either the file
                        // is corrupt or this algorithm is incorrect.  Either way
                        // we will just ignore the issue and move on to the next line.
                    } else {
                        if(key.equals("URL")) {
                            if(curr_section == UrlSection.INTERNET_SHORTCUT_SECTION) {
                                parsed_url = value;
                            } else if(curr_section == UrlSection.INTERNET_SHORTCUT_W_SECTION) {
                                try {
                                    byte[] asciiBytes = value.getBytes("US-ASCII");
                                    
                                    // Note that the jutf7 library (http://jutf7.sourceforge.net/)
                                    // needs to be added as a Maven dependency (or I think can be just
                                    // added to the classpath) in order to add UTF-7 support.
                                    parsed_urlw = new String(asciiBytes, "UTF-7");
                                } catch(UnsupportedEncodingException e) {
                                    throw new ShortcutReadException("Error converting URL from UTF7", e);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Return the appropriate URL.  If a UTF7 URL is found,
        // ignore the ASCII URL as it is probably not correct.
        if(parsed_urlw != null) {
            return parsed_urlw;
        } else if(parsed_url != null) {
            return parsed_url;
        } else {
            throw new ShortcutReadException("URL not found in file");
        }
    }
}
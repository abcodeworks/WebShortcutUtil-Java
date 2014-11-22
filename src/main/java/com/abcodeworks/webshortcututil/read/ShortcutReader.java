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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parent class for readers.
 */

public abstract class ShortcutReader {
    /* Common patterns which appear in Desktop, URL, and Website files.
     * Note that these patterns are fairly relaxed and allow a lot of whitespace.*/
    protected Pattern
        // Header e.g. [MyHeader]
        header = Pattern.compile("^\\s*\\[.*\\]\\s*$"),
        // Key/Value pair e.g. MyKey=MyValue
        key_value_pattern = Pattern.compile("^\\s*([A-Za-z0-9-]*)(\\[([^\\[\\]]*)\\])?\\s*=\\s*([^\\n\\r]*?)\\s*$");
    
    /* Checks if the str matches the specified pattern. */
    protected boolean matches(Pattern p, String str)
    {
        Matcher matcher = p.matcher(str);
        return matcher.matches();
    }
    
    /* Gets the shortcut name which is assumed to be the same as the
     * name of the file.
     */
    protected String getShortcutName(File file) {
        String name = file.getName();
       
        // Remove the extension
        int extension_start = name.lastIndexOf('.');
        if (extension_start > 0) {
        name = name.substring(0, extension_start);
        }
       
        return name;
	}
	
    /**
     * Reads the URL contained in the specified file.
     * 
     * @param file The file to read from.
     * @return The URL as a string.  Non-null.
     * @throws FileNotFoundException
     * @throws ShortcutReadException If any error occurs while reading the file.
     */
	public String readUrlString(File file)
	            throws FileNotFoundException,
	                   ShortcutReadException
	{
	    // We need to open a stream since the core implementation deals with streams.
	    FileInputStream stream = new FileInputStream(file);
	    String url = readUrlString(stream);
	    return url;
	}

    /**
     * Reads the URL contained in the specified stream.
     * 
     * @param stream The input stream to read from.  The stream is not closed.
     * @return The URL as a string.  Non-null.
     * @throws FileNotFoundException
     * @throws ShortcutReadException If any error occurs while reading the file.
     */
	abstract public String readUrlString(InputStream stream)
	            throws ShortcutReadException;
    
	/**
	 * Reads the specified file and extracts the contents.
	 * 
	 * See {@link com.abcodeworks.webshortcututil.read.ShortcutReadUtil#read ShortcutReadUtil.read} for details.
	 * 
     * @param file The file to read
     * @return A ShortcutContents class containing the name and URL of the shortcut.  Non-null.  The name and URL will be non-null as well.
     * @throws FileNotFoundException
     * @throws ShortcutReadException If any error occurs while reading the file.
	 */
    public ShortcutContents read(File file)
            throws FileNotFoundException,
                   ShortcutReadException {
        return new ShortcutContents(
                getShortcutName(file),
                readUrlString(file));
    }
}

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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.abcodeworks.webshortcututil.read.ShortcutReadException;

/**
 * Writes URL shortcuts (Windows shortcuts with a .url extension).
 * 
 */
public class UrlShortcutWriter extends ShortcutWriter {
    public String defaultExtension() {
        return "url";
    }
    
    protected void writeSection(BufferedWriter writer, String header, String url)
            throws IOException {
        writer.write("[");
        writer.write(header);
        writer.write("]\r\n");
        writer.write("URL=");
        writer.write(url);
        writer.write("\r\n");
    }
    
    @Override
    public void write(OutputStream stream, String name, String url)
            throws ShortcutWriteException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
        try {
            // We need to check if the URL only contains ASCII characters.  If it does not we need to encode it using UTF7.
            // See http://stackoverflow.com/questions/3585053/in-java-is-it-possible-to-check-if-a-string-is-only-ascii
            CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();
            if(asciiEncoder.canEncode(url)) {
                writeSection(writer, "InternetShortcut", url);
            } else {
                // We still need the ASCII sections, or Windows gives an error.
                Pattern unicodeCharPattern = Pattern.compile("[\u0080-\uFFFF]");
                Matcher matcher = unicodeCharPattern.matcher(url);
                String ascii_url = matcher.replaceAll("?");
                writeSection(writer, "InternetShortcut", ascii_url);
                writeSection(writer, "InternetShortcut.A", ascii_url);

                // Note that the jutf7 library (http://jutf7.sourceforge.net/)
                // needs to be added as a Maven dependency (or I think can be just
                // added to the classpath) in order to add UTF-7 support.
                byte[] asciiBytes = url.getBytes("UTF-7");
                String utf7Url = new String(asciiBytes, "US-ASCII");
                writeSection(writer, "InternetShortcut.W", utf7Url);
            }

            writer.close();
        } catch (IOException e) {
            throw new ShortcutWriteException();
        } finally {
            try {
                if(writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                throw new ShortcutWriteException(e);
            }
        }
        
    }

}

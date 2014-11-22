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
import java.io.UnsupportedEncodingException;

import com.abcodeworks.webshortcututil.read.ShortcutReadException;

/**
 * Writes Desktop shortcuts (FreeDesktop shortcuts with a .desktop extension - used by Linux, etc.).
 * 
 */
public class DesktopShortcutWriter extends ShortcutWriter {

    @Override
    protected String defaultExtension() {
        return "desktop";
    }

    @Override
    public void write(OutputStream stream, String name, String url)
            throws ShortcutWriteException {
 
        // Open a buffered reader for efficiency.
        BufferedWriter writer = null;
        try {
            // Note that Desktop Entry files use UTF8
            writer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new ShortcutWriteException(e);
        }

        try {
            writer.write("[Desktop Entry]\n");
            writer.write("Encoding=UTF-8\n");
            
            writer.write("URL=");
            writer.write(url);
            writer.write("\n");
            
            writer.write("Name=");
            writer.write(name);
            writer.write("\n");
            
            writer.write("Type=Link\n");
            
            writer.write("URL=");
            writer.write(url);
            writer.write("\n");
        } catch(IOException e) {
            throw new ShortcutWriteException(e);
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

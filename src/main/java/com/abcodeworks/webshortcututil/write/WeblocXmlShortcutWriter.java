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

import java.io.IOException;
import java.io.OutputStream;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;

/**
 * Writes Webloc XML shortcuts (Apple/Mac/OSX shortcuts with a .webloc extension).
 * 
 */
public class WeblocXmlShortcutWriter extends WeblocShortcutWriter {
    @Override
    public void writePlist(OutputStream stream, NSObject root)
            throws ShortcutWriteException {
        try {
            // Note that the stream is automatically closed by the PropertyListParser
            PropertyListParser.saveAsXML(root, stream);
        } catch (IOException e) {
            throw new ShortcutWriteException(e);
        }
    }
}

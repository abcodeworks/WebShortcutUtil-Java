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

import java.io.OutputStream;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;

/**
 * Parent class for the Webloc writers.
 * 
 * This class is abstract because you must choose a specific type of Webloc file to write.
 * Use the {@link com.abcodeworks.webshortcututil.write.WeblocBinaryShortcutWriter WeblocBinaryShortcutWriter}
 * or {@link com.abcodeworks.webshortcututil.write.WeblocXmlShortcutWriter WeblocXmlShortcutWriter}
 * classes instead.
 * 
 */
abstract public class WeblocShortcutWriter extends ShortcutWriter {

    abstract protected void writePlist(OutputStream stream, NSObject root)
            throws ShortcutWriteException;
    
    @Override
    protected String defaultExtension() {
        return "webloc";
    }

    @Override
    public void write(OutputStream stream, String name, String url)
            throws ShortcutWriteException {
        NSDictionary root = new NSDictionary();
        root.put("URL", url);
        writePlist(stream, root);
    }

}

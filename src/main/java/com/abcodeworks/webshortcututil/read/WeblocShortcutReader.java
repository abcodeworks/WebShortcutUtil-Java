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

import java.io.IOException;
import java.io.InputStream;

import com.abcodeworks.webshortcututil.write.ShortcutWriteException;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;

/**
 * Reads Webloc shortcuts (Apple/Mac/OSX shortcuts with a .webloc extension).
 * 
 * Note that this class reads both Binary and XML types of webloc files.
 * 
 */
public class WeblocShortcutReader extends ShortcutReader {

    @Override
    public String readUrlString(InputStream stream)
            throws ShortcutReadException {
        
        NSString urlObject = null;
        
        try {
            /* Parse the file and extract the URL.
             * The plist library will figure out whether the file is a Binary or an XML file
             * and parse it appropriately.
             */
            NSDictionary rootDict = (NSDictionary)PropertyListParser.parse(stream);
            urlObject = (NSString)rootDict.objectForKey("URL");
        } catch (Exception e) {
            throw new ShortcutReadException("Error reading URL", e);
        }

        if(urlObject == null) {
            throw new ShortcutReadException("URL not found");
        }
        
        try {
            String url = urlObject.toString();
            return url;
        } catch (Exception e) {
            throw new ShortcutReadException("Error reading URL", e);
        }
    }
}

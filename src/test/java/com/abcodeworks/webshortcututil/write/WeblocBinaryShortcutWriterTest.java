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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import com.abcodeworks.webshortcututil.read.ShortcutContents;
import com.abcodeworks.webshortcututil.read.ShortcutReadException;
import com.abcodeworks.webshortcututil.read.ShortcutReadUtil;
import com.abcodeworks.webshortcututil.write.DesktopShortcutWriter;
import com.abcodeworks.webshortcututil.write.FileAlreadyExistsException;
import com.abcodeworks.webshortcututil.write.ShortcutWriteException;
import com.abcodeworks.webshortcututil.write.ShortcutWriter;
import com.abcodeworks.webshortcututil.write.UrlShortcutWriter;
import com.abcodeworks.webshortcututil.write.WeblocBinaryShortcutWriter;
import com.abcodeworks.webshortcututil.write.WeblocXmlShortcutWriter;

public class WeblocBinaryShortcutWriterTest extends ShortcutWriterTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    @Test
    public void testWrite()
                  throws FileNotFoundException,
                         ShortcutReadException,
                         IOException,
                         FileAlreadyExistsException,
                         ShortcutWriteException
    {
        ShortcutWriter writer = new WeblocBinaryShortcutWriter();
        File folder = tempFolder.newFolder("weblocbin");
        testWriteShortcut(writer, folder, "Google", "http://www.google.com");
        testWriteShortcut(writer, folder, " !#$&'()+,-.09;=@AZ[]_`az{}~", "http://www.google.com");
    }
    
    @Test
    public void testWriteNonAscii()
                  throws FileNotFoundException,
                         ShortcutReadException,
                         IOException,
                         FileAlreadyExistsException,
                         ShortcutWriteException
    {
        ShortcutWriter writer = new WeblocBinaryShortcutWriter();
        File folder = tempFolder.newFolder("weblocbin_nonascii");
        testWriteShortcut(writer, folder, "导航.中国", "http://导航.中国/");
    }
}
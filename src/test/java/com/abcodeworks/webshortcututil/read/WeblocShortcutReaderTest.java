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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.*;
import org.junit.rules.ExpectedException;

import com.abcodeworks.webshortcututil.read.ShortcutReadException;
import com.abcodeworks.webshortcututil.read.ShortcutReader;
import com.abcodeworks.webshortcututil.read.WeblocShortcutReader;
import com.abcodeworks.webshortcututil.write.FileAlreadyExistsException;

import static org.junit.Assert.*;

import java.net.URL;

public class WeblocShortcutReaderTest extends ShortcutReaderTest
{
    final String
        BIN_PATH = "samples" + File.separator + "real" + File.separator + "webloc" + File.separator + "binary",
        BIN_PERCENT_PATH = "samples" + File.separator + "real" + File.separator + "webloc" + File.separator + "binary" + File.separator + "percent_encoded",
        XML_PATH = "samples" + File.separator + "real" + File.separator + "webloc" + File.separator + "xml",
        XML_PERCENT_PATH = "samples" + File.separator + "real" + File.separator + "webloc" + File.separator + "xml" + File.separator + "percent_encoded",
        XML_FAKE_PATH = "samples" + File.separator + "fake" + File.separator + "webloc" + File.separator + "xml";

    @Rule
    public ExpectedException thrown= ExpectedException.none();
    
    @Test
    public void testReadUrlString()
                  throws FileNotFoundException,
                         ShortcutReadException,
                         IOException
    {
        ShortcutReader reader = new WeblocShortcutReader();
        
        // Binary PLIST shortcuts generated on OSX
        testShortcut(reader, BIN_PATH, "Google.webloc", "Google", "https://www.google.com/");
        testShortcut(reader, BIN_PATH, "Yahoo!.webloc", "Yahoo!", "http://www.yahoo.com/");
        
        // XML PLIST shortcuts generated on OSX
        testShortcut(reader, XML_PATH, "Google.webloc", "Google", "https://www.google.com/");
        testShortcut(reader, XML_PATH, "Yahoo!.webloc", "Yahoo!", "http://www.yahoo.com/");
    }
    
    @Test
    public void testReadUrlStringNonAscii()
                  throws FileNotFoundException,
                         ShortcutReadException,
                         IOException
    {
        ShortcutReader reader = new WeblocShortcutReader();
        
        // Binary PLIST shortcuts generated on OSX
        testShortcut(reader, BIN_PATH, "CIOとITマネージャーの課題を解決するオンラインメディア - ZDNet Japan.webloc", "CIOとITマネージャーの課題を解決するオンラインメディア - ZDNet Japan", "http://japan.zdnet.com/");
        testShortcut(reader, BIN_PATH, "Microsoft Sverige  Enheter och tjänster.webloc", "Microsoft Sverige  Enheter och tjänster", "http://www.microsoft.com/sv-se/default.aspx");
        testShortcut(reader, BIN_PATH, "Myspace  Social Entertainment.webloc", "Myspace  Social Entertainment", "http://www.myspace.com/");
        testShortcut(reader, BIN_PATH, "sverige - Sök på Google.webloc", "sverige - Sök på Google", "http://www.google.se/#sclient=tablet-gws&hl=sv&tbo=d&q=sverige&oq=sveri&gs_l=tablet-gws.1.1.0l3.13058.15637.28.17682.5.2.2.1.1.0.143.243.0j2.2.0...0.0...1ac.1.xX8iu4i9hYM&pbx=1&fp=1&bpcl=40096503&biw=1280&bih=800&bav=on.2,or.r_gc.r_pw.r_qf.&cad=b");
        testShortcut(reader, BIN_PATH, "www.ÖÐ¹úÕþ¸®.ÕþÎñ.cn.webloc", "www.ÖÐ¹úÕþ¸®.ÕþÎñ.cn", "http://www.xn--fiqs8sirgfmh.xn--zfr164b.cn/");
        testShortcut(reader, BIN_PATH, "中国雅虎首页.webloc", "中国雅虎首页", "http://cn.yahoo.com/");
        testShortcut(reader, BIN_PATH, "导航.中国.webloc", "导航.中国", "http://xn--fet810g.xn--fiqs8s/");
        testShortcut(reader, BIN_PATH, "百度一下，你就知道.webloc", "百度一下，你就知道", "http://www.baidu.com/");
        testShortcut(reader, BIN_PERCENT_PATH, "导航.中国.webloc", "导航.中国", "http://%E5%AF%BC%E8%88%AA.%E4%B8%AD%E5%9B%BD/");
        
        // XML PLIST shortcuts generated on OSX
        testShortcut(reader, XML_PATH, "CIOとITマネージャーの課題を解決するオンラインメディア - ZDNet Japan.webloc", "CIOとITマネージャーの課題を解決するオンラインメディア - ZDNet Japan", "http://japan.zdnet.com/");
        testShortcut(reader, XML_PATH, "Microsoft Sverige  Enheter och tjänster.webloc", "Microsoft Sverige  Enheter och tjänster", "http://www.microsoft.com/sv-se/default.aspx");
        testShortcut(reader, XML_PATH, "Myspace  Social Entertainment.webloc", "Myspace  Social Entertainment", "http://www.myspace.com/");
        testShortcut(reader, XML_PATH, "sverige - Sök på Google.webloc", "sverige - Sök på Google", "http://www.google.se/#sclient=tablet-gws&hl=sv&tbo=d&q=sverige&oq=sveri&gs_l=tablet-gws.1.1.0l3.13058.15637.28.17682.5.2.2.1.1.0.143.243.0j2.2.0...0.0...1ac.1.xX8iu4i9hYM&pbx=1&fp=1&bpcl=40096503&biw=1280&bih=800&bav=on.2,or.r_gc.r_pw.r_qf.&cad=b");
        testShortcut(reader, XML_PATH, "www.ÖÐ¹úÕþ¸®.ÕþÎñ.cn.webloc", "www.ÖÐ¹úÕþ¸®.ÕþÎñ.cn", "http://www.xn--fiqs8sirgfmh.xn--zfr164b.cn/");
        testShortcut(reader, XML_PATH, "中国雅虎首页.webloc", "中国雅虎首页", "http://cn.yahoo.com/");
        testShortcut(reader, XML_PATH, "导航.中国.webloc", "导航.中国", "http://xn--fet810g.xn--fiqs8s/");
        testShortcut(reader, XML_PATH, "百度一下，你就知道.webloc", "百度一下，你就知道", "http://www.baidu.com/");
        testShortcut(reader, XML_PERCENT_PATH, "导航.中国.webloc", "导航.中国", "http://%E5%AF%BC%E8%88%AA.%E4%B8%AD%E5%9B%BD/");
    }
    
    @Test
    public void testReadGarbledHeader()
            throws FileNotFoundException,
                   ShortcutReadException,
                   IOException {
        ShortcutReader reader = new WeblocShortcutReader();
        thrown.expect(ShortcutReadException.class);
        reader.readUrlString(getTestStream(XML_FAKE_PATH, "MissingDictionary.webloc"));
    }
    
    @Test
    public void testReadHeaderOnly()
            throws FileNotFoundException,
                   ShortcutReadException,
                   IOException {
        ShortcutReader reader = new WeblocShortcutReader();
        thrown.expect(ShortcutReadException.class);
        thrown.expectMessage("URL not found");
        reader.readUrlString(getTestStream(XML_FAKE_PATH, "MissingUrl.webloc"));
    }
}

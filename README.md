WebShortcutUtil (Java library)
==============================

Summary
-------
This application allow you to launch web/internet shortcuts created in Windows, Linux, and Apple. Specifically, these are files with the following extensions: .url, .website, .desktop, .webloc. This application is part of the [Web Shortcut Utility Suite](http://beckus.github.io/WebShortcutUtil/).

Websites
--------
Main Website: http://beckus.github.io/WebShortcutUtil/<br/>
Maven Repository: http://mvnrepository.com/artifact/com.abcodeworks/webshortcututil<br/>
JavaDocs:     http://abcodeworks.github.io/WebShortcutUtil-Java/javadoc/latest/<br/>
Source Code:  https://github.com/abcodeworks/WebShortcutUtil-Java/<br/>

Maven Repository
----------------
The library is published to the Maven repository.  Use the following dependency:

    <dependency>
        <groupId>com.abcodeworks</groupId>
        <artifactId>webshortcututil</artifactId>
        <version>1.0.0</version>
    </dependency>

Building
--------
- To compile:<br/>
  mvn compile

- To run unit tests (note that some file names contain unicode characters, so it may not work<br/>
  on all operating systems - I tested successfully on Ubuntu 12.04):<br/>
  mvn test<br/>
  Detailed error information is in: target/site/surefire-reports
  
- To run unit tests in a debugger:<br/>
  mvn -Dmaven.surefire.debug test<br/>
  See http://maven.apache.org/surefire/maven-surefire-plugin/examples/debugging.html

- to generate test coverage reports:<br/>
  mvn cobertura:cobertura<br/>
  xdg-open target/site/cobertura/index.html
  
- To generate javadocs:<br/>
  mvn javadoc:javadoc<br/>
  xdg-open target/site/apidocs/index.html

- To package:<br/>
  mvn package<br/>
  The jar file is in the target/ folder
  
- To deploy to the Maven repository:<br/>
  Follow this guide: http://central.sonatype.org/pages/ossrh-guide.html
  To stage:   mvn clean deploy
  To release: mvn nexus-staging:release
  
  Need local settings.xml in ~.m2/ folder:<br/>
    <settings>
      <servers>
        <server>
          <id>ossrh</id>
          <username>xxx</username>
          <password>yyy</password>
        </server>
      </servers>
    </settings>
  
  Need GPG key set up:<br/>
  For backuping up/restoring keys: https://help.ubuntu.com/community/GnuPrivacyGuardHowto

Future Ideas
------------
Some ideas for enhanced functionality:
-   Improve ability to read an unknown shortcut from an input stream.
-   For ".desktop" files, add logic to extract the names embedded in a shortcut
    (including all localized versions of the name).  Similar logic could also
    be written for ".website" files.
-   Explore unicode functionality for ".webloc" files.  Will a Mac open a URL
    that has unicode characters?
-   Add an ASCII conversion option to the filename creation routines
    (i.e. to remove unicode characters).
  
Libraries and Licenses
----------------------
This project is released under the Apache License 2.0.

This project includes the following libraries:
-   com.dd.plist - http://code.google.com/p/plist/ - MIT License
-   com.beetstra.jutf7 http://jutf7.sourceforge.net/ - MIT License

All licenses can be found in the root folder.


References
----------
-   Free Desktop:<br/>
      http://standards.freedesktop.org/desktop-entry-spec/latest/ (used Version 1.1-draft)

-   Windows URL (also applicable to Website):<br/>
      http://stackoverflow.com/questions/539962/creating-a-web-shortcut-on-user-desktop-programmatically<br/>
      http://stackoverflow.com/questions/234231/creating-application-shortcut-in-a-directory<br/>
      http://delphi.about.com/od/internetintranet/a/lnk-shortcut.htm<br/>
      http://read.pudn.com/downloads3/sourcecode/windows/system/11495/shell/shlwapi/inistr.cpp__.htm<br/>
      http://epiphany-browser.sourcearchive.com/documentation/2.24.0/plugin_8cpp-source.html<br/>
      http://epiphany-browser.sourcearchive.com/documentation/2.24.0/plugin_8cpp-source.html<br/>

-   Webloc / Plist:<br/>
      http://search.cpan.org/~bdfoy/Mac-PropertyList-1.38/<br/>
        or https://github.com/briandfoy/mac-propertylist<br/>
      http://opensource.apple.com/source/CF/CF-550/CFBinaryPList.c<br/>
      http://code.google.com/p/cocotron/source/browse/Foundation/NSPropertyList/NSPropertyListReader_binary1.m<br/>
      http://www.apple.com/DTDs/PropertyList-1.0.dtd<br/>

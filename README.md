WebShortcutUtil (Java library)
==============================

Summary
-------
Java library for reading and writing web shortcut files

Websites
--------
Main Website: 
Source Code: 
JavaDocs: 

Building
--------
- To compile:
  mvn compile

- To run unit tests (note that some file names contain unicode characters, so it may not work
  on all operating systems - I tested successfully on Ubuntu 12.04):
  mvn test
  Detailed error information is in: target/site/surefire-reports
  
- To run unit tests in a debugger:
  mvn -Dmaven.surefire.debug test
  See http://maven.apache.org/surefire/maven-surefire-plugin/examples/debugging.html

- to generate test coverage reports:
  mvn cobertura:cobertura
  xdg-open target/site/cobertura/index.html
  
- To generate javadocs:
  mvn javadoc:javadoc
  xdg-open target/site/apidocs/index.html

- To package:
  mvn package
  The jar file is in the target/ folder

Future Ideas
------------
Some ideas for enhanced functionality:
-   Improve ability to read an unknown shortcut from an input stream.  The
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
-   Free Desktop:
      http://standards.freedesktop.org/desktop-entry-spec/latest/ (used Version 1.1-draft)

-   Windows URL (also applicable to Website):
      http://stackoverflow.com/questions/539962/creating-a-web-shortcut-on-user-desktop-programmatically
      http://stackoverflow.com/questions/234231/creating-application-shortcut-in-a-directory
      http://delphi.about.com/od/internetintranet/a/lnk-shortcut.htm
      http://read.pudn.com/downloads3/sourcecode/windows/system/11495/shell/shlwapi/inistr.cpp__.htm
      http://epiphany-browser.sourcearchive.com/documentation/2.24.0/plugin_8cpp-source.html
      http://epiphany-browser.sourcearchive.com/documentation/2.24.0/plugin_8cpp-source.html

-   Webloc / Plist:
      http://search.cpan.org/~bdfoy/Mac-PropertyList-1.38/
        or https://github.com/briandfoy/mac-propertylist
      http://opensource.apple.com/source/CF/CF-550/CFBinaryPList.c
      http://code.google.com/p/cocotron/source/browse/Foundation/NSPropertyList/NSPropertyListReader_binary1.m
      http://www.apple.com/DTDs/PropertyList-1.0.dtd
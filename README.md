# GenBank Reader Web-application #

### What is this repository for? ###

* This web-application is able to extract data from a GenBank file and return the required information.
* Version 1.0.0
* Copyright (c) 2016 Wout van Helvoirt

### How do I get set up? ###

* This repository requires at least Java 8 to function properly. [http://www.oracle.com]
* Make sure you have at least installed version 1.3.1 of Apache Commons CLI, version 1.3.1 of Apache FileUpload and 2.4 of Apache IO. Previous versions could work, but are not officially supported. [http://commons.apache.org]
* Apache Tomcat 8.0.28 has been used to set up a local host. Taglib Impl and Taglib Spec are used for JSP integration. Both are included when you install Tomcat. [http://commons.apache.org]
* Fork this repository and use the bitbucket link to add the forked repository to your editing program of choice.
* If you want to use the program without applying adjustments, you should download de dist folder.

### Which operations are supported? ###

This Reader is able to analyse GenBank files and give you information about it's genes and cds's. To use the GenBank Reader, start by selecting your GenBank file and click the upload button to continue. 
The settings that can be selected on the next page are:

* a summary of the GenBank file.
* Fetch gene/CDS names that match a RegEx pattern.
* Fetch the features within a coordinate region.
* Find the sequence sites that match sequence RegEx pattern.

** Note: A distribution with javadoc as well as example GenBank files can be found in the downloads section of this repository. **
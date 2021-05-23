# ci-versioning-extension
Automatic project version generation using maven extensions


## Uses:
``Plugin takes care of generating maven version based on the latest commit hash so that version can become CI friendly.``
### Please follow the below steps to set up the project
 * Add a file extension.xml under .mvn folder which is on the base directory of your maven project with the below content


    <extensions xmlns="http://maven.apache.org/EXTENSIONS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://maven.apache.org/EXTENSIONS/1.0.0 https://maven.apache.org/xsd/core-extensions-1.0.0.xsd">
        <extension>
            <groupId>com.sab.maven</groupId>
            <artifactId>ci-versioning-extension</artifactId>
            <version>1.0.0</version>
        </extension>
    </extensions>  
    
 * Follow projection version convention as below. So that placeholder revision will be updated by plugin in the runtime
 
 
    * Single module project:
        
            <groupId>xx.xxx</groupId>
            <artifactId>xx</artifactId>
            <version>${revision}</version>
        
            <prerequisites>
                <maven>3.3.1</maven>
            </prerequisites>
        
            <properties>
                <revision>SNAPSHOT</revision>
            <properties>
            
     * Multi module structure:
         * Parent POM
             
             <groupId>xx.xxx</groupId>
             <artifactId>xx</artifactId>
             <version>${revision}</version>
                    
             <prerequisites>
                 <maven>3.3.1</maven>
             </prerequisites>
                    
             <properties>
                 <revision>SNAPSHOT</revision>
             <properties>
             
          * Child POM
          
             <parent>
                  <groupId>xx.xxx</groupId>
                  <artifactId>xx</artifactId>
                  <version>${revision}</version>
             </parent>
             <modelVersion>4.0.0</modelVersion>
             <artifactId>child.xx.xx</artifactId>
             
  
 * We are almost done compile the project as usual. 
 
 
  
## Improvements:
   * Version on target pom.xml is not updating i.e placeholder revision can't be updated with the dynamic version which needs to be insvestigated.   
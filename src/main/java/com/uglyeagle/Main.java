package com.uglyeagle;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.net.URL;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.core.StandardContext;

public class Main {

    public static void main(String[] args) throws Exception {
        Path baseDir = Files.createTempDirectory("tomcat-base");

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.setBaseDir(baseDir.toString());

        File rootDir = new File(System.getProperty("user.dir"));
        File webContent = new File(rootDir, "src/main/webapp");
        File classesDir = new File(rootDir, "target/classes");

        StandardContext ctx = (StandardContext) tomcat.addWebapp("", webContent.getAbsolutePath());
        ctx.setParentClassLoader(Main.class.getClassLoader());

        // Explicitly set the location of web.xml
        ctx.setConfigFile(new File(webContent, "/WEB-INF/web.xml").toURI().toURL());

        // Enable JSP + classes loading
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", classesDir.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }
}



// this is a simpler version I wrote. If it does not work in your system then
// make the HerokuMain as your main class. which is inspired from a repo, even I need to pay attention
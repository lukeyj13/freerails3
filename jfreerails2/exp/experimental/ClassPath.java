package experimental;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

/**
 * ClassPath finds and records the fully qualified name of every Class on the
 * classpath via the system property "java.class.path".
 * <p>
 * Based on original prototype by duncanIdaho for javagaming.org.
 * 
 * @author adam@jgf
 */
public class ClassPath {
    protected Logger logger = Logger.getLogger("jgf.classlocater.classpath");

    protected LinkedList<String> pathElementsThatHaveAlreadyBeenProcessed;

    protected LinkedList<File> jarsThatHAveAlreadyBeenProcessed;

    /**
     * create a new ClassPath instance and find all classes on the classpath
     */
    public ClassPath() {
    }

    public List getAllClassNames() {
        String path = null;
        pathElementsThatHaveAlreadyBeenProcessed = new LinkedList<String>();
        jarsThatHAveAlreadyBeenProcessed = new LinkedList<File>();

        LinkedList<String> pendingClassPathElements = new LinkedList<String>();
        LinkedList<String> processedClassPathElements = new LinkedList<String>();

        try {
            path = System.getProperty("java.class.path");
        } catch (Exception x) {
            x.printStackTrace();
        }

        logger.info("scanning classpath: " + path);
        StringTokenizer toke = new StringTokenizer(path, File.pathSeparator);

        while (toke.hasMoreTokens()) {
            String pathElement = toke.nextToken();
            pendingClassPathElements.add(pathElement);
        }

        for (Iterator<String> iter = pendingClassPathElements.iterator(); iter
                .hasNext();) {
            String pathElement = iter.next();
            LinkedList<String> processPendingElement = processPendingElement(pathElement);
            processedClassPathElements.addAll(processPendingElement);
        }

        return processedClassPathElements;
    }

    /**
     * Clones the supplied list, then goes through it processing every element.
     * 
     */
    protected LinkedList<String> processPendingElement(String pathElement) {
        LinkedList<String> discoveredClasses = new LinkedList<String>();

        File elementFile = new File(pathElement);
        String elementName = elementFile.getAbsolutePath();

        // do NOT process dupes
        if (pathElementsThatHaveAlreadyBeenProcessed.contains(elementName))
            return discoveredClasses;

        try {
            if (elementName.endsWith(".jar")) {
                JarFile jar = null;
                Manifest man = null;
                jar = new JarFile(elementFile);
                man = jar.getManifest();

                // Find any nested path elements inside the JAR's own private
                // class-path...
                if (!(jarsThatHAveAlreadyBeenProcessed.contains(elementFile))) {
                    if (man != null) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Jarfile = " + elementFile
                                    + " was not in jarsalreadydone (size = "
                                    + jarsThatHAveAlreadyBeenProcessed.size());
                        }
                        jarsThatHAveAlreadyBeenProcessed.add(elementFile);
                        List extraPathElements = findPathElementsInJar(man,
                                jar, elementFile);

                        logger.info("...[" + elementFile + "] contained "
                                + extraPathElements.size()
                                + " additional path elements");
                        for (Iterator iter = extraPathElements.iterator(); iter
                                .hasNext();) {
                            String element = (String) iter.next();
                            discoveredClasses
                                    .addAll(processPendingElement(element));
                        }

                    }

                    // ...and add all the direct-listed classes that were in the
                    // JAR

                    Enumeration e = jar.entries();
                    while (e.hasMoreElements()) {
                        JarEntry entry = (JarEntry) e.nextElement();
                        if (!entry.isDirectory()
                                && entry.getName().endsWith(".class")) {
                            String className = getClassNameFrom(entry.getName());
                            discoveredClasses.add(className);
                        }
                    }
                }
            } else if (elementName.endsWith(".zip")) {
                discoveredClasses.addAll(getZipContents(elementFile));
            } else if (elementName.endsWith(".class")) {
                String className = convertToClass(elementFile);
                discoveredClasses.add(className);
            } else {
                discoveredClasses.addAll(getDirectoryContents(elementFile));
            }

            // Mark this element as having been processed, and do NOT process
            // dupes
            pathElementsThatHaveAlreadyBeenProcessed.add(elementName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return discoveredClasses;
    }

    /**
     * @param classFile
     *            a class file listed on the classpath itself.
     */
    protected String convertToClass(File classFile) {
        return getClassNameFrom(classFile.getName());
    }

    /**
     * replace ANY slashes with dots and remove the .class at the end of the
     * file name.
     * 
     * @param entryName
     *            a file name relative to the classpath. A class of package org
     *            found in directory bin would be passed into this method as
     *            "org/MyClass.class"
     * @return a fully qualified Class name.
     */
    private String getClassNameFrom(String entryName) {
        String foo = entryName.replace('/', '.');
        foo = foo.replace('\\', '.');
        return foo.substring(0, foo.lastIndexOf('.'));
    }

    /**
     * Finds all path elements in the supplied JAR and returns them as a list
     * 
     * @param man
     *            the manifest of the given jar
     * @param jar
     *            the jar associated with the given manifest.
     */
    protected LinkedList<String> findPathElementsInJar(Manifest man,
            JarFile jar, File jarFile) {
        LinkedList<String> result = new LinkedList<String>();
        Attributes atts = man.getMainAttributes();
        Set keys = atts.keySet();
        Iterator i = keys.iterator();
        while (i.hasNext()) {
            Object key = i.next();
            String value = (String) atts.get(key);

            if (logger.isDebugEnabled()) {
                logger.debug(jar.getName() + "  " + key + ": " + value);
            }

            if (key.toString().equals("Class-Path")) {
                logger.info("scanning " + jar.getName()
                        + "'s manifest classpath: " + value);
                StringTokenizer toke = new StringTokenizer(value);
                while (toke.hasMoreTokens()) {
                    String element = toke.nextToken();

                    if (jarFile.getParent() == null)
                        result.add(element);
                    else {
                        result.add(jarFile.getParent() + File.separator
                                + element);
                    }

                }
            }
        }
        return result;
    }

    /**
     * Adds all class names found in the zip mentioned
     * 
     * @param zipFile
     */
    protected LinkedList<String> getZipContents(File zipFile) {
        LinkedList<String> result = new LinkedList<String>();
        ZipFile zip = null;
        try {
            zip = new JarFile(zipFile);
        } catch (IOException iox) {
        }
        if (zip != null) {
            Enumeration e = zip.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    String className = getClassNameFrom(entry.getName());
                    result.add(className);
                }
            }
        }

        return result;
    }

    /**
     * This method takes a top level classpath dir i.e. 'classes' or bin
     * 
     * @param dir
     */
    protected LinkedList<String> getDirectoryContents(File dir) {
        LinkedList<String> result = new LinkedList<String>();

        // drill through contained dirs ... this is expected to be the
        // 'classes' or 'bin' dir
        File files[] = dir.listFiles();
        if (null == files) {
            logger.info("dir.listFiles() returned null for " + dir);
            return result;
        }
        for (int i = 0; i < files.length; ++i) {
            File f = files[i];
            if (f.isDirectory()) {
                result.addAll(getDirectoryContents("", f));
            } else {
                if (f.getName().endsWith(".class"))
                    result.add(convertToClass(f));
            }
        }

        return result;
    }

    /**
     * This method does the real directory recursion, passing along the the
     * corresponding package-path to this directory.
     * 
     * @param pathTo
     *            the preceding path to this directory
     * @param dir
     *            a directory to search for class files
     */
    protected LinkedList<String> getDirectoryContents(String pathTo, File dir) {
        LinkedList<String> result = new LinkedList<String>();

        String pathToHere = pathTo + dir.getName() + File.separator;
        File files[] = dir.listFiles();
        for (int i = 0; i < files.length; ++i) {
            File f = files[i];
            if (f.isDirectory()) {
                result.addAll(getDirectoryContents(pathToHere, f));
            } else {
                if (f.getName().endsWith(".class")) {
                    String absFilePath = pathToHere + f.getName();
                    result.add(getClassNameFrom(absFilePath));
                }
            }
        }

        return result;
    }
}

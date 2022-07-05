package com.commons.utilities;

import java.io.*;
import java.net.URL;

public class FileUtils {

    public static URL getResource(String resourceName, Class callingClass) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);

        if (url == null) {
            url = ResourcesUtils.class.getClassLoader().getResource(resourceName);
        }

        if (url == null) {
            ClassLoader cl = callingClass.getClassLoader();

            if (cl != null) {
                url = cl.getResource(resourceName);
            }
        }

        if ((url == null) && (resourceName != null) && ((resourceName.length() == 0) || (resourceName.charAt(0) != '/'))) {
            return getResource('/' + resourceName, callingClass);
        }

        return url;
    }

    public static void newFolder(String pathname) throws Exception {
        File userFolder = new File(pathname);
        if(!userFolder.exists()) {
            if(!userFolder.mkdir())
                throw new Exception("Was not able to create folder at: " + pathname);
        }
    }

    public static byte[] readFromFile(String fileName) throws IOException {
        File file = new File( fileName);

        if(!file.exists())
            return null;

        FileInputStream fin = null;
        try {
            // create FileInputStream object
            fin = new FileInputStream(file);
            byte fileContent[] = new byte[(int)file.length()];
            // Reads up to certain bytes of data from this input stream into an array of bytes.
            fin.read(fileContent);
            //create string from byte array
            return fileContent;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            // close the streams using close method
            try {
                if (fin != null) {
                    fin.close();
                }
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return new byte[0];
    }

    public static void writeToFile(byte[] bytesToWrite, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream( fileName);
        fos.write(bytesToWrite);
        fos.close();
    }

    public static byte[] getFile(String path) {
        File file = new File(path);
        if(!file.exists())
            return null;
        FileInputStream fin = null;
        try {
            // create FileInputStream object
            fin = new FileInputStream(file);
            byte fileContent[] = new byte[(int)file.length()];
            // Reads up to certain bytes of data from this input stream into an array of bytes.
            fin.read(fileContent);
            //create string from byte array
            return fileContent;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            ioe.printStackTrace();
        }
        finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return new byte[0];
    }

    public static boolean writeTextToFile(String path, String text) throws IOException {
        Writer output;
        output = new BufferedWriter(new FileWriter(path, true));
        output.append(text+"\n");
        output.close();
        return false;
    }
}

package me.ele.intimate.plugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

/**
 * Created by lizhaoxuan on 2017/12/31.
 */

public class JarUtils {

    /**
     * jar -cvf
     *
     * @param desJar
     * @param jarDir
     * @throws Exception
     */
    public static void jar(File desJar, File jarDir) throws Exception {
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desJar));
        File[] src = jarDir.listFiles();
        jar(out, src);
    }

    /**
     * @param out
     * @param src
     */
    public static void jar(OutputStream out, File[] src) throws Exception {
        jar(out, src, null, null);
    }

    /**
     * @param out
     * @param src
     */
    public static void jar(OutputStream out, File src) throws Exception {
        jar(out, new File[]{src}, null, null);
    }

    /**
     * @param out
     * @param src
     * @param prefix
     * @param man
     * @throws Exception
     */
    public static void jar(OutputStream out, File[] src, String prefix, Manifest man) throws Exception {
        JarOutputStream jout = null;
        if (man == null) {
            jout = new JarOutputStream(out);
        } else {
            jout = new JarOutputStream(out, man);
        }

        if (prefix != null && prefix.trim().length() > 0 && !prefix.equals("/")) {
            if (prefix.charAt(0) == '/') {
                prefix = prefix.substring(1);
            }
            if (prefix.charAt(prefix.length() - 1) != '/') {
                prefix = prefix + "/";
            }
        } else {
            prefix = "";
        }
        for (File f : src) {
            jar(f, prefix, jout);
        }
        jout.close();
    }

    /**
     * @param src
     * @param prefix
     * @param jout
     */
    private static void jar(File src, String prefix, JarOutputStream jout) throws Exception {
        if (src.isDirectory()) {
            prefix = prefix + src.getName() + "/";
            ZipEntry entry = new ZipEntry(prefix);
            entry.setTime(src.lastModified());
            entry.setMethod(ZipEntry.STORED);
            entry.setSize(0l);
            entry.setCrc(0l);
            jout.putNextEntry(entry);
            jout.closeEntry();
            File[] files = src.listFiles();
            if (files != null) {
                for (File file : files) {
                    jar(file, prefix, jout);
                }
            }
        } else {
            byte[] buffer = new byte[8092];
            ZipEntry entry = new ZipEntry(prefix + src.getName());
            entry.setTime(src.lastModified());
            jout.putNextEntry(entry);
            FileInputStream inputStream = new FileInputStream(src);
            int len;
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                jout.write(buffer, 0, len);
            }
            inputStream.close();
            jout.closeEntry();
        }
    }


    /**
     * jar -xvf
     *
     * @param jarFile
     * @param unJarDir
     */
    public static List<String> unJar(File jarFile, File unJarDir) throws Exception {
        List<String> list = new ArrayList<>();

        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(jarFile));
        unJar(inputStream, unJarDir, list);

        return list;
    }

    public static List<String> unJar(InputStream inputStream, File unJarDir, List<String> list) throws Exception {
        if (!unJarDir.exists()) {
            unJarDir.mkdirs();
        }

        JarInputStream jin = new JarInputStream(inputStream);
        byte[] buffer = new byte[8092];

        ZipEntry entry = jin.getNextEntry();
        while (entry != null) {
            String fileName = entry.getName();
            if (File.separatorChar != '/') {
                fileName = fileName.replace('/', File.separatorChar);
            }
            if (fileName.charAt(fileName.length() - 1) == '/') {
                fileName = fileName.substring(0, fileName.length() - 1);
            }
            if (fileName.charAt(0) == '/') {
                fileName = fileName.substring(1);
            }
            String entryName = entry.getName();
            if (entryName.endsWith(".class")) {
                String className = entryName.replace('\\', '.').replace('/', '.');
                list.add(className);
            }

            File file = new File(unJarDir, fileName);
            if (!file.getName().endsWith(".class")) {
                boolean re = file.mkdirs();
            } else {
                File parent = file.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                int len = 0;
                while ((len = jin.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                out.close();
                file.setLastModified(entry.getTime());
            }
            jin.closeEntry();
            entry = jin.getNextEntry();
        }

        Manifest mf = jin.getManifest();
        if (mf != null) {
            File file = new File(unJarDir, "META-INF/MANIFEST.MF");
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            OutputStream out = new FileOutputStream(file);
            mf.write(out);
            out.flush();
            out.close();
        }

        jin.close();

        return list;
    }

}

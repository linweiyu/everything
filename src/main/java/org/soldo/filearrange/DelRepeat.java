package org.soldo.filearrange;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.*;

public class DelRepeat {
    private Map<String, Integer> filesMD5 = new HashMap<>();

    public void doDel(String path) {
        Queue<File> tasks = new PriorityQueue<>();
        tasks.offer(new File(path));
        while (!tasks.isEmpty()) {
            File file = tasks.poll();
            //文件夹跳过
            if(file.getName().contains("$RECYCLE.BIN"))
                continue;
            //较大文件跳过 4G
            if(file.length() / 1024 / 1024 / 1024 >= 3){
                System.out.println("Big File Skip: " + file.getAbsolutePath());
                continue;
            }
            if (file != null) {
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    if (files == null)
                        continue;
                    if (files.length == 0) {
                        boolean isDelete = file.delete();
                        if (isDelete) {
                            System.out.println("Success Delete: " + file.getAbsolutePath());
                        } else {
                            System.out.println("Fail Delete: " + file.getAbsolutePath());
                        }
                    } else {
                        for (File temp : files) {
                            if (temp.isDirectory()) {
                                tasks.offer(temp);
                            } else {
                                deleteFile(temp);
                            }
                        }
                    }
                } else {
                    deleteFile(file);
                }
            }
        }
        System.out.println(filesMD5.size());
    }

    private void deleteFile(File file) {
        //计算MD5
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String md5Value = DelRepeat.getFileChecksum(md, file);
            if (filesMD5.containsKey(md5Value)) {
                boolean isDelete = file.delete();
                if (isDelete) {
                    System.out.println("Success Delete: " + file.getAbsolutePath());
                } else {
                    System.out.println("Fail Delete: " + file.getAbsolutePath());
                }
            } else {
                filesMD5.put(md5Value, 1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }
}

package org.soldo.filearrange;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class FileArrange {

    public static void main(String[] args) {
        String route = "/Volumes/Seagate Backup Plus Drive";
//        String route = "FileArrange";
        File file = new File(route);
        if (file.isDirectory()) {
            DelRepeat delRepeat = new DelRepeat();
            delRepeat.doDel(route);
        } else {
            System.out.println("Param should be a directory");
        }
    }
}

package com.example.myphotoapplicationversion2;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageUtils {
    public static File createFile(File storageDir, String folderName) throws IOException {
        // Create a file name
        String fileName = createUniqueFileName();

        // Use an existing directory or create it if necessary
        File picturesDir = new File(storageDir, folderName);
        if (!(picturesDir.exists())) {
            picturesDir.mkdir();
        }

        // Create the name of the file with suffix .jpg
        File pathToFile = File.createTempFile(
                fileName,
                ".jpg",
                picturesDir
        );

        return pathToFile;
    }

    private static String createUniqueFileName() throws IOException {
        // Create a unique file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timeStamp + "_";

        return fileName;
    }

    // Checks if a volume containing external storage is available
    // for read and write.
    public static boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // Checks if a volume containing external storage is available to at least read.
    public static boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }
}

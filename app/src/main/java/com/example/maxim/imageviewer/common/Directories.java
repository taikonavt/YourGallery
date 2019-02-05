package com.example.maxim.imageviewer.common;

import android.os.Environment;

import com.example.maxim.imageviewer.App;

import java.io.File;

public class Directories {

    public static String getThumbnailDir(){
        String directory = App.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                + File.separator + "thumbs" + File.separator;
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return directory;
    }

    public static String getDirPath() {
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator;
        File dir = new File(directory);
        if (!dir.exists()){
            dir.mkdir();
        }
        return directory;
    }


}

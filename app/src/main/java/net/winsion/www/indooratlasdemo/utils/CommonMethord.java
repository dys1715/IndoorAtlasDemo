package net.winsion.www.indooratlasdemo.utils;

import android.os.Environment;

import net.winsion.www.indooratlasdemo.bean.PointXY;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by D on 2016/5/3 0003.
 */
public class CommonMethord {

    public static void saveFile(String str) {
        String filePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "points.txt";
        } else {
            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "points.txt";
        }
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String ListToStr(List<PointXY> list) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            str.append(i + ":[x=" + list.get(i).getPointX() + ",y=" + list.get(i).getPointY()
                    + ",latitude=" + list.get(i).getLatitude() + ",longitude=" + list.get(i).getLongitude()
                    + "]" + '\n');
        }
        return str.toString();
    }
}

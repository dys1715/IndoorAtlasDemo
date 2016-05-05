package net.winsion.www.indooratlasdemo.utils;

import android.os.Environment;

import net.winsion.www.indooratlasdemo.bean.PointXY;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by D on 2016/5/3 0003.
 * 公共类
 */
public class CommonMethord {

    public static boolean saveFile(String str) {
        String filePath;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "points_" + getCurrentTime() + ".txt";
        } else {
            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "points_" + getCurrentTime() + ".txt";
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
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFile(String fileTime) {
        File file = new File(Environment.getExternalStorageDirectory(), "points_" + fileTime + ".txt");
        try {
            //读取字节流(FileInputStream)并转换为字符流(isr)
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
            //创建字符流缓冲区
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            String str = "";
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                str += line + '\n';
            }
            isr.close();
            bufferedReader.close();
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return "get file failed";
        }
    }

    public static String ListToStr(List<PointXY> list) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            str.append(i)
                    .append(">>[x=")
                    .append(list.get(i).getPointX())
                    .append(",y=")
                    .append(list.get(i).getPointY())
                    .append(",latitude=")
                    .append(list.get(i).getLatitude())
                    .append(",longitude=")
                    .append(list.get(i).getLongitude())
                    .append("]")
                    .append('\n');
        }
        return str.toString();
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyyMMddHH").format(new Date());
    }
}

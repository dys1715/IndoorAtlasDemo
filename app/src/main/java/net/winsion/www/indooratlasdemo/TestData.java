package net.winsion.www.indooratlasdemo;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dys on 2016/5/20 0020.
 * 标志点测试数据
 */
public class TestData {
    public static List<PointF> getMarks(){
        List<PointF> marks = new ArrayList<>();
        marks.add(new PointF(736, 113));
        marks.add(new PointF(776, 200));
        marks.add(new PointF(776, 240));
        marks.add(new PointF(990, 187));
        marks.add(new PointF(990, 276));
        return marks;
    }

    public static List<String> getMarksName() {
        List<String> marksName = new ArrayList<>();
//        for (int i = 0; i < getMarks().size(); i++) {
//            marksName.add("Shop " + (i + 1));
//        }
        marksName.add("饮水机");
        marksName.add("张昌明");
        marksName.add("邓宇思");
        marksName.add("周聪");
        marksName.add("李霈");
        return marksName;
    }
}

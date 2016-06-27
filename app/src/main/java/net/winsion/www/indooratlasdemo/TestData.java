package net.winsion.www.indooratlasdemo;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dys on 2016/5/20 0020.
 * 标志点测试数据
 */
public class TestData {

    public static List<PointF> getNodesList() {
        List<PointF> nodes = new ArrayList<>();
        nodes.add(new PointF(637, 153));
        nodes.add(new PointF(732, 153));
        nodes.add(new PointF(732, 107));
        nodes.add(new PointF(770, 153));
        nodes.add(new PointF(770, 205));
        nodes.add(new PointF(770, 240));
        nodes.add(new PointF(770, 275));
        nodes.add(new PointF(770, 120));
        nodes.add(new PointF(770, 90));
        nodes.add(new PointF(840, 153));
        nodes.add(new PointF(840, 205));
        nodes.add(new PointF(840, 240));
        nodes.add(new PointF(840, 275));
        nodes.add(new PointF(840, 120));
        nodes.add(new PointF(840, 90));
        nodes.add(new PointF(840, 50));
        nodes.add(new PointF(915, 153));
        nodes.add(new PointF(915, 205));
        nodes.add(new PointF(915, 240));
        nodes.add(new PointF(915, 275));
        nodes.add(new PointF(915, 120));
        nodes.add(new PointF(915, 90));
        nodes.add(new PointF(915, 50));
        nodes.add(new PointF(965, 153));
        nodes.add(new PointF(965, 185));
        nodes.add(new PointF(965, 277));
        nodes.add(new PointF(965, 77));
//27
        return nodes;
    }

    public static List<PointF> getNodesContactList() {
        List<PointF> nodesContact = new ArrayList<PointF>();
        nodesContact.add(new PointF(0, 1));
        nodesContact.add(new PointF(1, 2));
        nodesContact.add(new PointF(1, 3));
        nodesContact.add(new PointF(3, 4));
        nodesContact.add(new PointF(3, 7));
        nodesContact.add(new PointF(3, 9));
        nodesContact.add(new PointF(4, 5));
        nodesContact.add(new PointF(5, 6));
        nodesContact.add(new PointF(7, 8));
        nodesContact.add(new PointF(9, 10));
        nodesContact.add(new PointF(9, 13));
        nodesContact.add(new PointF(9, 16));
        nodesContact.add(new PointF(10, 11));
        nodesContact.add(new PointF(11, 12));
        nodesContact.add(new PointF(13, 14));
        nodesContact.add(new PointF(14, 15));
        nodesContact.add(new PointF(16, 17));
        nodesContact.add(new PointF(16, 20));
        nodesContact.add(new PointF(16, 23));
        nodesContact.add(new PointF(17, 18));
        nodesContact.add(new PointF(18, 19));
        nodesContact.add(new PointF(20, 21));
        nodesContact.add(new PointF(21, 22));
        nodesContact.add(new PointF(23, 24));
        nodesContact.add(new PointF(23, 26));
        nodesContact.add(new PointF(24, 25));
//26

        return nodesContact;
    }

    public static List<PointF> getMarks() {
        List<PointF> marks = new ArrayList<>();
        marks.add(new PointF(637, 153));
        marks.add(new PointF(726, 113));
        marks.add(new PointF(770, 200));
        marks.add(new PointF(770, 240));
        marks.add(new PointF(775, 273));
        marks.add(new PointF(990, 187));
        marks.add(new PointF(990, 276));
        marks.add(new PointF(770, 90));
        marks.add(new PointF(915, 90));
        marks.add(new PointF(915, 50));
        marks.add(new PointF(835, 200));
        marks.add(new PointF(835, 240));
        marks.add(new PointF(835, 275));
        marks.add(new PointF(830, 120));
        marks.add(new PointF(830, 80));
        marks.add(new PointF(840, 50));
        return marks;
    }

    public static List<String> getMarksName() {
        List<String> marksName = new ArrayList<>();
//        for (int i = 0; i < getMarks().size(); i++) {
//            marksName.add("Shop " + (i + 1));
//        }
        marksName.add("Enter");
        marksName.add("饮水机");
        marksName.add("小明");
        marksName.add("=_=");
        marksName.add("志银");
        marksName.add("周聪");
        marksName.add("李霈");
        marksName.add("金樑");
        marksName.add("浩亮");
        marksName.add("是谁呢");
        marksName.add("昕昕");
        marksName.add("王蒙");
        marksName.add("武军");
        marksName.add("no body");
        marksName.add("金龙");
        marksName.add("no body+1");

        return marksName;
    }
}

package com.wzq.recyclerdealer;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;

/**
 * Created by fy on 2015/6/5.
 * description:布局高度计算
 */
public class LayoutUtils {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int rows;
        int columns=0;
        int horizontalBorderHeight=0;
        Class<?> clazz=gridView.getClass();
        try {
            //利用反射，取得每行显示的个数
            Field column=clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            columns=(Integer)column.get(gridView);
            //利用反射，取得横向分割线高度
            Field horizontalSpacing=clazz.getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horizontalBorderHeight=(Integer)horizontalSpacing.get(gridView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if(listAdapter.getCount()%columns>0){
            rows=listAdapter.getCount()/columns+1;
        }else {
            rows=listAdapter.getCount()/columns;
        }
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) { //只计算每项高度*行数
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight+horizontalBorderHeight*(rows-1);//最后加上分割线总高度
        gridView.setLayoutParams(params);
    }

    /**
     * 获取Listview的高度，然后设置ViewPager的高度
     *
     * @param listView
     * @return
     */
    public static int setListViewHeightBasedOnChildren1(ListView listView) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
        return params.height;
    }

    /**
     * 判断最后listView中最后一个item是否完全显示出来
     * listView 是集合的那个ListView
     *
     * @return true完全显示出来，否则false
     */

    public static boolean isLastItemVisible(ListView listView) {
        final Adapter adapter = listView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = listView.getLastVisiblePosition();

        /**
         * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
         * internally uses a FooterView which messes the positions up. For me we'll just subtract
         * one to account for it and rely on the inner condition which checks getBottom().
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - listView.getFirstVisiblePosition();
            final int childCount = listView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = listView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= listView.getBottom();
            }
        }

        return false;
    }

    //sp转换成px
    public static int sp2px(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics());
    }

    //dp换算成pix
    public static int dip2px(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }
//
//    public static int dip2px(Context context, float dipValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dipValue * scale + 0.5f);
//    }

    public static int dip2sp(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        if (scale > 2) {
            scale = 2;
        }
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 计算listview的高度是否大于屏幕的高度
     *
     * @param
     * @param context
     */
    public static boolean isExceedScreen(ListView listView, Context context) {
        try {

            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            wm.getDefaultDisplay().getSize(point);
            int y = point.y;

            ListAdapter mAdapter = listView.getAdapter();
            if (mAdapter == null) {
                return false;
            }
            int totalHeight = 0;
            for (int i = 0; i < mAdapter.getCount(); i++) {
                View mView = mAdapter.getView(i, null, listView);
                mView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                mView.measure(0, 0);
                totalHeight += mView.getMeasuredHeight();
                Log.w("HEIGHT" + i, String.valueOf(totalHeight));
            }
            if (totalHeight > y) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

}

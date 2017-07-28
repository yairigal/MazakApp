package project.android.com.mazak.Model;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import net.cachapa.expandablelayout.ExpandableLayout;

/**
 * Created by Yair on 2017-03-21.
 * this class is taken from stackoverflow to help with the a couple lists in a single page to be all opened
 */

public class Utility {

    public static void setListViewHeightBasedOnChildrenWithExpandable(ListView listView, ExpandableLayout currentOpen,boolean plus) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }



        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        if(currentOpen!= null)
            if(plus)
                params.height += currentOpen.getHeight();
            else
                params.height -= currentOpen.getHeight();
        listView.setLayoutParams(params);
        listView.requestLayout();



    }

    public static void setListViewHeightBasedOnChildrenWithDetailsListView(ListView listView, ListView currentOpen, ExpandableLayout layout, boolean plus) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }



        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        if(currentOpen!= null)
            if(plus) { // adding the height of the ListView currentOpen + the height of the expand layout - some random blank space (130)
                params.height +=  calculateTotalHeight(currentOpen) + layout.getHeight() - 130;
            }
            else
                params.height -= currentOpen.getHeight() + 10;
        listView.setLayoutParams(params);
        listView.requestLayout();



    }

    private static int calculateTotalHeight(ListView currentOpen) {
        ListAdapter listAdapter2 = currentOpen.getAdapter();
        int totalHeight2 = 0;
        int desiredWidth2 = View.MeasureSpec.makeMeasureSpec(currentOpen.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter2.getCount(); i++) {
            View listItem = listAdapter2.getView(i, null, currentOpen);
            listItem.measure(desiredWidth2, View.MeasureSpec.UNSPECIFIED);
            totalHeight2 += listItem.getMeasuredHeight();
        }
        return totalHeight2;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }



        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();



    }
}

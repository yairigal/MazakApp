package project.android.com.mazak.Controller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import project.android.com.mazak.Model.IRefresh;
import project.android.com.mazak.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment implements IRefresh {

    View root;
    private WeekView mWeekView;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_schedule, container, false);
        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) root.findViewById(R.id.WeekView);
        mWeekView.setHorizontalFlingEnabled(false);
        mWeekView.setHorizontalFadingEdgeEnabled(false);
        mWeekView.setHorizontalScrollBarEnabled(false);
        mWeekView.setNumberOfVisibleDays(1);
        mWeekView.setFirstDayOfWeek(Calendar.FRIDAY);
        mWeekView.setScrollListener(new WeekView.ScrollListener() {
            @Override
            public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {
                mWeekView.setFirstDayOfWeek(Calendar.FRIDAY);
            }
        });
        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                List<WeekViewEvent> events = new ArrayList<WeekViewEvent>(){{add(new WeekViewEvent(1,"תיכון תוכנה - בירן 105",2017,3,14,2,30,2017,3,14,4,0));}};
                return events;
            }
        };
        mWeekView.setMonthChangeListener(mMonthChangeListener);
        return root;
    }

    @Override
    public void Refresh() {

    }
}

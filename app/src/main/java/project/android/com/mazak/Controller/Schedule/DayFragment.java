package project.android.com.mazak.Controller.Schedule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.zip.Inflater;

import project.android.com.mazak.Model.Entities.ClassEvent;
import project.android.com.mazak.Model.Entities.ScheduleList;
import project.android.com.mazak.Model.GradesModel;
import project.android.com.mazak.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayFragment extends Fragment {


    private View root;
    LayoutInflater mainInflater;
    ScheduleList events;

    public DayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainInflater = inflater;
        root = inflater.inflate(R.layout.fragment_day, container, false);
        getEventsListFromBundle();
        addEventsToView(events);
        return root;
    }

    private void getEventsListFromBundle() {
        events = (ScheduleList) getArguments().getSerializable("events");
    }

    private void addEventsToView(ScheduleList lst){
        for (ClassEvent e:lst) {
            addEvent(e);
        }
    }

    public void addEvent(ClassEvent aEvent){
        LinearLayout main = (LinearLayout) root.findViewById(R.id.DayMainLayout);
        View Mainview = mainInflater.inflate(R.layout.class_event,null);
        ((TextView)Mainview.findViewById(R.id.ClassEvent_CourseName)).setText(aEvent.name);
        ((TextView)Mainview.findViewById(R.id.ClassEvent_ClassRoomPlace)).setText(aEvent.Class);
        ((TextView)Mainview.findViewById(R.id.ClassEvent_Date)).setText(aEvent.startTime +" - "+aEvent.endTime);
        ((TextView)Mainview.findViewById(R.id.ClassEvent_Lecturer)).setText(aEvent.lecturer);
        colorByType(Mainview,aEvent.Type);
        main.addView(Mainview);
    }

    private void colorByType(View mainview, String type) {
        switch (type){
            case "מעבדה":
                mainview.setBackgroundColor(ColorTemplate.rgb("FFA500"));
                break;
            case "שעור":
                mainview.setBackgroundColor(ColorTemplate.rgb("00C000"));
                break;
            case "תרגיל":
                mainview.setBackgroundColor(ColorTemplate.rgb("FFFF80"));
                break;
            case "פרויקט":
                mainview.setBackgroundColor(ColorTemplate.rgb("00C0C0"));
                break;
            default:
                break;
        }
    }


}

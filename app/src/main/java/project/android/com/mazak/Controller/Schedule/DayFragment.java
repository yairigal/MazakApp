package project.android.com.mazak.Controller.Schedule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.utils.ColorTemplate;

import project.android.com.mazak.Model.Entities.ClassEvent;
import project.android.com.mazak.Model.Entities.ScheduleList;
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

    /**
     * gets the classes for that day from the Host.
     */
    private void getEventsListFromBundle() {
        events = (ScheduleList) getArguments().getSerializable("events");
    }

    /**
     * adds all the classes to the UI View.
     * @param lst
     */
    private void addEventsToView(ScheduleList lst){
        addDetails();
        for (ClassEvent e:lst) {
            addEvent(e);
        }
    }

    /**
     * adds the current event to the UI View
     * @param aEvent
     */
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

    /**
     * adds the time lecturer and place to the view.
     */
    public void addDetails(){
        //LinearLayout main = (LinearLayout) root.findViewById(R.id.DayMainLayout);
        //View Mainview = mainInflater.inflate(R.layout.day_details,null);
        String start="",end="",total="";
        if(events.size()!=0)
             start = events.get(0).startTime;
        if(events.size()!=0)
            end = events.get(events.size()-1).endTime;

        total = getTotalHours(events);

        ((TextView)root.findViewById(R.id.dayDetails_StartTimeInput)).setText(start);
        ((TextView)root.findViewById(R.id.dayDetails_EndTimeInput)).setText(end);
        ((TextView)root.findViewById(R.id.dayDetails_StudyTotalInput)).setText(total);
        //main.addView(Mainview);
    }

    /**
     * calculates the total hours of the day.
     * @param events
     * @return
     */
    private String getTotalHours(ScheduleList events) {
        float sum = 0;
        for (ClassEvent e: events)
            sum+=getHours(e);
        return String.valueOf(sum);
    }

    /**
     * get the amout of hours that the current class is.
     * @param ac
     * @return
     */
    private float getHours(ClassEvent ac){
        String start = ac.startTime;
        String end = ac.endTime;
        float startHour = Float.parseFloat(start.split(":")[0]);
        float startMin = Float.parseFloat(start.split(":")[1]);
        float EndHour = Float.parseFloat(end.split(":")[0]);
        float EndMin = Float.parseFloat(end.split(":")[1]);
        float st = (startHour > 12 ? startHour - 12:startHour) + (startMin/60f);
        float ed = (EndHour > 12 ? EndHour - 12:EndHour) + (EndMin/60f);
        return ed - st;
    }

    /**
     * colors the Class card by its type.
     * @param Mainview
     * @param type
     */
    private void colorByType(View Mainview, String type) {
        View mainview = Mainview.findViewById(R.id.cardLayout_ClassEvent);
        switch (type){
            case "מעבדה":
                mainview.setBackgroundColor(ColorTemplate.rgb("FFA500"));
                break;
            case "שעור":
                mainview.setBackgroundColor(ColorTemplate.rgb("00C000"));
                break;
            case "תרגיל":
                mainview.setBackgroundColor(ColorTemplate.rgb("FFFF80"));
                ((TextView)Mainview.findViewById(R.id.ClassEvent_CourseName)).setTextColor(ColorTemplate.rgb("000000"));
                ((TextView)Mainview.findViewById(R.id.ClassEvent_ClassRoomPlace)).setTextColor(ColorTemplate.rgb("000000"));
                ((TextView)Mainview.findViewById(R.id.ClassEvent_Date)).setTextColor(ColorTemplate.rgb("000000"));
                ((TextView)Mainview.findViewById(R.id.ClassEvent_Lecturer)).setTextColor(ColorTemplate.rgb("000000"));
                break;
            case "פרויקט":
                mainview.setBackgroundColor(ColorTemplate.rgb("00C0C0"));
                break;
            default:
                ((TextView)Mainview.findViewById(R.id.ClassEvent_CourseName)).setTextColor(ColorTemplate.rgb("000000"));
                ((TextView)Mainview.findViewById(R.id.ClassEvent_ClassRoomPlace)).setTextColor(ColorTemplate.rgb("000000"));
                ((TextView)Mainview.findViewById(R.id.ClassEvent_Date)).setTextColor(ColorTemplate.rgb("000000"));
                ((TextView)Mainview.findViewById(R.id.ClassEvent_Lecturer)).setTextColor(ColorTemplate.rgb("000000"));
                break;
        }
    }


}

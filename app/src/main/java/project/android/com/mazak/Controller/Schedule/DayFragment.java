package project.android.com.mazak.Controller.Schedule;


import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        ((TextView)Mainview.findViewById(R.id.ClassEvent_CourseName)).setText(aEvent.name + " ("+ aEvent.Type +")");
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
        if(events.size()!=0) {
            start = events.get(0).startTime;
        }
        if(events.size()!=0) {
            end = events.get(events.size() - 1).endTime;
        }

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
        for (ClassEvent e: events) {
            sum += getHours(e);
        }
        sum = Math.round(sum);
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
        Context ctx = getContext();
        boolean colors = ctx != null;

        if(!colors)
            Toast.makeText(ctx, "Colors not working", Toast.LENGTH_SHORT).show();
        type = type.toLowerCase();

        switch (type){
            case "מעבדה":
            case "Lab":
                if(colors) {
                    mainview.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lab));
                    ((TextView)Mainview.findViewById(R.id.ClassEvent_CourseName)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                    ((TextView)Mainview.findViewById(R.id.ClassEvent_ClassRoomPlace)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                    ((TextView)Mainview.findViewById(R.id.ClassEvent_Date)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                    ((TextView)Mainview.findViewById(R.id.ClassEvent_Lecturer)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                }
                break;
            case "שעור":
            case "Lesson":
                if(colors) {
                    mainview.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lesson));
                    ((TextView) Mainview.findViewById(R.id.ClassEvent_CourseName)).setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    ((TextView) Mainview.findViewById(R.id.ClassEvent_ClassRoomPlace)).setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    ((TextView) Mainview.findViewById(R.id.ClassEvent_Date)).setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    ((TextView) Mainview.findViewById(R.id.ClassEvent_Lecturer)).setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                }
                break;
            case "תרגיל":
            case "Exercise":
                mainview.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.targilBack));
//                mainview.setBackgroundColor(ColorTemplate.rgb("FFFF80"));
                ((TextView)Mainview.findViewById(R.id.ClassEvent_CourseName)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                ((TextView)Mainview.findViewById(R.id.ClassEvent_ClassRoomPlace)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                ((TextView)Mainview.findViewById(R.id.ClassEvent_Date)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                ((TextView)Mainview.findViewById(R.id.ClassEvent_Lecturer)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                break;
            case "פרויקט":
            case "Project":
                if(colors) {
                    mainview.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lesson));
                    ((TextView)Mainview.findViewById(R.id.ClassEvent_CourseName)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                    ((TextView)Mainview.findViewById(R.id.ClassEvent_ClassRoomPlace)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                    ((TextView)Mainview.findViewById(R.id.ClassEvent_Date)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                    ((TextView)Mainview.findViewById(R.id.ClassEvent_Lecturer)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                }
                break;
            default:
                ((TextView)Mainview.findViewById(R.id.ClassEvent_CourseName)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                ((TextView)Mainview.findViewById(R.id.ClassEvent_ClassRoomPlace)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                ((TextView)Mainview.findViewById(R.id.ClassEvent_Date)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                ((TextView)Mainview.findViewById(R.id.ClassEvent_Lecturer)).setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                Toast.makeText(ctx, "DEFAULT COLOR", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}

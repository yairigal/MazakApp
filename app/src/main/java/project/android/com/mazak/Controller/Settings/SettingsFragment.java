package project.android.com.mazak.Controller.Settings;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import project.android.com.mazak.Controller.NavDrawerActivity;
import project.android.com.mazak.Model.IRefresh;
import project.android.com.mazak.R;

public class SettingsFragment extends Fragment implements IRefresh {
    private static final String startingPageKey = "startingPage";
    private View view;
    private RadioGroup buttonsLayout;
    private LinearLayout startingLayout;

    @Override
    public void Refresh() {
    }

    public SettingsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.settings_layout, container, false);
            buttonsLayout = (RadioGroup) view.findViewById(R.id.StartingPageRadioButtonsMain);
            buttonsLayout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    String page = NavDrawerActivity.screens.get(i-1).first;
                    writeSettings(page);
                    //Toast.makeText(getContext(),page+" selected",Toast.LENGTH_LONG).show();
                }
            });

            String selectedPage = readSettings(getActivity());
            for (int i=0;i<NavDrawerActivity.screens.size();++i) {
                String page = NavDrawerActivity.screens.get(i).first;
                RadioButton button = new RadioButton(getContext());
                button.setText(page);
                buttonsLayout.addView(button);
                if(page.equals(selectedPage))
                    button.toggle();
            }
        } else { //IF ALREADY INSTANTIATED USE SAME OLD View
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }



    public static String readSettings(Activity activity){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(startingPageKey, "Grades");
    }

    private void writeSettings(String page){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(startingPageKey,page);
        editor.commit();
    }



}

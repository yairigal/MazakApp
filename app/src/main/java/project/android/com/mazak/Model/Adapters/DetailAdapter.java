package project.android.com.mazak.Model.Adapters;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import project.android.com.mazak.Model.Entities.gradeIngerdiants;
import project.android.com.mazak.R;

/**
 * Created by Yair on 2017-07-28.
 */

public class DetailAdapter extends ArrayAdapter<gradeIngerdiants> {

    ArrayList<gradeIngerdiants> list;

    public DetailAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<gradeIngerdiants> objects) {
        super(context, resource, objects);
        this.list = (ArrayList<gradeIngerdiants>) objects;
        //this.list.add(0, new gradeIngerdiants("סוג", "ציון מינימום", "משקל", "מועד א", "מועד ב", "מועד מיוחד", "מועד ג"));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.grade_detail, parent, false);
            gradeIngerdiants current = list.get(position);
            ((TextView) convertView.findViewById(R.id.Type_details)).setText(current.type);
            int calulatedWeightValue = (int) (float) Float.valueOf(current.weight);
            ((TextView) convertView.findViewById(R.id.Weight_details)).setText(String.valueOf(calulatedWeightValue));
            TextView mingrade = ((TextView) convertView.findViewById(R.id.MinGrade_Details));
            mingrade.setText(current.minGrade);
            TextView moeda = ((TextView) convertView.findViewById(R.id.MoedA_detials));
            moeda.setText(current.moedA);
            TextView moedb = ((TextView) convertView.findViewById(R.id.MoedB_Details));
            moedb.setText(current.moedB);
            TextView moedc = ((TextView) convertView.findViewById(R.id.MoedC_Details));
            moedc.setText(current.moedC);
            TextView moeds = ((TextView) convertView.findViewById(R.id.MoedSpec_Details));
            moeds.setText(current.moedSpecial);

            ColorLastGrade(moeda, moedb, moedc, moeds, mingrade.getText().toString());

            return convertView;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    private void ColorLastGrade(TextView moeda, TextView moedb, TextView moedc, TextView moeds, String minGrade) {
        String ma = moeda.getText().toString();
        String mb = moedb.getText().toString();
        String mc = moedc.getText().toString();
        String ms = moeds.getText().toString();
        int mingrade = Integer.parseInt(minGrade);
        if (!mc.equals(""))
            color(moedc, mingrade);
        else // mc = null
            if (!ms.equals(""))
                color(moeds, mingrade);
            else if (!mb.equals(""))
                color(moedb, mingrade);
            else
                color(moeda, mingrade);


    }

    private void color(TextView moed, int mingrade) {
        try {
            int grade = Integer.parseInt(moed.getText().toString());
            if (grade >= mingrade)
                moed.setTextColor(ColorTemplate.rgb("2ecc71")); // green
            else
                moed.setTextColor(ColorTemplate.rgb("e74c3c")); // red
        } catch (Exception ex) {

        }
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        return 0;
    }
}
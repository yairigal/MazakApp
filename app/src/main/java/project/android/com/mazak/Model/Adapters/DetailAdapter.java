package project.android.com.mazak.Model.Adapters;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import project.android.com.mazak.Model.Entities.gradeIngredients;
import project.android.com.mazak.R;

/**
 * Created by Yair on 2017-07-28.
 */

public class DetailAdapter extends ArrayAdapter<gradeIngredients> {

    ArrayList<gradeIngredients> list;

    public DetailAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<gradeIngredients> objects) {
        super(context, resource, objects);
        this.list = (ArrayList<gradeIngredients>) objects;
        //this.list.add(0, new gradeIngredients("סוג", "ציון מינימום", "משקל", "מועד א", "מועד ב", "מועד מיוחד", "מועד ג"));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.grade_detail, parent, false);
            gradeIngredients current = list.get(position);
            ((TextView) convertView.findViewById(R.id.Type_details)).setText(current.type);
            int calulatedWeightValue = (int) (float) Float.parseFloat(current.weight);
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
            if (grade >= mingrade) {
                moed.setTextColor(ContextCompat.getColor(getContext(), R.color.green)); // green
                 }
            else {
                moed.setTextColor(ContextCompat.getColor(getContext(), R.color.red)); // red
                }
        } catch (Exception ex) {
            Toast.makeText(getContext(), getContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
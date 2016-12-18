package project.android.com.android5777_9254_6826.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import project.android.com.android5777_9254_6826.R;
import project.android.com.android5777_9254_6826.model.backend.Backend;
import project.android.com.android5777_9254_6826.model.backend.FactoryDatabase;
import project.android.com.android5777_9254_6826.model.entities.Attraction;
import project.android.com.android5777_9254_6826.model.entities.Business;
import project.android.com.android5777_9254_6826.model.entities.Properties;

/**
 * Created by Yair on 2016-12-14.
 */

public class AttractionListTab extends Fragment {

    Business currentBusiness;
    Backend db;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO get business object from BusinessActivity
        db = FactoryDatabase.getDatabase();
        rootView = inflater.inflate(R.layout.content_attractions, container, false);
        initItemByListView(rootView);
        init(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initItemByListView(rootView);
    }

    public void setBusiness(Business toUpdate){
        currentBusiness = toUpdate;
    }

    void initItemByListView(View v) {
        final Attraction[] myItemList = getAttractionListAsyncTask();
        ListView lv = (ListView) v.findViewById(R.id.attractionsLV);
        ArrayAdapter<Attraction> adapter = new ArrayAdapter<Attraction>(getContext(), R.layout.single_attraction_layout, myItemList) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = View.inflate(getContext(), R.layout.single_attraction_layout, null);

                TextView ID = (TextView) convertView.findViewById(R.id.attractionIDTV);
                TextView Date = (TextView) convertView.findViewById(R.id.DateTV);
                TextView Price = (TextView) convertView.findViewById(R.id.PriceTV);
                TextView Description = (TextView) convertView.findViewById(R.id.DescriptionTV);
                ID.setText(myItemList[position].getAttractionID());
                Date.setText(myItemList[position].getStartDate().toString());
                Price.setText(Float.toString(myItemList[position].getPrice())+"₪");
                Description.setText(myItemList[position].getDescription());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //moveToBusinessActivity(myItemList[position]);
                    }
                });
                return convertView;
            }
        };
        lv.setAdapter(adapter);

        /*LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0;i<myItemList.length;i++) {
            Business item = myItemList[i];
            View view  = inflater.inflate(R.layout.single_business_layout, layout, false);
            TextView Name = (TextView) view.findViewById(R.id.tvName);
            TextView ID = (TextView) view.findViewById(R.id.tvID);
            Name.setText(myItemList[i].getBusinessName());
            ID.setText(myItemList[i].getBusinessID());
            layout.addView(view);
        }*/

    }

    private Attraction[] getAttractionListAsyncTask() {
        //TODO need to change this to real AsyncTask and to get attractions by Business
        AsyncTask<Void,Void,Attraction[]> asyncTask = new AsyncTask<Void, Void, Attraction[]>() {
            @Override
            protected Attraction[] doInBackground(Void... params) {
                return getList(db.getAttractionList(currentBusiness.getBusinessID()));
            }
        };
        asyncTask.execute();
        try {
            return asyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Attraction[] getList(ArrayList<Attraction> bs) {
        Attraction[] toReturn = new Attraction[bs.size()];
        for (int i = 0; i < bs.size(); i++) {
            toReturn[i] = bs.get(i);
        }
        return toReturn;
    }

    private void init(View v){
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAttractionActivity();
            }
        });
    }

    private void moveToAttractionActivity() {
        Intent intent = new Intent(getContext(),AddAttractionActivity.class);
        intent.putExtra("business",currentBusiness);
        startActivity(intent);
    }
}

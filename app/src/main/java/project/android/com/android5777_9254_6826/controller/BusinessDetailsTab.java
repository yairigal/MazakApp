package project.android.com.android5777_9254_6826.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import project.android.com.android5777_9254_6826.R;
import project.android.com.android5777_9254_6826.model.entities.Business;

/**
 * Created by Yair on 2016-12-14.
 */

public class BusinessDetailsTab extends Fragment {
    Business currentBusiness;

    TextView Id;
    TextView name;
    TextView address;
    TextView email;
    TextView website;

    public BusinessDetailsTab(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO get business object from BusinessActivity
        View rootView = inflater.inflate(R.layout.content_business_details, container, false);
        setCurrentBusiness(rootView);
        return rootView;
    }

    public void setBusiness(Business toUpdate){
        currentBusiness = toUpdate;
    }

    public void setCurrentBusiness(View v) {
        Id = (TextView) v.findViewById(R.id.idTV);
        name = (TextView) v.findViewById(R.id.nameTV);
        address = (TextView) v.findViewById(R.id.addressTV);
        email = (TextView) v.findViewById(R.id.emailTV);
        website = (TextView) v.findViewById(R.id.websiteTV);
        Id.setText(currentBusiness.getBusinessID());
        name.setText(currentBusiness.getBusinessName());
        address.setText(currentBusiness.getBusinessAddress().toString());
        email.setText(currentBusiness.getEmail());
        website.setText(currentBusiness.getWebsite().toString());
    }
}

package project.android.com.android5777_9254_6826.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import project.android.com.android5777_9254_6826.R;
import project.android.com.android5777_9254_6826.model.entities.Business;

public class BusinessActivity extends AppCompatActivity {

    Business currentBusiness;
    TextView Id;
    TextView name;
    TextView address;
    TextView email;
    TextView website;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentBusiness = getBusinessFromIntent();
        setContentView(R.layout.activity_business);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(currentBusiness.getBusinessName());
        Id = (TextView) findViewById(R.id.idTV);
        name = (TextView) findViewById(R.id.nameTV);
        address = (TextView) findViewById(R.id.addressTV);
        email = (TextView) findViewById(R.id.emailTV);
        website = (TextView) findViewById(R.id.websiteTV);
        setSupportActionBar(toolbar);
        context = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                moveToAttractionActivity();
            }
        });
        setCurrentBusiness();
    }

    private void moveToAttractionActivity() {
        Intent intent = new Intent(getBaseContext(),AddAttractionActivity.class);
        startActivity(intent);
    }

    //TODO implmenet add attraction
    public void setCurrentBusiness() {
        Id.setText(currentBusiness.getBusinessID());
        name.setText(currentBusiness.getBusinessName());
        address.setText(currentBusiness.getBusinessAddress().toString());
        email.setText(currentBusiness.getEmail());
        website.setText(currentBusiness.getWebsite().toString());
    }

    private Business getBusinessFromIntent() {
        Intent intent = getIntent();
        Business toReturn = (Business) intent.getSerializableExtra("business");
        return toReturn;
    }

}

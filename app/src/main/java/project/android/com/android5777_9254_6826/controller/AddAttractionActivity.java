package project.android.com.android5777_9254_6826.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Date;

import project.android.com.android5777_9254_6826.R;
import project.android.com.android5777_9254_6826.model.backend.Backend;
import project.android.com.android5777_9254_6826.model.backend.FactoryDatabase;
import project.android.com.android5777_9254_6826.model.backend.Provider;
import project.android.com.android5777_9254_6826.model.entities.Attraction;
import project.android.com.android5777_9254_6826.model.entities.Business;
import project.android.com.android5777_9254_6826.model.entities.Properties;

import static android.R.attr.type;

public class AddAttractionActivity extends AppCompatActivity {


    Backend db;
    TextView attractionID;
    TextView attractionName;
    TextView Country;
    TextView StartDate;
    TextView EndDate;
    TextView Description;
    TextView Price;
    Spinner spinner;
    TextView type;
    Attraction att;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FactoryDatabase.getDatabase();
        setContentView(R.layout.activity_add_attraction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Business currentbusiness = getBusinessFromIntent();

        attractionName = (EditText) findViewById(R.id.AttractionName);
        Country = (EditText) findViewById(R.id.AttractionCountry);

        StartDate = (EditText) findViewById(R.id.AttStartDate);
        EndDate = (EditText) findViewById(R.id.AttEndDate);

        Description = (EditText) findViewById(R.id.AttDescription);
        Price = (EditText) findViewById(R.id.AttPrice);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                Properties.AttractionType.getTypes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = (EditText)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = (EditText)parent.getItemAtPosition(0);

            }
        });
        att = new Attraction(attractionID.toString(),
                Properties.AttractionType.valueOf(type.toString()),
                attractionName.toString(),
                Country.toString(),
                StartDate.toString(),
                EndDate.toString(),
                Float.valueOf(Price.toString()),
                Description.toString(),currentbusiness.getBusinessID());
        Button addatt = (Button)  findViewById(R.id.AddAttButton);
        addatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        db.addNewAttraction(att);
                        return null;
                    }
                }.execute();

                Snackbar.make(v,"Business Added!",Snackbar.LENGTH_SHORT).isShown();
                Intent intent = new Intent(getBaseContext(),BusinessesActivity.class);
                intent.putExtra("business", currentbusiness);
                startActivity(intent);

            }

        });


    }

    private Business getBusinessFromIntent() {
        Intent intent = getIntent();
        Business toReturn = (Business) intent.getSerializableExtra("business");
        return toReturn;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AddAttraction Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}

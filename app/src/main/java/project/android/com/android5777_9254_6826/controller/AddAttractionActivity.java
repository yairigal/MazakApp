package project.android.com.android5777_9254_6826.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Date;
import java.util.Locale;

import project.android.com.android5777_9254_6826.R;
import project.android.com.android5777_9254_6826.model.backend.Backend;
import project.android.com.android5777_9254_6826.model.backend.FactoryDatabase;
import project.android.com.android5777_9254_6826.model.backend.Provider;
import project.android.com.android5777_9254_6826.model.entities.Attraction;
import project.android.com.android5777_9254_6826.model.entities.Business;
import project.android.com.android5777_9254_6826.model.entities.Properties;

import static android.R.attr.type;

@RequiresApi(api = Build.VERSION_CODES.N)
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
    String type;
    Attraction att;
    int TextIDClicked;
    Calendar myCalendar = Calendar.getInstance();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @RequiresApi(api = Build.VERSION_CODES.N)
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
                type = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = parent.getItemAtPosition(0).toString();

            }
        });
        datePickerPopup();


        // popup for calendar
        /** StartDate.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override public void onClick(View v) {
        TextIDClicked = v.getId();
        //showPopup(AddAttractionActivity.this);
        datePickerPopup();

        }
        });
         EndDate.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override public void onClick(View v) {
        TextIDClicked = v.getId();
        //showPopup(AddAttractionActivity.this);
        datePickerPopup();

        }
        });
         */
        Button addatt = (Button) findViewById(R.id.AddAttButton);
        addatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (datesAreOK() && restIsFilledOut()) {
                    att = new Attraction(attractionID.toString(),
                            Properties.AttractionType.valueOf(type),
                            attractionName.toString(),
                            Country.toString(),
                            StartDate.toString(),
                            EndDate.toString(),
                            Float.valueOf(Price.toString()),
                            Description.toString(), currentbusiness.getBusinessID());
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            db.addNewAttraction(att);
                            return null;
                        }
                    }.execute();

                    Toast.makeText(getApplicationContext(), "Business Added!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), BusinessesActivity.class);
                    intent.putExtra("business", currentbusiness);
                    startActivity(intent);
                } else {
                    Snackbar.make(v, "Your input is not compatible, please check!", Snackbar.LENGTH_SHORT).isShown();
                }
            }

        });


    }

    private boolean restIsFilledOut() {
        return attractionID.toString().length() > 0 &&
                (type.toString()).length() > 0 &&
                attractionName.toString().length() > 0 &&
                Country.toString().length() > 0 &&
                StartDate.toString().length() > 0 &&
                EndDate.toString().length() > 0 &&
                (Price.toString()).length() > 0 &&
                Description.toString().length() > 0;

    }

    private boolean datesAreOK() {
        String[] strt = StartDate.getText().toString().split("/");
        String[] end = EndDate.getText().toString().split("/");
        Date start = new Date(Integer.parseInt(strt[0]), Integer.parseInt(strt[1]), Integer.parseInt(strt[2]));
        Date ende = new Date(Integer.parseInt(strt[0]), Integer.parseInt(strt[1]), Integer.parseInt(strt[1]));
        return (ende.after(start) || ende.equals(start)) && start.after(new Date());
    }

    private void showPopup(Activity context) {

        // Inflate the popup_layout.xml
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout;
        layout = layoutInflater.inflate(R.layout.content_add_attraction, null, false);
        // Creating the PopupWindow
        final PopupWindow popupWindow = new PopupWindow(
                layout, 400, 400);

        popupWindow.setContentView(layout);
        popupWindow.setHeight(500);
        popupWindow.setOutsideTouchable(false);
        // Clear the default translucent background
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        CalendarView cv = (CalendarView) layout.findViewById(R.id.calendarView);
        cv.setBackgroundColor(Color.BLUE);

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub, i think i dit it

                if (TextIDClicked == StartDate.getId()) {
                    StartDate.setText(dayOfMonth + "/" + month + "/" + year);
                }
                if (TextIDClicked == EndDate.getId()) {
                    EndDate.setText(dayOfMonth + "/" + month + "/" + year);
                }
                popupWindow.dismiss();
                Log.d("date selected", "date selected " + year + " " + month + " " + dayOfMonth);

            }
        });
        popupWindow.showAtLocation(layout, Gravity.TOP, 5, 170);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void datePickerPopup() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(TextIDClicked);
            }

        };

        StartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddAttractionActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                TextIDClicked = StartDate.getId();
            }
        });
        EndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddAttractionActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                TextIDClicked = EndDate.getId();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLabel(int TextViewID) {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (TextViewID == StartDate.getId())
            StartDate.setText(sdf.format(myCalendar.getTime()));
        else
            EndDate.setText(sdf.format(myCalendar.getTime()));
    }

    private Business getBusinessFromIntent() {
        Intent intent = getIntent();
        Business toReturn = (Business) intent.getSerializableExtra("business");
        return toReturn;
    }
}



package project.android.com.android5777_9254_6826.controller;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

import project.android.com.android5777_9254_6826.R;
import project.android.com.android5777_9254_6826.model.backend.Backend;
import project.android.com.android5777_9254_6826.model.backend.FactoryDatabase;
import project.android.com.android5777_9254_6826.model.entities.Account;
import project.android.com.android5777_9254_6826.model.entities.Address;
import project.android.com.android5777_9254_6826.model.entities.Business;

public class BusinessesActivity extends AppCompatActivity {

    Backend db;
    LinearLayout layout;
    Account currentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businesses);
        getAccountfromIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout cbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        db = FactoryDatabase.getDatabase();
        layout = (LinearLayout) findViewById(R.id.nestedScrollView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .show();
                moveToAddBusinessActivity();
            }
        });
        cbar.setTitle("Businesses");
        tempAddBusinessses();
        initItemByListView();
    }

    private void getAccountfromIntent() {
        currentAccount = (Account) getIntent().getSerializableExtra("account");
    }

    private void moveToAddBusinessActivity() {
        Intent toBuss = new Intent(getBaseContext(), AddBusinessActivity.class);
        startActivity(toBuss);
    }

    private void moveToTheNextActivity(Business currBuss) {
        Intent toBuss = new Intent(getBaseContext(), BusinessActivity.class);
        startActivity(toBuss);
    }

    void initItemByListView() {
        final Business[] myItemList = getBusinessesListAsyncTask();
        ListView lv = (ListView) findViewById(R.id.itemsLV);
        ArrayAdapter<Business> adapter = new ArrayAdapter<Business>(this, R.layout.single_business_layout, myItemList) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = View.inflate(BusinessesActivity.this, R.layout.single_business_layout, null);

                TextView Name = (TextView) convertView.findViewById(R.id.tvName);
                TextView ID = (TextView) convertView.findViewById(R.id.tvID);
                Name.setText(myItemList[position].getBusinessName());
                ID.setText(myItemList[position].getBusinessID());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToBusinessActivity(myItemList[position]);
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

    private Business[] getBusinessesListAsyncTask() {
        Business[] toReturn=null;
        AsyncTask<Void,Void,Business[]> as = new AsyncTask<Void, Void, Business[]>() {
            @Override
            protected Business[] doInBackground(Void... params) {
                return getList(db.getBusinessList(Long.toString(currentAccount.getAccountNumber())));
            }
        };
        as.execute();
        try {
            toReturn = as.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
    private Business[] getList(ArrayList<Business> bs) {
        Business[] toReturn = new Business[bs.size()];
        for (int i = 0; i < bs.size(); i++) {
            toReturn[i] = bs.get(i);
        }
        return toReturn;
    }
    private void tempAddBusinessses(){
        for (int i=0;i<100;i++){
            db.addNewBusiness(Long.toString(currentAccount.getAccountNumber()),"ID"+i, "name"+i, new Address("israel", "israel", "rishon"), "adaw@gamil.com", null);
        }
    }
    private void moveToBusinessActivity(Business toSend){
        Intent intent = new Intent(getBaseContext(),BusinessDeatilsActivity.class);
        intent.putExtra("business", toSend);
        startActivity(intent);
    }
}

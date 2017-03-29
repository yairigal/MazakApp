/*
package project.android.com.httpstest.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import project.android.com.httpstest.Model.Entities.Delegate;
import project.android.com.httpstest.Model.Entities.Grade;
import project.android.com.httpstest.Model.Entities.GradesList;
import project.android.com.httpstest.Model.GradesModel;
import project.android.com.httpstest.Model.Filter;
import project.android.com.httpstest.Model.Web.ConnectionData;
import project.android.com.httpstest.Model.Web.WebViewConnection;
import project.android.com.httpstest.R;

public class StartingActivity extends AppCompatActivity {

    WebView webView;
    ListView listView;
    String username, password;
    GradesList original = new GradesList();
    public static Activity ctx;
    GradesList list = new GradesList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_starting);
        setupConnection();
    }

    private void setupConnection() {
        final SearchView search = (SearchView) findViewById(R.id.searchView);
        search.setLayoutParams(new Toolbar.LayoutParams(Gravity.RIGHT));
        search.setMaxWidth(Integer.MAX_VALUE);
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        webView = (WebView) findViewById(R.id.web);
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
        listView = (ListView) findViewById(R.id.lvID);
        WebViewConnection webViewConnection = new WebViewConnection(webView, username, password);
        listView.setVisibility(View.GONE);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.spinner);
        pb.setVisibility(View.VISIBLE);
        search.setEnabled(false);
        final ArrayAdapter<Grade> arrayAdapter = new ArrayAdapter<Grade>(this, R.layout.activity_starting, list) {
            @NonNull
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                try {
                    if (convertView == null) {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.grade, parent, false);
                    }
                    TextView name = (TextView) convertView.findViewById(R.id.courseTv);
                    //TextView mingrade = (TextView) convertView.findViewById(R.id.MinGradeTV);
                    name.setText(list.get(position).name);
                    //mingrade.setText(list.get(position).minGrade);
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(list.get(position));
                        }
                    });
                    return convertView;
                } catch (Exception ex) {

                }
                return convertView;
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };
        webViewConnection.connect(ConnectionData.GradesURL, new Delegate() {
            @Override
            public void function(Object object) {
                GradesList l = (ArrayList<Grade>) object;
                list.clear();
                Collections.reverse(l);
                list.addAll(l);*/
/**//*

                original.addAll(l);
                arrayAdapter.notifyDataSetChanged();
                pb.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                search.setEnabled(true);
                Intent intent = new Intent(StartingActivity.this, NavDrawerActivity.class);
                //listPack pkg = new listPack();
                //HashMap mp = GradesModel.sortBySemesterAndYear(list);
                //intent.putExtra("list",mp);
                startActivity(intent);
            }
        });
        listView.setAdapter(arrayAdapter);

        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                reLoadGrades(original, arrayAdapter);
                return false;
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            Filter flt = new Filter(original);

            @Override
            public boolean onQueryTextSubmit(String query) {
                list = flt.filter(query);
                refreshAdapter(arrayAdapter);
                GradesModel gradesModel = new GradesModel(original);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //reLoadGrades(original,arrayAdapter);
                list = flt.filter(newText);
                refreshAdapter(arrayAdapter);
                return false;
            }
        });
    }

    void showDialog(final Grade gd) {
        Dialog dialog = new Dialog(StartingActivity.this);
        dialog.setContentView(R.layout.dialog_list);
        ListView listView = (ListView) dialog.findViewById(R.id.DialogLV);
        final ArrayList<String> lst = new ArrayList<String>() {{
            add(gd.name);
            add(gd.code);
            add("נ''ז: " + gd.points);
            add("סמסטר: " + gd.semester);
            add("ציון סופי: " + gd.finalGrade);
        }};
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.dialog_list, lst) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.detail, parent, false);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.detail);
                tv.setText(lst.get(position));
                switch (position){
                    case 0:
                        tv.setText(gd.name);
                        break;
                    case 1:
                        tv.setText(gd.code);
                        break;
                    case 2:
                        tv.setText(gd.points);
                        break;
                    case 3:
                        tv.setText(gd.semester);
                        break;
                    case 4:
                        tv.setText(gd.finalGrade);
                        break;
                }

                return convertView;
            }
        });
        dialog.setTitle("Details");
        dialog.show();
    }

    void refreshAdapter(ArrayAdapter adp) {
        adp.notifyDataSetChanged();
    }

    void reLoadGrades(ArrayList<Grade> org, ArrayAdapter adp) {
        list = org;
        adp.notifyDataSetChanged();
    }

    public static void showError() {
        Toast.makeText(ctx, "Check your username and password", Toast.LENGTH_SHORT).show();
        ctx.finish();
    }
}
*/

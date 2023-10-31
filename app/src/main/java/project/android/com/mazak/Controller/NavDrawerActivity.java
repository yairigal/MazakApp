package project.android.com.mazak.Controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

//import com.flurry.android.FlurryAgent;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import project.android.com.mazak.Controller.Appeals.IrurFragment;
import project.android.com.mazak.Controller.Average.AverageFragment;
import project.android.com.mazak.Controller.GradesView.FatherTab;
import project.android.com.mazak.Controller.Login.LoginActivity;
import project.android.com.mazak.Controller.Schedule.ScheduleHost;
import project.android.com.mazak.Controller.Settings.SettingsFragment;
import project.android.com.mazak.Controller.Tests.TestsFragment;
import project.android.com.mazak.Controller.TfilaTimes.MinyanFragment;
import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Database.LoginDatabase;
import project.android.com.mazak.Model.Entities.Delegate;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.Entities.getOptions;
import project.android.com.mazak.Model.IRefresh;
import project.android.com.mazak.Model.Services.LoginService;
import project.android.com.mazak.R;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String FLURRY_API_KEY = "F3Q9JV89QJSSB3YK27RY";
    private static final String policyLink = "http://htmlpreview.github.io/?https://github.com/yairigal/MazakApp/blob/master/privacy_policy.html";
    String username, password;
    String myEmailAdd = "yigalyairn@gmail.com";
    Menu menu;
    FrameLayout frame;
    Database db;
    IRefresh currentFragment;
    LoginDatabase loginDatabase;
//    AdView adView;
    public NavDrawerActivity current;
    private boolean fromWeb = false;
//    Tracker mTracker;
    private AsyncTask<Void, Void, Void> getGrades;
    ProgressBar pb;


    public static ArrayList<Pair<String, Integer>> screens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        current = this;
        setContentView(R.layout.activity_nav_drawer);
        //startActivity(new Intent(this,MinyanTimesActivity.class));

        boolean fromSettings = false;
        pb = (ProgressBar) findViewById(R.id.spinner);
        frame = (FrameLayout) findViewById(R.id.frameNav);

        //starting service and alarm
        //startService(new Intent(this, startAlarmService.class));
        //grades = getGradesFromIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        setupAnalytics();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setItemIconTintList(null);

        //AsyncGetGrades();
        //adding to the nav drawer in runtime
        menu = navigationView.getMenu();
        //menu.add();

        //setupNavigationDrawerItems(menu);

        Factory.getInstance(getApplicationContext());

        screens.add(new Pair<>(getString(R.string.grades), R.id.grades));
        screens.add(new Pair<>(getString(R.string.appeals), R.id.irurs));
        screens.add(new Pair<>(getString(R.string.average_heading), R.id.avgItem));
        screens.add(new Pair<>(getString(R.string.schedule), R.id.ScheudleItem));
        screens.add(new Pair<>(getString(R.string.tfila_times), R.id.TfilaTimesItem));
        screens.add(new Pair<>(getString(R.string.tests), R.id.TestsItem));

        //this checkup if for opening the appeals fragment from the service.
        //String whereToNav = getIntent().getStringExtra("fragment");
        // navigate to selected fragment in the settings
        onNavigationItemSelected(menu.findItem(getScreenIdByName(SettingsFragment.readSettings(current))));

//        setupGoogleAnalyticsTracker();



    }

    private Integer getScreenIdByName(String name) {
        for (int i = 0; i < screens.size(); ++i) {
            if (screens.get(i).first.equals(name)) {
                return screens.get(i).second;
            }
        }
        return screens.get(0).second;
    }

    /**
     * sets up all google analytics tracer stuff
     */
//    private void setupGoogleAnalyticsTracker() {
//        if (mTracker == null) {
//            mTracker = GoogleAnalytics.getInstance(this).newTracker("UA-96616811-1");
//        }
//    }

    /**
     * sets up flurry analytics
     */
//    private void setupAnalytics() {
//        new FlurryAgent.Builder()
//                .withLogEnabled(false)
//                .build(this, FLURRY_API_KEY);
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        fromWeb = intent.getBooleanExtra("refresh", false);
        if (fromWeb) {
            currentFragment.Refresh();
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        sendGoogleAnalyticsData("Grades");

        try {
            Intent toLogin = new Intent(NavDrawerActivity.this, LoginService.class);
            toLogin.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startService(toLogin);

//            setupAd();
            //fromWeb = getIntent().getExtras() == null ? false : getIntent().getExtras().getBoolean("refresh");
            getDatabasesFactory();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * sends to google analytics the page that was accessed
     *
     * @param ScreenName
     */
//    private void sendGoogleAnalyticsData(String ScreenName) {
//        setupGoogleAnalyticsTracker();
//        mTracker.setScreenName(ScreenName);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//    }

    /**
     * gets the database factory.
     *
     * @throws Exception
     */
    private void getDatabasesFactory() throws Exception {
        loginDatabase = LoginDatabase.getInstance(this);
        HashMap<String, String> data = loginDatabase.getLoginDataFromMemory();
        username = data.get("username");
        password = data.get("password");
        db = Factory.getInstance(current);
    }

    /**
     * sets up the google ads ad
     */
//    private void setupAd() {
//        adView = (AdView) findViewById(R.id.adView);
//        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
//        // Optionally populate the ad request builder.
//        adRequestBuilder.addKeyword("Cars");
//        adView.loadAd(adRequestBuilder.build());
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            if (currentFragment != null)
                currentFragment.Refresh();
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.grades) {
            navigateTo(FatherTab.getInstance(), getString(R.string.grades));
        } else if (id == R.id.irurs) {
            navigateTo(IrurFragment.getInstance(), getString(R.string.appeals));
        } else if (id == R.id.feedback_menu_item) {
            sendFeedbackWithLog();
        } else if (id == R.id.avgItem) {
            navigateTo(AverageFragment.getInstance(), getString(R.string.average_heading));
        } else if (id == R.id.ScheudleItem) {
            navigateTo(ScheduleHost.getInstance(), getString(R.string.schedule));
        } else if (id == R.id.TfilaTimesItem) {
            navigateTo(new MinyanFragment(), getString(R.string.tfila_times));
        } else if (id == R.id.TestsItem) {
            navigateTo(new TestsFragment(), getString(R.string.tests));
        } else if (id == R.id.Settings) {
            navigateTo(new SettingsFragment(), getString(R.string.title_activity_settings));
        } else if (id == R.id.telegram_bot_menu) {
            Intent telegram = new Intent(Intent.ACTION_VIEW);
            telegram.setData(Uri.parse(getString(R.string.bot_link)));
            startActivity(telegram);
        } else if (id == R.id.policy_menu_item) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(policyLink));
            startActivity(browserIntent);
        } else {
            popUpLogoutDialog();
        }

           /*else if(id == R.id.menu_item_Schedule){
            navigateTo(new ScheduleFragment(),"Schedule",null);
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * pops up the logout dialog
     */
    private void popUpLogoutDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle(getString(R.string.logout_message));


        adb.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        adb.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }

    private void sendFeedbackWithLog() {
        File outputFile = new File(Environment.getExternalStorageDirectory(),
                "logcat.txt");
        try {
            Runtime.getRuntime().exec(
                    "logcat -f " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        openAppChooser();


/*        Intent intent = new Intent(Intent.ACTION_SEND);
        //intent.setType("text/plain");
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {myEmailAdd });
        intent.putExtra(Intent.EXTRA_SUBJECT, "MazakGrades");
        intent.putExtra(Intent.EXTRA_TEXT, " ");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outputFile));
        startActivity(Intent.createChooser(intent , "Send Feedback :"));*/

/*        Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.fromParts(
                "mailto", myEmailAdd, null));
        startActivity(Intent.createChooser(emailIntent, "Send email..."));*/
    }

    private void openAppChooser() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + getString(R.string.myEmail)));

        Intent telegram = new Intent(Intent.ACTION_VIEW);
        telegram.setData(Uri.parse("https://t.me/YairYigal"));

        Intent chooser = new Intent(Intent.createChooser(telegram, "title"));
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{emailIntent});
        startActivity(chooser);
    }

    /**
     * It shows the chooser app to send the message. It filters out other apps
     * on the chooser dialog and shows only Messaging and whatsapp apps.
     *
     * @param message
     */

    private void showException(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void toggleSpinner(boolean toggle, View toDismiss, ProgressBar toShow) {
        if (toggle) {
            toDismiss.setVisibility(View.GONE);
            //swipeRefreshLayout.setRefreshing(true);
        } else {
            toDismiss.setVisibility(View.VISIBLE);
            //swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Navigates the frame to the current fragment with the current title.
     *
     * @param fgmt
     * @param title
     */
    void navigateTo(Fragment fgmt, String title) {
        //fgmt.setArguments(bundle);
        currentFragment = (IRefresh) fgmt;
        getSupportFragmentManager().beginTransaction().replace(R.id.frameNav, fgmt).commit();
        this.setTitle(title);
//        sendGoogleAnalyticsData(title);
    }

    /**
     * Resetes and setup all Navigation drawer items (Year1, Year2 Year3)...
     *
     * @param menu
     */
    void setupNavigationDrawerItems(Menu menu) {
        //clear the nav drawer
        menu.clear();
        //add the All Tab
        /*        menu.add(R.id.group1,R.id.allitem,Menu.NONE,"Grades").setIcon(R.mipmap.nav_year_icons);
         *//*        //Add all years tab
        for (int i = 0; i < grades.size(); i++)
            menu.add(R.id.group2, Menu.NONE, Menu.NONE, getYearTitle(i + 1)).setIcon(R.mipmap.all_tab_icon);*//*
        menu.add(R.id.group1,R.id.irurs,Menu.NONE,"Appeals").setIcon(R.mipmap.all_tab_icon);

        menu.add(R.id.group1, R.id.avgItem, Menu.NONE, "Average").setIcon(R.mipmap.icon_student);

       // menu.add(R.id.group2,R.id.menu_item_Schedule,Menu.NONE,"Schedule").setIcon(R.mipmap.all_tab_icon);

        //add settings tab.
        menu.add(R.id.group4, settingsId, Menu.NONE, "Settings").setIcon(R.mipmap.icon_settings);



        //Feedback
        menu.add(R.id.group5, R.id.feedback_menu_item, Menu.NONE, "Feedback").setIcon(R.mipmap.icon_email);*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //notify to the alarm to run after the app got killed.
        //sendBroadcast(new Intent("action.APP_KILLED"));
    }

    /**
     * this function executes when the user presses the email.
     *
     * @param view
     */
    public void onEmailClick(View view) {
        sendFeedbackWithLog();
    }

    //region commented

    //region commented
    /*    private void setupLists(GradesList object) {
        GradesList l = object;
        list = new GradesList();
        list.clear();
        list.reverse();
        list.addAll(l);
        original.clear();
        original.addAll(l);
    }*/

    /*    private void navigateToAllFragment() {
       GradesList all = new GradesList();
        for (int i = 1; i <= grades.size(); i++)
            for (int j = 0; j < grades.get(i).size(); j++) {
                Object var1 = grades.get(i);
               GradesList var2 = ((HashMap<Integer,GradesList>) var1).get(j);
                all.addAll(var2);
            }
        Fragment whole = new gradesViewFragment();
        //all.reverse();
        Bundle list = new Bundle();
        //list.putSerializable("list", all);
        //putDelegate(whole,list);
        putYear(whole,list,0);
        whole.setArguments(list);
        try {
            currentFragment = (ISearch) whole;
        } catch (Exception ex) {
            showException(ex.getMessage());
        }
        navigateTo(whole, "All",null);
    }*/
// endregion
    public void AsyncGetGrades(final Delegate after) {
        //final SearchView search = (SearchView) findViewById(R.id.searchView);
        toggleSpinner(true, frame, pb);
        this.setTitle(getString(R.string.loading_data));
        //search.setEnabled(false);
        try {
            GradesList lst;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //region async
        getGrades = new AsyncTask<Void, Void, Void>() {
            String errorMsg;
            GradesList lst;

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    //db.clearDatabase();
                    if (fromWeb) {
                        lst = db.getGrades(getOptions.fromWeb);
                    }
                    else {
                        lst = db.getGrades(getOptions.fromMemory);
                    }
                    if (isCancelled()) {
                        return null;
                    }
                } catch (Exception e) {
                    errorMsg = e.getMessage();
                    if (!errorMsg.equalsIgnoreCase(String.valueOf(R.string.no_grades_error))) {
                        errorMsg = getString(R.string.wrong_user_pass);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (errorMsg == null) {
                    after.function(lst);
                }
                else {
                    showException(errorMsg);
                    finish();
                }
            }
        };
        getGrades.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        ;
        //endregion
    }

    private void onConnectionFinished(GradesList object) {
        try {
            toggleSpinner(false, frame, pb);
            //search.setEnabled(true);
            setupNavigationDrawerItems(menu);
            //select the all fragment first.
            onNavigationItemSelected(menu.findItem(R.id.grades));
        } catch (Exception ex) {
            showException(ex.getMessage());
        }

    }

    /*    private void putYear(Fragment fgmt,Bundle b,int year){
        b.putInt("year",year);
    }*/

    /*    int getYearNum(String title) {
        for (int i = 1; i < 10; i++)
            if (title.equals(getYearTitle(i)))
                return i;
        return 1;
    }*/
    /*    String getYearTitle(int year) {
        String title = "";
        switch (year) {
            case 1:
                title = "First Year";
                break;
            case 2:
                title = "Second Year";
                break;
            case 3:
                title = "Third Year";
                break;
            case 4:
                title = "Fourth Year";
                break;
            case 5:
                title = "Fifth Year";
                break;
            default:
                title = "Other";
                break;
        }
        return title;
    }*/

    /*    private void initSearchView(SearchView search) {

        //set the search view to the right side.
        search.setLayoutParams(new Toolbar.LayoutParams(Gravity.RIGHT));
        search.setMaxWidth(Integer.MAX_VALUE);

        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                currentFragment.clearFilter();
                return false;
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (currentFragment != null)
                    currentFragment.Filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (currentFragment != null)
                    currentFragment.Filter(newText);
                return false;
            }
        });
    }*/

    //endregion


}

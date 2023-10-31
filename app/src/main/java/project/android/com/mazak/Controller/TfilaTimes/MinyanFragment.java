package project.android.com.mazak.Controller.TfilaTimes;


import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import project.android.com.mazak.Controller.GradesView.FatherTab;
import project.android.com.mazak.Model.Entities.TfilaTime;
import project.android.com.mazak.Model.HtmlParser;
import project.android.com.mazak.Model.IRefresh;
import project.android.com.mazak.Model.Web.ConnectionData;
import project.android.com.mazak.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MinyanFragment extends Fragment implements IRefresh {


    private View view;
    private static ListView listView;
    private static ArrayList<TfilaTime> times = new ArrayList<>();
    static ProgressBar pb;
    static ArrayAdapter<TfilaTime> Adapter;

    public MinyanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_minyan_times, container, false);
        listView = (ListView) view.findViewById(R.id.tfilaListView);
        pb = (ProgressBar) view.findViewById(R.id.progressBarTfila);
        getTimes();
        Adapter = new ArrayAdapter<TfilaTime>(getContext(),R.layout.activity_minyan_times,times) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.tfila_time, parent, false);
                }

                TfilaTime current = times.get(position);
                TextView name =((TextView)convertView.findViewById(R.id.tfileName));
                TextView time =((TextView)convertView.findViewById(R.id.tfileTime));
                if(current.isSelected){
                    (convertView.findViewById(R.id.mainLayoutTfilaTime)).setBackgroundColor(ContextCompat.getColor(getContext(),R.color.tfila));
                    name.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                    time.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                }
                name.setText(current.name);
                time.setText(current.time);
                return convertView;
            }

            @Override
            public int getCount() {
                return times.size();
            }
        };
        listView.setAdapter(Adapter);
        return view;
    }

    /**
     * GET request (for the minyans times)
     * @param url
     * @return
     * @throws Exception
     */
    private static String GetPageContent(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        // default is GET
        conn.setRequestMethod("GET");

        conn.setUseCaches(false);

        // act like a browser
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        int responseCode = conn.getResponseCode();
        Log.d("MazakTFILA","\nSending 'GET' request to URL : " + url);
        Log.d("MazakTFILA","Response Code : " + responseCode);

        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Get the response cookies
        return response.toString();

    }

    /**
     * gets the times to the times var
     */
    private static void getTimes() {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                FatherTab.toggleSpinner(true,listView,pb);
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    String html = GetPageContent(ConnectionData.TfilaTimesURL);
                    times = HtmlParser.ParseToTfilaTime(html);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                FatherTab.toggleSpinner(false,listView,pb);
                times = (ArrayList<TfilaTime>) times.clone();
                Adapter.notifyDataSetChanged();
            }
        }.executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR );
    }

    @Override
    public void Refresh() {
        getTimes();
    }
}

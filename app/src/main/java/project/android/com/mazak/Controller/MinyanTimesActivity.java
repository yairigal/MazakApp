package project.android.com.mazak.Controller;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import project.android.com.mazak.Model.Entities.TfilaTime;
import project.android.com.mazak.Model.HtmlParser;
import project.android.com.mazak.Model.Web.ConnectionData;
import project.android.com.mazak.Model.Web.MazakConnection;
import project.android.com.mazak.R;

public class MinyanTimesActivity extends AppCompatActivity {

    MazakConnection connection;
    private ArrayList<TfilaTime> times;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minyan_times);
        getTimes();
        listView = (ListView) findViewById(R.id.tfilaListView);
    }

    private String GetPageContent(String url) throws Exception {

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
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Get the response cookies
        return response.toString();

    }

    private void getTimes() {
        new AsyncTask<Void,Void,Void>(){
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
                listView.setAdapter(new ArrayAdapter<TfilaTime>(getApplicationContext(),R.layout.activity_minyan_times,times) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        if (convertView == null)
                            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tfila_time, parent, false);

                        TfilaTime current = times.get(position);
                        ((TextView)convertView.findViewById(R.id.tfileName)).setText(current.name);
                        ((TextView)convertView.findViewById(R.id.tfileTime)).setText(current.time);
                        return convertView;
                    }
                });
            }
        }.execute();
    }
}

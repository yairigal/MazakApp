package project.android.com.mazak.Model.Web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import project.android.com.mazak.Model.Entities.NameValuePair;

/**
 * Created by Yair on 2017-02-17.
 */

public class MazakConnection {

    public List<String> cookies;
    private HttpsURLConnection conn = null;
    private final String USER_AGENT = "Mozilla/5.0";
    String loginUrl = ConnectionData.LoginUrl;
    String postData;

    public MazakConnection(final String username, final String password) throws UnsupportedEncodingException {
        Object mazakCredentials = ConnectionData.getMazakCredentials(username, password);
        postData = getQuery((List<NameValuePair>) mazakCredentials);
        cookies = null;
    }

    /**
     * logging in to the page
     * we need it only at the start , and after a long period of time.
     *
     * @throws Exception
     */
    private void login() throws Exception {
        CookieHandler.setDefault(new CookieManager());

        // 1. Send a "GET" request, so that you can extract the form's data.
        GetPageContent(loginUrl);
        sendPost(loginUrl, postData);
    }

    /**
     * connecting to the current url page
     * if it needs login we are logging in first.
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String connect(String url) throws Exception {
        //need to login only in the first time.
        if (conn == null)
            login();
        return GetPageContent(url);
    }

    /**
     * Like The Connect function , checks if we need to login , and after that sends a POST instead of GET
     * @param url
     * @return
     */
    public String connectPost(String url,String params) throws Exception {
        //need to login only in the first time.
        if (conn == null)
            login();
        return sendPost(url,params);
    }

    /**
     * returns the Statistics html page
     *
     * @param statLink
     * @return
     * @throws Exception
     */
    public String getStatisticsPage(String statLink) throws Exception {
        //need to login only in the first time.
        if (cookies == null)
            login();
        String postQuery = getQuery(ConnectionData.getStatsPostArguments());
        return sendPost(statLink, postQuery);
    }

    //region web functions

    /**
     * HTTP POST
     *
     * @param url
     * @param postParams
     * @return
     * @throws Exception
     */
    public String sendPost(String url, String postParams) throws Exception {

        URL obj = new URL(url);
        conn = (HttpsURLConnection) obj.openConnection();

        // Acts like a browser
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        //conn.setRequestProperty("Host", "accounts.google.com");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        //for (String cookie : this.cookies) {
         //   conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
       // }
        conn.setRequestProperty("WebViewConnection", "keep-alive");
        //conn.setRequestProperty("Referer", "https://accounts.google.com/ServiceLoginAuth");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

        conn.setDoOutput(true);
        conn.setDoInput(true);

        // Send post request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + postParams);
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
/*        Map<String, List<String>> coo1 = conn.getHeaderFields();
        List<String> coo = coo1.get("Set-Cookie");
        if (coo != null)
            setCookies(coo);*/

        return response.toString();
        // System.out.println(response.toString());

    }

    /**
     * HTTP GET
     *
     * @param url
     * @return
     * @throws Exception
     */
    private String GetPageContent(String url) throws Exception {

        URL obj = new URL(url);
        conn = (HttpsURLConnection) obj.openConnection();

        // default is GET
        conn.setRequestMethod("GET");

        conn.setUseCaches(false);

        // act like a browser
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
/*
        if (cookies != null) {
            for (String cookie : this.cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
        }
*/




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


/*        // Get the response cookies
        Map<String, List<String>> coo1 = conn.getHeaderFields();
        List<String> coo = coo1.get("Set-Cookie");
        if (coo != null)
            setCookies(coo);*/

        return response.toString();

    }

    /**
     * saves the cookies for next time enters.
     *
     * @param cookies
     */
    public void setCookies(List<String> cookies) {
        this.cookies = cookies;
    }

    /**
     * converts the params from NameValuePair to a String.
     *
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
    //endregion
}
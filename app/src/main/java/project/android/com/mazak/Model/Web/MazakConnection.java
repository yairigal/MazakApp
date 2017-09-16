package project.android.com.mazak.Model.Web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import project.android.com.mazak.Model.Adapters.NotebookAdapter;
import project.android.com.mazak.Model.Entities.NameValuePair;
import project.android.com.mazak.Model.Entities.Notebook;

/**
 * Created by Yair on 2017-02-17.
 */

public class MazakConnection implements Cloneable {

    private static final int BUFFER_SIZE = 4096;
    public List<String> cookies;
    private HttpsURLConnection conn = null;
    private final String USER_AGENT = "Mozilla/5.0";
    private String postData;


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
        String loginUrl = ConnectionData.LoginUrl;
        //GetPageContent(loginUrl);
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
     *
     * @param url
     * @return
     */
    public String connectPost(String url, String params) throws Exception {
        //need to login only in the first time.
        if (conn == null)
            login();
        return sendPost(url, params);
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

    public File downloadPDF(String sUrl, Notebook currentNotebook) throws Exception {
        login();
        String saveOutputName = NotebookAdapter.getFileName(currentNotebook);
        URL obj = new URL(sUrl);
        conn = (HttpsURLConnection) obj.openConnection();

        // Acts like a browser
        // default is GET
        conn.setRequestMethod("GET");

        conn.setUseCaches(false);

        // act like a browser
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        int responseCode = conn.getResponseCode();



        if ("gzip".equals(conn.getContentEncoding())) {
            GZIPInputStream reader = new GZIPInputStream(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(saveOutputName);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = reader.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            reader.close();
        } else {
            InputStream inputStream = conn.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(saveOutputName);
            int SumBytes = 0;

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                SumBytes += bytesRead;
            }
            inputStream.close();
            outputStream.close();
        }


        return new File(saveOutputName);
    }

    public File getAndWritePDF(String url, String postParams, Notebook current) throws Exception {

        URL obj = new URL(url);
        conn = (HttpsURLConnection) obj.openConnection();

        // Acts like a browser
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        conn.setRequestProperty("WebViewConnection", "keep-alive");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36");
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


        //InputStream inputStream = conn.getInputStream();
        String saveFilePath = NotebookAdapter.getFileName(current);
        //FileOutputStream outputStream = new FileOutputStream(saveFilePath);
        //int SumBytes = 0;

        //int bytesRead = -1;
        //byte[] buffer = new byte[BUFFER_SIZE];
        //while ((bytesRead = inputStream.read(buffer)) != -1) {
        //   outputStream.write(buffer, 0, bytesRead);
        //   SumBytes += bytesRead;
        // }


        if ("gzip".equals(conn.getContentEncoding())) {
            GZIPInputStream reader = new GZIPInputStream(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(saveFilePath);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = reader.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            reader.close();
        } else {
            InputStream inputStream = conn.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
            int SumBytes = 0;

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                SumBytes += bytesRead;
            }
            inputStream.close();
            outputStream.close();
        }


        return new File(saveFilePath);

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

        Charset set = Charset.forName("UTF-8");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), set));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            //response.append("\n");
        }
        in.close();

        return response.toString();

    }

    /**
     * HTTP GET
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String GetPageContent(String url) throws Exception {

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
                new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
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
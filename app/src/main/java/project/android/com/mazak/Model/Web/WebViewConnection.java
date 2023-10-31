/*
package project.android.com.httpstest.Model.Web;

import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import project.android.com.httpstest.Model.Entities.Delegate;
import project.android.com.httpstest.Model.Entities.Grade;
import project.android.com.httpstest.Model.Entities.gradeIngredients;
import project.android.com.httpstest.Model.HtmlParser;

*/
/**
 * Created by Yair on 2017-02-14.
 *//*


public class WebViewConnection {

    public static String currentHTML;
    String username, password;
    WebView webView;
    String html;
    Delegate callback;
    boolean first = true;
    boolean check;
    boolean getWindow = false;
   GradesList grades;
    String getWindowHtml;

    public WebViewConnection(WebView webView, String username, String password) {
        this.username = username;
        this.password = password;
        this.webView = webView;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        html = "";
        check = false;

        setWebView();

    }

    public void setWebView() {
        webView.addJavascriptInterface(new MyJavascriptInterface(), "HtmlViewer");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String setUN = setJSElement(ConnectionData.USERNAMEID, username);
                String setPW = setJSElement(ConnectionData.PASSWORDID, password);
                String execCMD = execCommandJS(ConnectionData.BUTTONID, "click()");

                if (url.equals(ConnectionData.GradeURL2) && first) {
                    webView.loadUrl(setUN);
                    webView.loadUrl(setPW);
                    webView.loadUrl(execCMD);
                    first = false;
                    return;
                }
                if (url.equals(ConnectionData.afterLoginGrades)) {
                    webView.evaluateJavascript("javascript:window.HtmlViewer.showHTML" +
                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            currentHTML = html;
                            final HtmlParser parser = new HtmlParser();
                           GradesList list = null;
                            try {
                                list = parser.ParseToGrades(currentHTML);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            callback.function(list);
                        }
                    });
                    return;
                }
                //error occurred.
                StartingActivity.showError();
            }
        });
    }

    public void connect(String url, Delegate cb) {
        webView.loadUrl(url);
        callback = cb;
    }

    static String getElementId(String id) {
        return ("javascript:document.getElementById('" + id + "')");
    }

    static String setJSElement(String id, String val) {
        return getElementId(id) + ".value='" + val + "';void(0);";
    }

    static String execCommandJS(String id, String cmd) {
        return getElementId(id) + "." + cmd;
    }

    public String getWindow(String gradeID){
        getWindow = true;
        webView.evaluateJavascript(execCommandJS(gradeID, "javscript:click(this)"), new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String html) {
                getWindowHtml = html;
                getWindow = false;
            }
        });
        //webView.loadUrl(execCommandJS(gradeID,"click()"));
        while (getWindow);
        return getWindowHtml;
    }

    class MyJavascriptInterface {
        MyJavascriptInterface() {
        }

        @JavascriptInterface
        public void showHTML(String html) throws ExecutionException, InterruptedException {
            if(getWindow){
                getWindowHtml = html;
                getWindow = false;
                return;
            }
            currentHTML = html;
            final HtmlParser parser = new HtmlParser();
           GradesList list;
            list = parser.ParseToGrades(currentHTML);
            callback.function(list);
            //region async
*/
/*            new AsyncTask<Void, Void, Void>() {
               GradesList list;
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        list = parser.ParseToGrades(currentHTML);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    callback.function(list);
                }
            }.execute();*//*

            //endregion
        }
    }

    private ArrayList<gradeIngredients> getGradesDetails(Grade g) {
        ArrayList<gradeIngredients> ret = new ArrayList<>();
        String htmlOutput = getWindow(g.subDetailsID);
        return HtmlParser.ParseToingerdiants(html);
    }
}

*/

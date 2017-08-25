package project.android.com.mazak.Controller;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import project.android.com.mazak.Model.Entities.BackgroundTask;
import project.android.com.mazak.Model.Entities.Delegate;
import project.android.com.mazak.Model.Entities.NameValuePair;
import project.android.com.mazak.Model.Web.ConnectionData;
import project.android.com.mazak.Model.Web.MazakConnection;
import project.android.com.mazak.R;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TestActivity extends AppCompatActivity {

    public static String params2 = "__EVENTTARGET=ctl00%24ctl00%24ContentPlaceHolder1%24ContentPlaceHolder1%24grdStudentNotebooks%24ctrl46%24lnkDownload&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwULLTE0MTY2ODcyMThkGAMFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYDBThjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJExvZ2luU3RhdHVzQ29udHJvbCRjdGwwMQU4Y3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRMb2dpblN0YXR1c0NvbnRyb2wkY3RsMDMFSWN0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSR1Y0NvbW1hbmRTaG93JGJ0blNob3cFR2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRTdHVkZW50Tm90ZWJvb2tzDxQrAA5kZGRkZGRkPCsALwACL2RkZGYC%2F%2F%2F%2F%2Fw9kBSljdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJExvZ2luVmlldw8PZAIBZHc8GuE%2FmrRyvK5ODhm1wToz1Jkh&__VIEWSTATEGENERATOR=860DB63F&__SCROLLPOSITIONX=0&__SCROLLPOSITIONY=1169&__EVENTVALIDATION=%2FwEdAEOtW%2BBE4fHaGiI9Xw%2F7j%2F8YCW8vdzVzmzgmuoYuqTP%2BfrzNHFSs7lSNq0%2BdeRyG2iB1Cpam%2F%2BYA6ZwazksodyYwRrg1LO9qTKAvKcUDOXQM2fh1s9h85FxvJUNPOC0sWG0lXFBl1Ur%2Bq13i3uhQH0bdcZoqHMUjs%2B9l00xz9nm1xI6TUEAkUVRmNoEsEpd9nhBln%2Bi5etHD5sCufiCUS9W4ielF5W%2B%2Bl4YEQj4pQtiVuUu6kDA47r8cLlAQrwVfjlG642k4kog2kn7soZiKldG9lNqZSjtSSX60Jww23CPJPEKvmMG2rAzowBczY5cAmKF2nNoRLTYL3BgYXnbuNuIeNgHRJB9jF5vqSbI8lKLG2p%2FnAk3h%2BK0PnlUN%2FCUTODdnR9wBAFo8RyGOg%2F8suX03jpK57qAKPqPzAgVmODcHRTLVQNej7gpcmYT4Cxq2pGxyif4q6umWDZ8UhHEkmgxs9adAcP3Q0zTnYjtdhw1HYJGh%2B35RDSaQtk1S5at7nfwKJCVoIT0ztfQbjMek0nEmadfbAnnpCgwTy7RUtr3FeALS9x4jZ70rna3ctmcyFbC1juwN%2BYR3GRRBKCBD%2F516dI%2BMS5ebJQXnSvZGxM6DNxYr46NDEwu2Vw1FYKayf%2B%2Fxtz6Nr6jhxD1Cfo%2FvVDBsMpFsoUANI1%2B4E%2B9sGd63hLF7Bhswg2UEA27nroDzw92eIgbgSIg%2Bmn7XSxpGUXrXWlCAPbl97eokLY9ZTGvU1H9qUx9JP76mLme3m%2BQP3F7g33LJS0egefYuUh4wksvNSehHQC9JCK39axm7%2FKgVtjSjN1GD%2BU9ZEFFu6eAQY4UB%2BNWJdiaDzZFq%2BpwAlxyiGPlLqJBgmJXmJ9xqcRyZJKXTtR1LbfzrCPQDJvTiOhSFVK04broydvCF7MmH8IV9M4f2Qtsm4TzS1TM5lakNpr1m3IpavNE4AVCvejrhmWLjyT4r%2FXfipii%2B%2B427z0jdQmhlsCSzci4MRW6l1Jty7xl%2B%2FhnNRSKwEtoxlLXT6OODDVg40YOJBlgBUvh1%2BG2ylDpFBy3Hnr1p7mrWxB%2FU2oQPei4O6IR4nkBt2p2aW1gUp5xKZyyw9d57TTwTeB0BDUXkCngeWWDcMSYegsiqLpgh6sVom5yujaYmmUUNd%2BPNQGmaJ8IlJStC%2FEubuB2qewfiM7kYMlAwsLv1vDSwtBhkf5%2BHg9DNWldfES3evAvIFVh9likflK32hhmL5SDyPhNzP3VdqZYbSXdL%2Be9ayuIATjE14EnowbVoX3hWphXAsUs9ohIdq8oF8KP7w4Br4FfLIw269Bzd0B%2FW542CYU8csJxVNquuHxYV0uJgr1s2E%2F4I6B%2BN6tzF9Wc9l%2BDqILX4JOctwjOaakaP0I3TzfJcwIxkaLqKS%2FHUpD2YJNAUKlI8c%2Br%2FzrK7WHGvpf6RMeZEl5kZXpFDWNV4uJNyaVFmAdI%3D&ctl00%24ctl00%24ContentPlaceHolder1%24ContentPlaceHolder1%24hPaidRevacha=true&ctl00%24ctl00%24ContentPlaceHolder1%24ContentPlaceHolder1%24cmbAcademicYears=&ctl00%24ctl00%24ContentPlaceHolder1%24ContentPlaceHolder1%24cmbSemsters=&ctl00%24ctl00%24ContentPlaceHolder1%24ContentPlaceHolder1%24cmbTestTimeTypes=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_full_layout);
        new BackgroundTask(new Delegate() {
            @Override
            public void function(Object obj) {
                //webfunc();
            }
        }).start();
    }

    private void webfunc() {
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new NameValuePair("__EVENTTARGET", "ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$grdStudentNotebooks$ctrl46$lnkDownload"));
        params.add(new NameValuePair("__EVENTARGUMENT", ""));
        params.add(new NameValuePair("__VIEWSTATE", "wEPDwULLTE0MTY2ODcyMThkGAMFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYDBThjdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJExvZ2luU3RhdHVzQ29udHJvbCRjdGwwMQU4Y3RsMDAkY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRMb2dpblN0YXR1c0NvbnRyb2wkY3RsMDMFSWN0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSR1Y0NvbW1hbmRTaG93JGJ0blNob3cFR2N0bDAwJGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkQ29udGVudFBsYWNlSG9sZGVyMSRncmRTdHVkZW50Tm90ZWJvb2tzDxQrAA5kZGRkZGRkPCsALwACL2RkZGYC/////w9kBSljdGwwMCRjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJExvZ2luVmlldw8PZAIBZHc8GuE/mrRyvK5ODhm1wToz1Jkh"));
        params.add(new NameValuePair("__VIEWSTATEGENERATOR", "860DB63F"));
        params.add(new NameValuePair("__SCROLLPOSITIONX", "0"));
        params.add(new NameValuePair("__SCROLLPOSITIONY", "1169"));
        params.add(new NameValuePair("__EVENTVALIDATION", "/wEdAEOtW+BE4fHaGiI9Xw/7j/8YCW8vdzVzmzgmuoYuqTP+frzNHFSs7lSNq0+deRyG2iB1Cpam/+YA6ZwazksodyYwRrg1LO9qTKAvKcUDOXQM2fh1s9h85FxvJUNPOC0sWG0lXFBl1Ur+q13i3uhQH0bdcZoqHMUjs+9l00xz9nm1xI6TUEAkUVRmNoEsEpd9nhBln+i5etHD5sCufiCUS9W4ielF5W++l4YEQj4pQtiVuUu6kDA47r8cLlAQrwVfjlG642k4kog2kn7soZiKldG9lNqZSjtSSX60Jww23CPJPEKvmMG2rAzowBczY5cAmKF2nNoRLTYL3BgYXnbuNuIeNgHRJB9jF5vqSbI8lKLG2p/nAk3h+K0PnlUN/CUTODdnR9wBAFo8RyGOg/8suX03jpK57qAKPqPzAgVmODcHRTLVQNej7gpcmYT4Cxq2pGxyif4q6umWDZ8UhHEkmgxs9adAcP3Q0zTnYjtdhw1HYJGh+35RDSaQtk1S5at7nfwKJCVoIT0ztfQbjMek0nEmadfbAnnpCgwTy7RUtr3FeALS9x4jZ70rna3ctmcyFbC1juwN+YR3GRRBKCBD/516dI+MS5ebJQXnSvZGxM6DNxYr46NDEwu2Vw1FYKayf+/xtz6Nr6jhxD1Cfo/vVDBsMpFsoUANI1+4E+9sGd63hLF7Bhswg2UEA27nroDzw92eIgbgSIg+mn7XSxpGUXrXWlCAPbl97eokLY9ZTGvU1H9qUx9JP76mLme3m+QP3F7g33LJS0egefYuUh4wksvNSehHQC9JCK39axm7/KgVtjSjN1GD+U9ZEFFu6eAQY4UB+NWJdiaDzZFq+pwAlxyiGPlLqJBgmJXmJ9xqcRyZJKXTtR1LbfzrCPQDJvTiOhSFVK04broydvCF7MmH8IV9M4f2Qtsm4TzS1TM5lakNpr1m3IpavNE4AVCvejrhmWLjyT4r/Xfipii++427z0jdQmhlsCSzci4MRW6l1Jty7xl+/hnNRSKwEtoxlLXT6OODDVg40YOJBlgBUvh1+G2ylDpFBy3Hnr1p7mrWxB/U2oQPei4O6IR4nkBt2p2aW1gUp5xKZyyw9d57TTwTeB0BDUXkCngeWWDcMSYegsiqLpgh6sVom5yujaYmmUUNd+PNQGmaJ8IlJStC/EubuB2qewfiM7kYMlAwsLv1vDSwtBhkf5+Hg9DNWldfES3evAvIFVh9likflK32hhmL5SDyPhNzP3VdqZYbSXdL+e9ayuIATjE14EnowbVoX3hWphXAsUs9ohIdq8oF8KP7w4Br4FfLIw269Bzd0B/W542CYU8csJxVNquuHxYV0uJgr1s2E/4I6B+N6tzF9Wc9l+DqILX4JOctwjOaakaP0I3TzfJcwIxkaLqKS/HUpD2YJNAUKlI8c+r/zrK7WHGvpf6RMeZEl5kZXpFDWNV4uJNyaVFmAdI="));
        params.add(new NameValuePair("ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$hPaidRevacha", "true"));
        params.add(new NameValuePair("ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$cmbAcademicYears", ""));
        params.add(new NameValuePair("ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$cmbSemsters", ""));
        params.add(new NameValuePair("ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$cmbTestTimeTypes", ""));
        try {
            MazakConnection connection = new MazakConnection("yyigal", "******");
            connection.connect("https://mazak.jct.ac.il/Student/TestNotebooks.aspx");
            String pdf = connection.sendPost("https://mazak.jct.ac.il/Student/TestNotebooks.aspx", params2);
            FileOutputStream outputStream = openFileOutput("test.pdf", Context.MODE_PRIVATE);
            //outputStream.write(pdf.getBytes());
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/test.pdf");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);

            outputStream = new FileOutputStream(file);
            outputStream.write(pdf.getBytes(Charset.forName("UTF8")));
            outputStream.close();


            Uri path = Uri.fromFile(file);
            Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
            pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfOpenintent.setDataAndType(path, "application/pdf");
            try {
                startActivity(Intent.createChooser(pdfOpenintent, "Your title"));
                System.out.println("Opened file");
            }catch (Exception ex){ex.printStackTrace();}


/*            DownloadManager.Request request = new DownloadManager.Request(Uri.fromFile(file));
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "test.pdf");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); // to notify when download is complete
            request.allowScanningByMediaScanner();// if you want to be available from media players
            DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            manager.enqueue(request);*/


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

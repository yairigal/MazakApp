package project.android.com.mazak.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import project.android.com.mazak.Model.Entities.BackgroundTask;
import project.android.com.mazak.Model.Entities.Delegate;
import project.android.com.mazak.R;

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


}

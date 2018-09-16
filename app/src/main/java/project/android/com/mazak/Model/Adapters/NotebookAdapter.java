package project.android.com.mazak.Model.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Model.Entities.BackgroundTask;
import project.android.com.mazak.Model.Entities.Delegate;
import project.android.com.mazak.Model.Entities.Notebook;
import project.android.com.mazak.R;

/**
 * Created by Yair on 2017-08-03.
 */

public class NotebookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final ArrayList<Notebook> list;
    private final Activity context;
    private final Database db;
    public static Notebook currentPressed = null;
    public static ViewHolderNotebook currentHolder;


    public NotebookAdapter(Activity ctx,ArrayList<Notebook> list) {
        this.list = list;
        this.context = ctx;
        db = Factory.getInstance(ctx);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderNotebook(LayoutInflater.from(parent.getContext()).inflate(R.layout.notebook_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Notebook current = list.get(position);
        final ViewHolderNotebook hold = (ViewHolderNotebook) holder;
        setupOpenBtn(current, hold);
        hold.Name.setText(current.moed);
        hold.Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadNotebook(current, hold);
                enableOpenBtn(hold);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }


    private void setupOpenBtn(Notebook current, ViewHolderNotebook hold) {
        setupOpenBtnEnable(hold, current);
        setupOpenBtnOnClick(hold, current);
    }

    private void setupOpenBtnOnClick(ViewHolderNotebook hold, final Notebook current) {
        hold.OpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File pdf = new File(getFileName(current));
                openFile(pdf);
            }
        });
    }

    private void setupOpenBtnEnable(ViewHolderNotebook hold, Notebook current) {
        String name = getFileName(current);
        File file = new File(name);
        if (file.exists()) {
            enableOpenBtn(hold);
        }
        else {
            disableOpenBtn(hold);
        }
    }

    private void enableOpenBtn(ViewHolderNotebook hold) {
        hold.OpenBtn.setEnabled(true);
        hold.OpenBtn.setTextColor(ColorTemplate.rgb(String.valueOf(R.color.colorAccent)));
        hold.OpenBtn.setBackground(context.getDrawable(R.color.colorPrimary));
    }

    private void disableOpenBtn(ViewHolderNotebook hold) {
        hold.OpenBtn.setEnabled(false);
        hold.OpenBtn.setTextColor(Color.GRAY);
        hold.OpenBtn.setBackgroundResource(android.R.drawable.btn_default);
    }

    private void DownloadNotebook(Notebook pressed, ViewHolderNotebook holder) {
        DownloadNoteBook(pressed, holder);
    }


    public class ViewHolderNotebook extends RecyclerView.ViewHolder {
        private final ImageButton Download;
        private final TextView Name;
        private LinearLayout loadingLayout;
        private final Button OpenBtn;
        private LinearLayout ButtonsLayout;

        public ViewHolderNotebook(View convertView) {
            super(convertView);
            ButtonsLayout = (LinearLayout) convertView.findViewById(R.id.ButtonLayout);
            OpenBtn = (Button) convertView.findViewById(R.id.OpenBtnNotebooks);
            Download = (ImageButton) convertView.findViewById(R.id.NotebookDownloadBtn);
            Name = (TextView) convertView.findViewById(R.id.NotebookNameTV);
            loadingLayout = (LinearLayout) convertView.findViewById(R.id.DownloadNotebookLayout);
        }

    }


    private void setGradeDownloadNotebookButton() {
/*        Button download = (Button) findViewById(R.id.NotebookDownloadBtn);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadNoteBook();
            }
        });*/
    }

    private void DownloadNoteBook(Notebook pressed,ViewHolderNotebook hold) {
        currentPressed = pressed;
        currentHolder = hold;
        askForPermission();
    }

    private void askForPermission() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);

        } else {
            context.onRequestPermissionsResult(3, new String[]{"he"}, new int[]{0, 0});
        }
    }


    private void DownloadPDF(ViewHolderNotebook hold,Notebook currentPressed) throws IOException {
        //if it exists , try opening it.
        downloadAndOpenPDF(hold,currentPressed);
    }

    private void downloadAndOpenPDF(ViewHolderNotebook hold, final Notebook currentPressed) {

        final View DwldBtn = hold.ButtonsLayout;
        final View loadingLayout = hold.loadingLayout;

        new BackgroundTask(new Delegate() {
            @Override
            public void function(Object obj) {
                DwldBtn.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.VISIBLE);
            }
        }, new Delegate() {
            @Override
            public void function(Object obj) {
                try {
                    String url;
                    url = currentPressed.link;
                    File pdf = db.downloadPDF(url,currentPressed);
                    openFile(pdf);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Delegate() {
            @Override
            public void function(Object obj) {
                DwldBtn.setVisibility(View.VISIBLE);
                loadingLayout.setVisibility(View.GONE);
            }
        }).start();

    }

    private void openFile(File pdf) {
        if(Build.VERSION.SDK_INT  < Build.VERSION_CODES.N) {
            Uri path = Uri.fromFile(pdf);
            Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
            pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfOpenintent.setDataAndType(path, "application/pdf");
            try {
                context.startActivity(Intent.createChooser(pdfOpenintent, "Your title"));
                System.out.println("Opened file");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else{
            Uri pdfURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".yairigal.provider", pdf);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfURI, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        }
    }

/*    private void onFinishedGettingNotebooks() throws IOException {
        String url, params, name, moed;
        ArrayList<Notebook> currentNotebook = notebooks.get(currentGrade.code);
        url = ConnectionData.NotebookURL;
        params = db.getConnection().getQuery(ConnectionData.getNoteBookPostArguments(currentNotebook.get(0).link));
        name = currentGrade.code;
        moed = String.valueOf(currentNotebook.get(0).moed);
        File pdf = null;
        try {
            pdf = db.getConnection().getAndWritePDF(url, TestActivity.params2, name, moed, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Uri path = Uri.fromFile(pdf);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setDataAndType(path, "application/pdf");
        try {
            startActivity(Intent.createChooser(pdfOpenintent, "Your title"));
            System.out.println("Opened file");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/

    public void onRequestArrived(int requestCode, int[] grantResults,ViewHolderNotebook hold , Notebook current) {
        switch (requestCode) {
            case 3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    try {
                        DownloadPDF(hold,current);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static String getFileName(Notebook currentPressed) {
        return Uri.withAppendedPath(Uri.parse(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))), currentPressed.time+".pdf").toString();
    }

}

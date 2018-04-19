package project.android.com.mazak.Model.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.utils.ColorTemplate;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.w3c.dom.Text;

import java.util.function.Consumer;

import project.android.com.mazak.Controller.GradesView.gradesViewFragment;
import project.android.com.mazak.Controller.GradesView.singleGradeView;
import project.android.com.mazak.Controller.Statistics.CourseStatisticsActivity;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Model.Entities.BackgroundTask;
import project.android.com.mazak.Model.Entities.Delegate;
import project.android.com.mazak.Model.Entities.Grade;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.Utility;
import project.android.com.mazak.R;

import static project.android.com.mazak.Controller.GradesView.FatherTab.toggleSpinner;
import static project.android.com.mazak.Controller.GradesView.gradesViewFragment.refreshAdapter;

/**
 * Created by Yair on 2017-07-28.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final GradesList list0, list1, list2;
    private GradesList fullList;
    boolean doneLoadingDetails = false;
    public final int ITEM_TYPE_HEADER = 0;
    public final int ITEM_TYPE_GRADE = 1;
    private Context context;
    private ExpandableLayout currentOpen = null;
    private ScrollView mScrollView;

    public MyAdapter(GradesList sem0List, GradesList sem1List, GradesList list, Context ctx, ScrollView mScrollView) {
        this.list0 = sem0List;
        this.list1 = sem1List;
        this.list2 = list;
        fillFullList();
        notifyDataSetChanged();
        this.context = ctx;
        this.mScrollView = mScrollView;
        setHasStableIds(true);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private void fillFullList() {
        this.fullList = new GradesList();

        if (list0.size() != 0) {
            Grade sem0title = new Grade();
            sem0title.name = "סמסטר אלול";
            sem0title.code = "-1";
            fullList.add(sem0title);


            fullList.addAll(list0);
        }


        if (list1.size() != 0) {
            Grade sem1title = new Grade();
            sem1title.name = "סמסטר א";
            sem1title.code = "-1";
            fullList.add(sem1title);

            fullList.addAll(list1);
        }


        if (list2.size() != 0) {
            Grade sem2title = new Grade();
            sem2title.name = "סמסטר ב";
            sem2title.code = "-1";
            fullList.add(sem2title);

            fullList.addAll(list2);
        }
    }

    public class ViewHolderGrade extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView grade;
        TextView Nz;
        Button statsBtn;
        View view;

        public ViewHolderGrade(View convertView) {
            super(convertView);
            Nz = (TextView) convertView.findViewById(R.id.NZTV);
            name = (TextView) convertView.findViewById(R.id.courseTv);
            //statsBtn = (Button) convertView.findViewById(R.id.statsBtn);
            grade = (TextView) convertView.findViewById(R.id.GradeTV);
            view = convertView;
        }

        public void clearAnimation() {
            view.clearAnimation();
        }
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {
        TextView NzTv, TitleName;

        public ViewHolderHeader(View convertView) {
            super(convertView);
            NzTv = (TextView) convertView.findViewById(R.id.NZTV);
            TitleName = (TextView) convertView.findViewById(R.id.courseTv);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_GRADE)
            return new ViewHolderGrade(LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_view, parent, false));
        if (viewType == ITEM_TYPE_HEADER)
            return new ViewHolderHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.grades_title, parent, false));
        // set the view's size, margins, paddings and layout parameters
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Grade current = fullList.get(position);
        int itemtype = getItemViewType(position);
        holder.setIsRecyclable(false);
        //its not a title
        if (itemtype == ITEM_TYPE_GRADE) {
            ViewHolderGrade Gholder = (ViewHolderGrade) holder;
            //Gholder.view.findViewById(R.id.statsBtn).setVisibility(View.VISIBLE);
            Gholder.view.findViewById(R.id.NZTV).setVisibility(View.VISIBLE);
            setNz(Gholder.Nz, current);
            setName(Gholder.name, current);
            setGrade(Gholder.grade, current);
            setOnClick(Gholder.view, current);
            setStatsButton(current, Gholder.view);
            Gholder.view.findViewById(R.id.gradeCard).setBackgroundColor(Color.WHITE);
            setAnimation(Gholder.view, position);
        } else { // it is a title
            ViewHolderHeader Hholder = (ViewHolderHeader) holder;
            setName(Hholder.TitleName, current);
            setNzGroup(Hholder.NzTv, getNz(current));

        }
    }

    private void setGrade(TextView gradeTV, Grade grade) {
        gradeTV.setText(grade.finalGrade);
        setGradeColor(gradeTV, grade.finalGrade);
    }

    private void setGradeColor(TextView gradeView, String finalGrade) {
        try {
            float grade = Float.parseFloat(finalGrade);
            if (grade <= 59)
                gradeView.setTextColor(CourseStatisticsActivity.colors[0]);
            else if (grade <= 64)
                gradeView.setTextColor(CourseStatisticsActivity.colors[1]);
            else if (grade <= 69)
                gradeView.setTextColor(CourseStatisticsActivity.colors[2]);
            else if (grade <= 74)
                gradeView.setTextColor(CourseStatisticsActivity.colors[3]);
            else if (grade <= 79)
                gradeView.setTextColor(CourseStatisticsActivity.colors[4]);
            else if (grade <= 84)
                gradeView.setTextColor(CourseStatisticsActivity.colors[5]);
            else if (grade <= 89)
                gradeView.setTextColor(CourseStatisticsActivity.colors[6]);
            else if (grade <= 94)
                gradeView.setTextColor(CourseStatisticsActivity.colors[7]);
            else if (grade <= 100)
                gradeView.setTextColor(CourseStatisticsActivity.colors[8]);
        } catch (Exception ex) {
            gradeView.setTextColor(Color.GRAY);
        }
    }

    private float getNz(Grade current) {
        int list = -1;
        final float[] sum = {0};
        if (current.name.contains("אלול")) {
            for (Grade g : list0) {
                try {
                    if (!g.droppedOut.equals("true"))
                        sum[0] += Float.parseFloat(g.points);
                } catch (Exception ignored) {
                }
            }
        } else if (current.name.contains("א")) {
            for (Grade g : list1) {
                try {
                    if (!g.droppedOut.equals("true"))
                        sum[0] += Float.parseFloat(g.points);
                } catch (Exception ignored) {
                }
            }
        } else {
            for (Grade g : list2) {
                try {
                    if (!g.droppedOut.equals("true"))
                        sum[0] += Float.parseFloat(g.points);
                } catch (Exception ignored) {
                }
            }
        }

        return sum[0];
    }

    @Override
    public int getItemViewType(int position) {
        Grade current = fullList.get(position);
        if (current.code != null && !current.code.equals("-1"))
            return ITEM_TYPE_GRADE;
        return ITEM_TYPE_HEADER;
    }

    @Override
    public int getItemCount() {
        if (fullList != null)
            return fullList.size();
        return 0;
    }

    private void setBackgroundColor(View convertView) {
        convertView.findViewById(R.id.gradeCard).setBackgroundColor(Color.rgb(194, 217, 237));
    }

    private void setDetails(final View convertView, final Grade gd) {
        //((TextView) convertView.findViewById(R.id.nameDetails)).setText(gd.name);
        ((TextView) convertView.findViewById(R.id.courseIdDetails)).setText(gd.code);
        ((TextView) convertView.findViewById(R.id.courseFinalGrade)).setText("ציון סופי " + gd.finalGrade);

        ((TextView) convertView.findViewById(R.id.Type_details)).setTypeface(null, Typeface.BOLD);
        ((TextView) convertView.findViewById(R.id.Weight_details)).setTypeface(null, Typeface.BOLD);
        ((TextView) convertView.findViewById(R.id.MinGrade_Details)).setTypeface(null, Typeface.BOLD);
        ((TextView) convertView.findViewById(R.id.MoedA_detials)).setTypeface(null, Typeface.BOLD);
        ((TextView) convertView.findViewById(R.id.MoedB_Details)).setTypeface(null, Typeface.BOLD);
        ((TextView) convertView.findViewById(R.id.MoedC_Details)).setTypeface(null, Typeface.BOLD);
        ((TextView) convertView.findViewById(R.id.MoedSpec_Details)).setTypeface(null, Typeface.BOLD);

        final ListView detailsListView = (ListView) convertView.findViewById(R.id.detailsListView);
        final ProgressBar spinner = (ProgressBar) convertView.findViewById(R.id.detailsProgressBar);
        final ArrayAdapter adp = new DetailAdapter(context, R.layout.grade_detail, gd.ingerdiantses);
        detailsListView.setAdapter(adp);

        new BackgroundTask(new Delegate() {
            @Override
            public void function(Object obj) {
                toggleSpinner(true, detailsListView, spinner);
            }
        }, new Delegate() {
            @Override
            public void function(Object obj) {
                try {
                    gd.ingerdiantses = Factory.getInstance().getGradesParts(gd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Delegate() {
            @Override
            public void function(Object obj) {
                toggleSpinner(false, detailsListView, spinner);
                refreshAdapter(adp);
                doneLoadingDetails = true;
                Utility.setListViewHeightBasedOnChildren(detailsListView);
            }
        }).start();

    }

    private void setNz(View convertView, Grade current) {
        TextView Nz = (TextView) convertView;
        float num = Float.parseFloat(current.points);
        try {
            if (num == (int) num)
                Nz.setText((int) num + " נ''ז");
            else
                Nz.setText(num + " נ''ז");
        } catch (Exception e) {
            Nz.setText(num + " נ''ז");
        }
    }

    private void setNzGroup(View convertView, float nz) {
        TextView Nz = (TextView) convertView;
        float num = nz;
        try {
            if (num == (int) num)
                Nz.setText((int) num + " נ''ז");
            else
                Nz.setText(num + " נ''ז");
        } catch (Exception e) {
            Nz.setText(num + " נ''ז");
        }
    }

    private void setName(View convertView, Grade grade) {
        TextView name = (TextView) convertView;
        if (grade.droppedOut != null && grade.droppedOut.equals("true"))
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        name.setText(grade.name);
    }

    private void setOnClick(final View convertView, final Grade grade) {
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDialog(grade);
                /*final ExpandableLayout layout = (ExpandableLayout) convertView.findViewById(R.id.ExpadAbleLayout_Details);
                layout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
                    @Override
                    public void onExpansionUpdate(float expansionFraction, int state) {
                        switch (state) {
                            case 3: // done expanding
                                if (doneLoadingDetails) {
                                    final ListView detailsListView = (ListView) convertView.findViewById(R.id.detailsListView);
                                    Utility.setListViewHeightBasedOnChildren(detailsListView);
                                }
                                //if its the last value , scroll to bottom.
                                if (grade.equals(list2.get(list2.size() - 1))) // last value
                                    scollDown();
                                break;
                        }
                    }
                });

                if (layout.isExpanded()) {
                    layout.collapse();
                    currentOpen = null;
                } else { // if the layout is about to be expanded

                    //check when it is expanded or collapsed and then change the listview size

                    //if someone else is opened -> close it.
                    if (currentOpen != null)
                        currentOpen.collapse();
                    currentOpen = layout;


                    setDetails(convertView, grade);
                    layout.expand();

                }*/
                Intent intet = new Intent(context, singleGradeView.class);
                intet.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intet.putExtra("grade", grade);
                context.startActivity(intet);
            }

            private void scollDown() {
                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.fullScroll(mScrollView.FOCUS_DOWN);
                    }
                });
            }
        });
    }

    private void setStatsButton(final Grade position, View convertView) {
/*        (convertView.findViewById(R.id.statsBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CourseStatisticsActivity.class);
                intent.putExtra("grade", position);
                context.startActivity(intent);
            }
        });*/
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);
    }


    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof ViewHolderGrade)
            ((ViewHolderGrade) holder).clearAnimation();
    }
}
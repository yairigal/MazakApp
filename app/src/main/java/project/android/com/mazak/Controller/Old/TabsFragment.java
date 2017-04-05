package project.android.com.mazak.Controller.Old;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Locale;

import project.android.com.mazak.Controller.GradesView.gradesViewFragment;
import project.android.com.mazak.Model.Entities.GradesList;
import project.android.com.mazak.Model.ISearch;
import project.android.com.mazak.R;

public class TabsFragment extends Fragment implements ISearch {

    HashMap<Integer,GradesList> GradesList;
    private FragmentTabHost mTabHost;
    int year;
    ISearch currentFragmet;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TabsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_year_list,container,false);
/*        DemoCollectionPagerAdapter mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(
                getFragmentManager());
        ViewPager mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);*/
        setupTabs(v);

        //MakeTabs(v);


        return v;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getYear();
        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
  }

    public void getYear(){
        year = getArguments().getInt("year");
    }

    private View setTabs(){
        mTabHost = new FragmentTabHost(getActivity());
        //mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_semester);

        mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("Simple"),
                gradesViewFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("contacts").setIndicator("Contacts"),
                gradesViewFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("custom").setIndicator("Custom"),
                gradesViewFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("throttle").setIndicator("Throttle"),
                gradesViewFragment.class, null);

        return mTabHost;
    }

    private void MakeTabs(View v) {
        mTabHost = (FragmentTabHost) v.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        Bundle b0 = new Bundle();
        b0.putSerializable("list",GradesList.get(0));
        Bundle b1 = new Bundle();
        b1.putSerializable("list",GradesList.get(1));
        Bundle b2 = new Bundle();
        b2.putSerializable("list",GradesList.get(2));


/*        gradesViewFragment fragment = new gradesViewFragment(b0);
        mTabHost.addTab(mTabHost.newTabSpec("sem0").setIndicator("Semester Alul"),
                gradesViewFragment.class,b0);
        gradesViewFragment fragment1 = new gradesViewFragment(b1);
        mTabHost.addTab(mTabHost.newTabSpec("sem1").setIndicator("Semester A"),
                gradesViewFragment.class, b1);
        gradesViewFragment fragment2 = new gradesViewFragment(b2);
        mTabHost.addTab(mTabHost.newTabSpec("sem2").setIndicator("Semester B"),
                gradesViewFragment.class, b2);*/

        mTabHost.setCurrentTab(0);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mTabHost.setCurrentTab(1);
    }

    @Override
    public void Filter(String query) {
        currentFragmet.Filter(query);
    }

    @Override
    public void clearFilter() {
        currentFragmet.clearFilter();
    }

    @Override
    public void Refresh() {

    }

    class DemoCollectionPagerAdapter extends FragmentPagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Bundle args = new Bundle();
            args.putSerializable("list",GradesList.get(i));
            // Our object is just an integer :-P
            //args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
            //fragment.setArguments(args);
            return new gradesViewFragment();
        }

        @Override
        public int getCount() {
            return GradesList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Semester " + (position + 1);
        }
    }

    private void setupTabs(View v) {
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabLayoutYear);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewPagerYear);

        viewPager.setAdapter(new SamplePageAdapter(getFragmentManager()));
        tabLayout.setupWithViewPager(viewPager,true);
        if(checkHebrew())
            tabLayout.getTabAt(2).select();
    }

    private class SamplePageAdapter extends FragmentStatePagerAdapter {


        public SamplePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle b = new Bundle();
            b.putInt("year",year);
            b.putInt("semester",position);
            Fragment fragment = new gradesViewFragment();
            fragment.setArguments(b);
            currentFragmet = (ISearch) fragment;
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(checkHebrew()){
                switch (position){
                    case 0:
                        return "Semester 2";
                    case 1:
                        return "Semester 1";
                    default:
                        return "Semester Elul";
                }
            }
            switch (position){
                case 0:
                    return "Semester Elul";
                case 1:
                    return "Semester 1";
                default:
                    return "Semester 2";
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    boolean checkHebrew()   {
        return Locale.getDefault().toString().equals("iw_IL") || Locale.getDefault().toString().equals("he_IL");
    }
}

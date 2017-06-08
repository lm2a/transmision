package com.indra.elections.peruvian.fragments;



import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

import com.indra.elections.R;
import com.indra.elections.adapters.FragmentAdapter;
import com.indra.elections.customcontrols.CustomTextViewOpenSans;
import com.indra.elections.customcontrols.DisableSwapViewPager;
import com.indra.elections.customcontrols.SwipeDirection;
import com.indra.elections.model.FTIPELE;

import com.indra.elections.peruvian.adapters.MyPageAdapter;
import com.indra.elections.utils.ElectionsDBHelper;
import com.indra.elections.utils.LogManager;


import java.util.ArrayList;
import java.util.List;

public class CandidatosResultadosFragment extends Fragment {


    View mLayout;

    private DisableSwapViewPager mViewPager;
    private TabHost mTabhost;
    public ArrayList<Fragment> fragments;
    private FragmentPagerAdapter mPagerAdapter;
    private FTIPELE[] eleccionesBBBDD;

    private HorizontalScrollView sv;


    @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);


   }

    @Override
    public void onResume() {
        super.onResume();
        refreshElections();

        setAll();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment

        mLayout = inflater.inflate(R.layout.candidatos_resultados_fragment, parent, false);

        return mLayout;
    }

    private void refreshElections(){

        eleccionesBBBDD = ElectionsDBHelper.getEleccionPestanas();
    }


    //----------- Tabs-Viewpager clases -----------------------------------


    public void setAll(){
        mViewPager = (DisableSwapViewPager) mLayout.findViewById(R.id.pager);

        // Tab Initialization
        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        mPagerAdapter = new MyPageAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(new PageChangeListener());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CandidatosResultadosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CandidatosResultadosFragment newInstance() {
        CandidatosResultadosFragment fragment = new CandidatosResultadosFragment();
        return fragment;
    }

    class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }

        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            return v;
        }

    }

    public class TabChangeListener implements TabHost.OnTabChangeListener{

        @Override
        public void onTabChanged(String tabId) {
            int pos = mTabhost.getCurrentTab();
            mViewPager.setCurrentItem(pos);
        }
    }

    public class PageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int pos = mViewPager.getCurrentItem();
            mTabhost.setCurrentTab(pos);
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }



    private void initialiseTabHost() {
        mTabhost = (TabHost) mLayout.findViewById(android.R.id.tabhost);
        mTabhost.setup();

        for(int i = 0; i < eleccionesBBBDD.length; i++){
            addTab(this.mTabhost.newTabSpec("Tab"+i).setIndicator(eleccionesBBBDD[i].getDESCETIQ()));
        }


        mTabhost.setOnTabChangedListener(new TabChangeListener());
    }




    private void addTab(TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new TabFactory(getActivity()));
        mTabhost.addTab(tabSpec);
    }

    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();

        for(int i = 0; i < eleccionesBBBDD.length; i++)
        {
            EleccionCandidatosListaResultadosFragment f = EleccionCandidatosListaResultadosFragment.newInstance(eleccionesBBBDD[i].getCODELE());
            fList.add(f);
        }

        return fList;
    }


}


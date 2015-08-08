package com.cert.eli.gigigoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import android.support.design.widget.TabLayout;

import com.cert.eli.gigigoapp.Utils.Photo;
import com.cert.eli.gigigoapp.Utils.Utility;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DialogFilter.DialogInterfaceFilter,GalleryGridFragment.NewActivityIntent {

    public static final String DIALOG_TAG = "DialogFilter";

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_PHOTO = "photo";
    public static final String EXTRA_DESC = "desc";
    public static final String EXTRA_UP = "ups";
    public static final String EXTRA_DOWN = "down";
    public static final String EXTRA_SCORE = "score";

    Adapter adapter;
    public static int actualPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this) .build();
        ImageLoader.getInstance().init(config);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if(!Utility.verificaConexion(this)){
            Snackbar.make(findViewById(android.R.id.content), R.string.no_internet, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        Utility.TYPE_VIEW = Utility.TYPE_GRID;

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(GalleryGridFragment.init(0), getString(R.string.option_one));
        adapter.addFragment(GalleryGridFragment.init(1), getString(R.string.option_two));
        adapter.addFragment(GalleryGridFragment.init(2),getString(R.string.option_three));
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList();
        private final List<String> mFragmentTitles = new ArrayList();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            MainActivity.actualPosition=position;
            return mFragments.get(position);
        }

        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }
        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(adapter==null){
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            if (viewPager != null) {
                setupViewPager(viewPager);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.action_settings:

                return true;
            case R.id.action_filter:
                openFilter();
                break;
            case R.id.action_about:
                initAbout();
                break;
            case R.id.action_list: //Cambia la vista a List o Grid
                if(Utility.TYPE_VIEW==Utility.TYPE_GRID) {
                    Utility.TYPE_VIEW = Utility.TYPE_LIST;
                    item.setIcon(R.drawable.ic_action_view_as_grid);
                }else{
                    Utility.TYPE_VIEW = Utility.TYPE_GRID;
                    item.setIcon(R.drawable.ic_action_view_as_list);
                }
                adapter.notifyDataSetChanged();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Inicia la actividad AcercaDe
     */
    private void initAbout(){
        Intent i = new Intent(this,AcercaDe.class);
        startActivity(i);
    }

    /**
     * Abre el dialogo para cambiar la preferencia de showViral
     */
    private void openFilter(){
        DialogFilter dialog = new DialogFilter();
        dialog.show(getFragmentManager(),DIALOG_TAG);
    }

    /**
     * Inicia Deatials Activity
     * @param photo
     */
    @Override
    public void newActivity(Photo photo) {
        Intent i =new Intent(this,Details.class);
        i.putExtra(EXTRA_TITLE,photo.getTitle());
        i.putExtra(EXTRA_DESC,photo.getDescription());
        i.putExtra(EXTRA_PHOTO,photo.getUrl());
        i.putExtra(EXTRA_UP,photo.getUps());
        i.putExtra(EXTRA_DOWN,photo.getDowns());
        i.putExtra(EXTRA_SCORE,photo.getScore());
        startActivity(i);
    }

    /**
     * Actualiza la sección USER.
     * @param check
     */
    @Override
    public void onDialogOk(boolean check) {
        Utility.changeViralOption(check,this);
        GalleryGridFragment fragment = (GalleryGridFragment)adapter.getItem(actualPosition);
        if(fragment!=null) {
            fragment.updateData();
        }
    }

}

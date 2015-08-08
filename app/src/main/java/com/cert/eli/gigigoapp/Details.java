package com.cert.eli.gigigoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.cert.eli.gigigoapp.Utils.GradientOverImageDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;


public class Details extends AppCompatActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_PHOTO = "photo";
    public static final String EXTRA_DESC = "desc";
    public static final String EXTRA_UP = "ups";
    public static final String EXTRA_DOWN = "down";
    public static final String EXTRA_SCORE = "score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        final String title = intent.getStringExtra(EXTRA_TITLE);
        final String photo = intent.getStringExtra(EXTRA_PHOTO);
        final String desc = intent.getStringExtra(EXTRA_DESC);
        final String up = intent.getStringExtra(EXTRA_UP);
        final String down = intent.getStringExtra(EXTRA_DOWN);
        final String score = intent.getStringExtra(EXTRA_SCORE);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(title);

        loadInfo(photo, desc, up, down, score);
    }

    private void loadInfo(String url,String desc, String up, String down,String score) {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        TextView txtDesc = (TextView)findViewById(R.id.txtDescDetail);
        TextView txtUps = (TextView)findViewById(R.id.txtUps);
        TextView txtDowns = (TextView)findViewById(R.id.txtDowns);
        TextView txtScore = (TextView)findViewById(R.id.txtScore);

        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).displayer(new BitmapDisplayer() {
                    @Override
                    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                        int gradientStartColor = Color.argb(0, 0, 0, 0);
                        int gradientEndColor = Color.argb(255, 0, 0, 0);
                        GradientOverImageDrawable gradientDrawable = new GradientOverImageDrawable(getResources(), bitmap);
                        gradientDrawable.setGradientColors(gradientStartColor, gradientEndColor);
                        imageAware.setImageDrawable(gradientDrawable);
                    }
                })
                .build();
        ImageLoader.getInstance().displayImage(url, imageView,options);
        if(desc.equals("null")){
            txtDesc.setText(R.string.desc_null);
        }else {
            txtDesc.setText(desc);
        }
        txtUps.setText(up);
        txtDowns.setText(down);
        txtScore.setText(score);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.cert.eli.gigigoapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.cert.eli.gigigoapp.Utils.GalleryListAdapter;
import com.cert.eli.gigigoapp.Utils.GridviewAdapter;
import com.cert.eli.gigigoapp.Utils.JSONObjectRequestCustom;
import com.cert.eli.gigigoapp.Utils.Photo;
import com.cert.eli.gigigoapp.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eli on 06/08/15.
 */
public class GalleryGridFragment extends Fragment implements Response.Listener,Response.ErrorListener {

    GridView gv;
    ListView lv;
    GridviewAdapter adapter;
    GalleryListAdapter adapterList;


    List<Photo> listPhotos;
    private RequestQueue mQueue;
    public static final String REQUEST_TAG = "VolleyActivity";
    int val;

    NewActivityIntent mCallback;

    interface NewActivityIntent{
        void newActivity(Photo photo);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (NewActivityIntent)activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public static GalleryGridFragment init(int val) {
        GalleryGridFragment fragment= new GalleryGridFragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        val = getArguments() != null ? getArguments().getInt("val") : 1;
        listPhotos = new ArrayList<Photo>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.frag, container,
                false);

        gv = (GridView) layoutView.findViewById(R.id.grid_view);
        lv = (ListView) layoutView.findViewById(R.id.listView);

        adapter = new GridviewAdapter(getActivity(),listPhotos);
        adapterList = new GalleryListAdapter(getActivity(),listPhotos);

        gv.setAdapter(adapter);
        lv.setAdapter(adapterList);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Photo photo = (Photo)parent.getItemAtPosition(position);
                mCallback.newActivity(photo);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Photo photo = (Photo)parent.getItemAtPosition(position);
                mCallback.newActivity(photo);
            }
        });



        return layoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateData();
    }

    private String createURL(){
        String section="";
        switch (val){
            case 0:
                section = "hot";
                break;
            case 1:
                section = "top";
                break;
            case 2:
                section = "user";
                break;
        }

        final String BASE_URL =
                "https://api.imgur.com/3/gallery/user/viral/0.json";
        final String SECTION_PATH=section;
        final String SORT_PATH ="viral";
        final String PAGE_PATH ="0";
        final String SHOW_VIRAL = "showViral";
        String OPTION_VIRAL ="true";

        if(val==2) {
            String viral = "true";
            boolean optionViral = Utility.getViralOption(getActivity());
            if (optionViral) {
                viral = "false";
            }
            OPTION_VIRAL = viral;
        }

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.imgur.com")
                .appendPath("3")
                .appendPath("gallery")
                .appendPath(SECTION_PATH)
                .appendPath(SORT_PATH)
                .appendPath(PAGE_PATH)
                .appendQueryParameter(SHOW_VIRAL, OPTION_VIRAL)
                .appendEncodedPath(".json");

        return builder.build().toString();
    }

    public void updateData(){

        if (mQueue == null) {
            //Instanciar el Caché
            Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 10 * 1024 * 1024);
            //Iniciar la red para usar HTTPCONNECT
            Network network = new BasicNetwork(new HurlStack());
            //Instanciar mQueue con caché y la red
            mQueue = new RequestQueue(cache, network);
            // Iniciar el request
            mQueue.start();
        }

        String url = createURL();

        Log.i("GALLERY",url);
        // Request a string response from the provided URL.
        final JSONObjectRequestCustom jsonRequest = new JSONObjectRequestCustom(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(Object response) {
        listPhotos.clear();
        try {

            JSONObject mainJson = new JSONObject(response.toString());


            String success = mainJson.getString("success");
            Log.i("JSON","Success: "+success);

            if(success.contentEquals("true")) {

                JSONArray data = mainJson.getJSONArray("data");
                Log.i("JSON Res", "Res: " + data.length());

                for (int i = 0; i < data.length(); i++) {
                    JSONObject ob = data.getJSONObject(i);
                    String url_img = ob.getString("link");
                    String title = ob.getString("title");
                    String description = ob.getString("description");

                    try {
                        String type = ob.getString("type");
                        String upvotes = ob.getString("ups");
                        String down = ob.getString("downs");
                        String score = ob.getString("score");

                        listPhotos.add(new Photo(title, url_img, description,upvotes,down,score));
                    }catch (JSONException e){

                    }

                }

                if(Utility.TYPE_VIEW==Utility.TYPE_GRID) {
                   lv.setVisibility(View.GONE);
                   gv.setVisibility(View.VISIBLE);
                   adapter.notifyDataSetChanged();

                }else{
                    gv.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                   adapterList.notifyDataSetChanged();
                }
            }else{
                Snackbar.make(getView(), R.string.query_fail, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
            if (mQueue != null) {
                mQueue.cancelAll(REQUEST_TAG);
            }

    }

    public void onDestroy(){
        super.onDestroy();
        mCallback=null;
        adapter=null;
        adapterList=null;
    }
}

package com.example.newsaggregator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsaggregator.databinding.ActivityMainBinding;
import com.example.newsaggregator.databinding.DrawerItemBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //API KEY fc7eef8af5874f0eb9d3552171fa3681

    private Menu drawerMenu;
    private ViewPager2 viewPager;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;

    private final ArrayList<Article> newsArticleList = new ArrayList<>();

    private final HashMap<String, ArrayList<String>> categoryData = new HashMap<>();
    private final HashMap<String,String> channels = new HashMap<>();
    private final HashMap<String,Integer> textColors = new HashMap<>();


    private ActionBarDrawerToggle drawerToggleObject;

    private ArrayAdapter<String> drawerlistAdapter;
    articlePage articleAdapter;

    private ArrayList<String> channelList = new ArrayList<>();



    private RequestQueue apiRequest;
    String selectedChannel;
    String selectedChannelId;
    String api = "https://newsapi.org/v2/sources?apiKey=fc7eef8af5874f0eb9d3552171fa3681";

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        apiRequest = Volley.newRequestQueue(this);



        drawerLayout = binding.mainDrawerLayout;
        viewPager = binding.mainPager;
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        drawerListView = binding.mainDrawerList;


        drawerToggleObject = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close);

        drawerlistAdapter = new ArrayAdapter<String>(this, R.layout.drawer_item,channelList){

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                TextView textView=(TextView) view.findViewById(R.id.channels);
                int colour=textView.getCurrentTextColor();
                String name = textView.getText().toString();
                for(String s : categoryData.keySet()){
//                    if(s.equals("ALL")){
//                        continue;
//                    }
                    if(categoryData.get(s).contains(name) && !s.equalsIgnoreCase("All")){
                        colour = textColors.get(s);
                    }
                }
                textView.setTextColor(colour);
                return textView;
            }
        };
        drawerListView.setAdapter(drawerlistAdapter);
        if(hasNetworkConnection())
            downloadData();
        else{
            setTitle("No Network");
        }

        drawerListView.setOnItemClickListener(
                (parent, view,position,id) -> {
                    drawerLayout.closeDrawer(drawerListView);
                    String channel = channelList.get(position);
                    String channelId = (channels.get(channel).toString());
                    selectedChannel = channel;
                    selectedChannelId = channelId;


                    newsArticleList.clear();
                    downloadArticle(channelId);
                }
        );
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        articleAdapter = new articlePage(this,newsArticleList);
        viewPager.setAdapter(articleAdapter);



    }

    private boolean hasNetworkConnection()
    {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    void downloadArticle(String id){
        setTitle(selectedChannel);
        String urlLink="https://newsapi.org/v2/top-headlines?sources="+ id +"&apiKey=fc7eef8af5874f0eb9d3552171fa3681";

        Response.Listener<JSONObject> listener =
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray articlesArray = response.getJSONArray("articles");
                            for (int j = 0;j < articlesArray.length();j++)
                            {
                                JSONObject jsonObject = articlesArray.getJSONObject(j);
                                String author = jsonObject.getString("author");

                                String title = jsonObject.getString("title");

                                String url = jsonObject.getString("url");

                                String urlToImage = jsonObject.getString("urlToImage");

                                String publishedAt = jsonObject.getString("publishedAt");

                                String desc = jsonObject.getString("description");

                                newsArticleList.add(new Article(author,title,desc,url,urlToImage,publishedAt));

                            }

                            articleAdapter.notifyItemRangeChanged(0, newsArticleList.size());


                        } catch (Exception e) {
                        }
                    }
                };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try
                {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlLink,
                        null, listener, error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "News-App");
                        return headers;
                    }
                };
        // Add the request to the RequestQueue.
        apiRequest.add(jsonObjectRequest);
    }

    void downloadData(){

        String url = "";

        Uri.Builder urlbuilder = Uri.parse(api).buildUpon();
         url = urlbuilder.build().toString();

        Response.Listener<JSONObject> listner = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{


                    JSONArray newsList = response.getJSONArray("sources");
                    String category,id,name;

                    for (int i =0;i< newsList.length();i++) {
                        category = newsList.getJSONObject(i).getString("category");
                        if(!categoryData.containsKey(category)){
                            categoryData.put(category,new ArrayList<>());
                        }

                        if(!categoryData.get(category).contains(newsList.getJSONObject(i).getString("name")))
                          categoryData.get(category).add(newsList.getJSONObject(i).getString("name"));

                        if(!categoryData.containsKey("ALL")){
                            categoryData.put("ALL",new ArrayList<>());
                        }

                        if(!categoryData.get("ALL").contains(newsList.getJSONObject(i).getString("name")))
                            categoryData.get("ALL").add(newsList.getJSONObject(i).getString("name"));


                        if(!channels.containsKey(newsList.getJSONObject(i).getString("name"))){
                            channels.put(newsList.getJSONObject(i).getString("name"),newsList.getJSONObject(i).getString("id"));
                        }

                    }

                    ArrayList<String> sortedCategory = new ArrayList<>(categoryData.keySet());

                    Collections.sort(sortedCategory);
                    int rgbValue =0;
                    Random r = new Random();
                    for (String s: sortedCategory) {

                        SpannableString colorString = new SpannableString(s);
                        rgbValue = Color.rgb(r.nextInt(255),r.nextInt(255),r.nextInt(255));
                        colorString.setSpan(new ForegroundColorSpan(rgbValue),0,s.length(),0);

                            textColors.put(s,rgbValue);

                        drawerMenu.add(colorString);
                    }


                    channelList.addAll(categoryData.get("ALL"));
                    drawerlistAdapter.notifyDataSetChanged();
                    setTitle("News Gateway " +"("+ Integer.toString(channelList.size())+")");





                }catch(Exception e){

                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API","API CALL FAILED");
            }
        };



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,listner,errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };

        apiRequest.add(jsonObjectRequest);





    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        drawerToggleObject.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggleObject.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        drawerMenu = menu;
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString("Channel", selectedChannel);
        outState.putString("Channel_ID", selectedChannelId);
        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);

        selectedChannel = savedInstanceState.getString("Channel");
        selectedChannelId = savedInstanceState.getString("Channel_ID");

        if(!TextUtils.isEmpty(selectedChannel))
            downloadArticle(selectedChannelId);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggleObject.onOptionsItemSelected(item)){
            return true;
        }

        newsArticleList.clear();
        articleAdapter.notifyDataSetChanged();
        channelList.clear();


        channelList.addAll(categoryData.get(item.getTitle().toString()));
        drawerlistAdapter.notifyDataSetChanged();
        setTitle("News Gateway " +"("+ Integer.toString(channelList.size())+")");

        return super.onOptionsItemSelected(item);



    }
}
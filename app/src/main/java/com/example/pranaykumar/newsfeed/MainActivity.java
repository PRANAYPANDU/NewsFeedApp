package com.example.pranaykumar.newsfeed;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {
        public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String News_url="https://content.guardianapis.com/search?api-key=a63945e7-33d8-4210-99cf-85388bef0f9b";

    private NewsAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private TextView mTextView;
    private View root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView newsListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_list);
        mTextView=(TextView)findViewById(R.id.header);
        root=findViewById(R.id.activity_main);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get details on the currently active default data network
        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();

        //if there is network connection,fetch data
        if(networkInfo!=null&&networkInfo.isConnected())
        {
            LoaderManager loader=getLoaderManager();
            loader.initLoader(1,null,this);
        }
        else{
            View loading_indicator=(ProgressBar)findViewById(R.id.loading_indicator);
            loading_indicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mTextView.setText("");


        }
    }


        @Override
        public Loader<List<News>> onCreateLoader(int id, Bundle args) {
            Log.i(LOG_TAG,"in onCreateLoader");
            return new NewsLoader(this,News_url);
        }

        @Override
        public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
            View loading_indicator=(ProgressBar)findViewById(R.id.loading_indicator);
            loading_indicator.setVisibility(View.GONE);

            mAdapter.clear();
            Log.i(LOG_TAG,"in onLoadFinished");
            if(data!=null&&!data.isEmpty()){
                mAdapter.addAll(data);
                Log.i(LOG_TAG,"data added");
            }
            else {
                mEmptyStateTextView.setText(R.string.no_news);
            }


        }

        @Override
        public void onLoaderReset(Loader<List<News>> loader) {

            mAdapter.clear();
            Log.i(LOG_TAG,"Loader reset");
        }

}


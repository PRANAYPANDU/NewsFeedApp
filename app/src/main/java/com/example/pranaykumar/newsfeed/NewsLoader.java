package com.example.pranaykumar.newsfeed;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by PRANAYKUMAR on 25-03-2017.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG=NewsLoader.class.getSimpleName();
    private String mUrl;
    public NewsLoader(Context context, String news_url) {
        super(context);
        mUrl=news_url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if(mUrl==null) {
            return null;
        }

        List<News> newsFeed=QueryUtils.fetchNews(mUrl);
        return newsFeed;
    }
}

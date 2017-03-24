package com.example.pranaykumar.newsfeed;

/**
 * Created by PRANAYKUMAR on 24-03-2017.
 */

public class News {
    private String Heading;
    private String date;
    private String time;
    private String Url;
    public News(String mHeading,String mdate,String mtime,String mUrl)
    {
        Heading=mHeading;
        date=mdate;
        time=mtime;
        Url=mUrl;
    }

    public String getHeading() {
        return Heading;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return Url;
    }
}

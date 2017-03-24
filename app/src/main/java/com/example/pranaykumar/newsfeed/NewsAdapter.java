package com.example.pranaykumar.newsfeed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pranaykumar.newsfeed.News;
import com.example.pranaykumar.newsfeed.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by PRANAYKUMAR on 24-03-2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, ArrayList<News> newsArrayList) {
        super(context,0,newsArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        News currentNews=getItem(position);
        TextView heading=(TextView) listItemView.findViewById(R.id.news_heading);
        heading.setText(currentNews.getHeading());
        TextView date=(TextView)listItemView.findViewById(R.id.date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2=new SimpleDateFormat("dd MMM,yyyy");
        String DateData=currentNews.getDate();
        int i=DateData.indexOf("T");
        String mdate=DateData.substring(0,i);
        Date Fdate = null;
        try {
            Fdate = formatter.parse(mdate);
            date.setText(formatter2.format(Fdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }



        TextView time=(TextView)listItemView.findViewById(R.id.time);
        String mTime=DateData.substring(i+1,DateData.length()-1);
        SimpleDateFormat formatter3=new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat formatter4=new SimpleDateFormat("hh:mm a");
        Date Ftime=null;
        try {
            Ftime=formatter3.parse(mTime);
            time.setText(formatter4.format(Ftime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listItemView;
    }
}

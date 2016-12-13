package com.dde.shakeonit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Eli on 11/9/2016.
 */

public class DealAdapter extends ArrayAdapter<Deal>
{

    public static class ViewHolder{
        TextView title;
        TextView text;
        TextView dateCreated;
        ImageView completed;
        ImageView waiting;
        ImageView incomplete;
    }

    public DealAdapter(Context context, ArrayList<Deal> deals)
    {
        super(context, 0, deals);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //get data for view position
        Deal deal = getItem(position);

        ViewHolder viewHolder;

        // check if existing view is being reused, otherwise inflate a new view
        if(convertView == null) {

            //if there is no view being reused create on and ViewHolder for it
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);

            //set views to ViewHolder so we dont have to use findViewById every time
            viewHolder.title = (TextView) convertView.findViewById(R.id.listItemDealTitle);
            viewHolder.text = (TextView) convertView.findViewById(R.id.listItemDealText);
            viewHolder.dateCreated = (TextView) convertView.findViewById(R.id.listItemDealDateCreated);
            viewHolder.completed = (ImageView) convertView.findViewById((R.id.deal_completed_icon));
            viewHolder.waiting = (ImageView) convertView.findViewById((R.id.deal_waiting_icon));
            viewHolder.incomplete = (ImageView) convertView.findViewById((R.id.deal_incomplete_icon));
            //use setTag to remember view holder which has references
            convertView.setTag(viewHolder);
        }
        //^^^^^^^^code in getView now acomplishes this more efficiently
        //get references to each view so we can populate them
        //TextView dealTitle = (TextView) convertView.findViewById(R.id.listItemDealTitle);
        //TextView dealText = (TextView) convertView.findViewById(R.id.listItemDealText);
        else{
            //we alread have view so just get references from view holder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //fill in views with our own data
        viewHolder.title.setText(deal.getTitle());
        viewHolder.text.setText(deal.getDescription());
        viewHolder.dateCreated.setText(processDate(deal.getDate()));
        if(deal.getCompleted().equals("complete")){
            viewHolder.completed.setVisibility(View.VISIBLE);
            viewHolder.waiting.setVisibility(View.INVISIBLE);
            viewHolder.incomplete.setVisibility(View.INVISIBLE);
        }
        else if(deal.getAccepted().equals("accepted") && deal.getCompleted().equals("NA")){
            viewHolder.completed.setVisibility(View.INVISIBLE);
            viewHolder.waiting.setVisibility(View.VISIBLE);
            viewHolder.incomplete.setVisibility(View.INVISIBLE);
        }
        else if(deal.getAccepted().equals("rejected") || deal.getCompleted().equals("incomplete")){
            viewHolder.completed.setVisibility(View.INVISIBLE);
            viewHolder.waiting.setVisibility(View.INVISIBLE);
            viewHolder.incomplete.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.completed.setVisibility(View.INVISIBLE);
            viewHolder.waiting.setVisibility(View.INVISIBLE);
            viewHolder.incomplete.setVisibility(View.INVISIBLE);
        }


        // return the modified view
        return convertView;
    }

    public String processDate(long dateMilli){
        Date date = new Date(dateMilli);
        long dateDif = System.currentTimeMillis() - dateMilli;

        String createDate;
        DateFormat sameDay = new SimpleDateFormat("hh:mm");
        DateFormat sameWeek = new SimpleDateFormat("EEE");
        DateFormat sameYear = new SimpleDateFormat("MMM dd");
        DateFormat older = new SimpleDateFormat("MMM dd yyyy");
        if(dateDif <= 86400000)
            createDate = sameDay.format(date);
        else if(86400000 < dateDif && dateDif < 604800016)
            createDate = sameWeek.format(date);
        else if (604800016 < dateDif && dateDif < 31557600000L)
            createDate = sameYear.format(date);
        else
            createDate = older.format(date);

        return createDate;
    }
}

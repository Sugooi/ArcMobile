package www.wielabs.com.arc.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import www.wielabs.com.arc.R;
import www.wielabs.com.arc.others.RecentCallModel;

/**
 * Created by Adil on 06-12-2017.
 */

public class RecentCallAdapter extends ArrayAdapter<RecentCallModel> {
private Context context;

public RecentCallAdapter(Activity context, ArrayList<RecentCallModel> recent) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViewsw, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, recent);
        this.context=context;

        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
        listItemView = LayoutInflater.from(getContext()).inflate(
        R.layout.recentlist_item, parent, false);
        }

// Get the {@link AndroidFlavor} object located at this position in the list
final RecentCallModel currentcall = getItem(position);

        TextView callnameView = (TextView) listItemView.findViewById(R.id.call_name);
        TextView datetext = (TextView) listItemView.findViewById(R.id.date);
        TextView durationtext = (TextView) listItemView.findViewById(R.id.duration);

        callnameView.setText(currentcall.getMobile_number());
        datetext.setText(currentcall.getData_and_time());
        durationtext.setText(currentcall.getDuration());



    return listItemView;
        }
        }
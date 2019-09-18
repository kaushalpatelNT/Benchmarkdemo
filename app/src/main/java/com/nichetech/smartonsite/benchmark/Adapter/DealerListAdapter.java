package com.nichetech.smartonsite.benchmark.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.nichetech.smartonsite.benchmark.Data.Dealer;
import com.nichetech.smartonsite.benchmark.R;

import java.util.ArrayList;

public class DealerListAdapter extends ArrayAdapter<Dealer> {

    public LayoutInflater layoutInflater;
    public Context mContext;
    public ArrayList<Dealer> dealerList;
    private ArrayList<Dealer> copyItemList = new ArrayList<>();
    public int mResource;
    private Filter filter = new CustomFilter();

    public DealerListAdapter(Context context, int resource, ArrayList<Dealer> dealerList) {
        super(context, resource, dealerList);

        mContext = context;
        mResource = resource;
        this.dealerList = dealerList;

    }

    @Nullable
    @Override
    public Dealer getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return copyItemList.size();
    }

    public long getItemId(int position) {
        return Long.parseLong(copyItemList.get(position).DealerId);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_spinner, null);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);

        tvTitle.setText(dealerList.get(position).DealerName);

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            copyItemList.clear();
            // Check if the Original List and Constraint aren't null.
            if (dealerList != null && constraint != null) {
                for (int i = 0; i < dealerList.size(); i++) {
                    // Compare item in original list if it contains constraints.
                    if (dealerList.get(i).DealerName.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        // If TRUE add item in Suggestions.
                        copyItemList.add(dealerList.get(i));
                    }
                }
            }
            // Create new Filter Results and return this to publishResults;
            FilterResults results = new FilterResults();
            results.values = copyItemList;
            results.count = copyItemList.size();
            Log.d("Cause_filter", copyItemList.size() + "");
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}

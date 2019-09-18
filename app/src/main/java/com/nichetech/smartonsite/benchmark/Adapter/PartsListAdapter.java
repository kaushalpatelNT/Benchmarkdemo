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
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponsePart;

import java.util.ArrayList;

public class PartsListAdapter extends ArrayAdapter<ResponsePart.Parts> {

    public LayoutInflater layoutInflater;
    public Context mContext;
    public ArrayList<ResponsePart.Parts> usePartsList;
    private ArrayList<ResponsePart.Parts> copyItemList = new ArrayList<>();
    public int mResource;
    private Filter filter = new CustomFilter();

    public PartsListAdapter(Context context, int resource, ArrayList<ResponsePart.Parts> usePartsList) {
        super(context, resource, usePartsList);

        mContext = context;
        mResource = resource;
        this.usePartsList = usePartsList;

    }

    @Nullable
    @Override
    public ResponsePart.Parts getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return copyItemList.size();
    }

    public long getItemId(int position) {
        return Long.parseLong(copyItemList.get(position).PartId);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_spinner, null);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);

        tvTitle.setText(usePartsList.get(position).PartName);

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
            if (usePartsList != null && constraint != null) {
                for (int i = 0; i < usePartsList.size(); i++) {
                    // Compare item in original list if it contains constraints.
                    if (usePartsList.get(i).PartName.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        // If TRUE add item in Suggestions.
                        copyItemList.add(usePartsList.get(i));
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

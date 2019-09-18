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
import com.nichetech.smartonsite.benchmark.Data.Product;
import com.nichetech.smartonsite.benchmark.R;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<Product> {

    public LayoutInflater layoutInflater;
    public Context mContext;
    public ArrayList<Product> productList;
    private ArrayList<Product> copyItemList = new ArrayList<>();
    public int mResource;
    private Filter filter = new CustomFilter();

    public ProductListAdapter(Context context, int resource, ArrayList<Product> productList) {
        super(context, resource, productList);

        mContext = context;
        mResource = resource;
        this.productList = productList;

    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return copyItemList.size();
    }

    public long getItemId(int position) {
        return Long.parseLong(copyItemList.get(position).ProductId);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_spinner, null);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);

        tvTitle.setText(productList.get(position).Productname);

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
            if (productList != null && constraint != null) {
                for (int i = 0; i < productList.size(); i++) {
                    // Compare item in original list if it contains constraints.
                    if (productList.get(i).Productname.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        // If TRUE add item in Suggestions.
                        copyItemList.add(productList.get(i));
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

package com.nichetech.smartonsite.benchmark.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nichetech.smartonsite.benchmark.Activities.TripDetailActivity;
import com.nichetech.smartonsite.benchmark.Common.DateTimeUtils;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseTripList;

import java.util.ArrayList;

/**
 * Created by android-2 on 4/18/17.
 */

public class TripListAdapter extends RecyclerView.Adapter {
    private ArrayList<ResponseTripList.TripListData> listData;
    private Context mContext;
    Typeface mTypefaceMyriad, mTypefaceBold, mTypefacesemiBold, mTypefacemedium;
    private boolean loading;
    onLoadMoreListener onLoadListener;
    private int last, first, total;
    private int threshold = 5;
    private static int DATAVIEW = 1;
    private static int PROGRES = 2;
    View view;

    public boolean isLoading() {
        return loading;
    }

    public TripListAdapter(Context context, ArrayList<ResponseTripList.TripListData> listData, RecyclerView recyclerView) {
        mContext = context;
        this.listData = listData;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);


                    total = linearLayoutManager.getItemCount();
                    first = linearLayoutManager.findFirstVisibleItemPosition();
                    last = linearLayoutManager.findLastVisibleItemPosition();


                    Log.e("LAST==>", " " + last);


                    if (!loading && total <= (last + threshold)) {

                        loading = true;
                        if (onLoadListener != null) {
                            onLoadListener.onLoadMore();
                        }

                    }


                }
            });
        }


        //------------- TYPEFACE ------------//
        mTypefaceBold = TypefaceUtils.RalewayBold(context);
        mTypefacesemiBold = TypefaceUtils.RalewaySemiBold(context);
        mTypefacemedium = TypefaceUtils.RalewayMedium(context);
        mTypefaceMyriad = TypefaceUtils.MyriadProRegular(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder hold;
        if (viewType == PROGRES) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_horizontal, parent, false);
            hold = new ProgressBarFav(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_trip_list, parent, false);
            hold = new CustomViewHolder(view);
        }
        return hold;
       /* View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_trip_list, null);
        return new CustomViewHolder(view);*/
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, final int position) {

        if (holder1 instanceof CustomViewHolder) {

            final CustomViewHolder holder = (CustomViewHolder) holder1;


            holder.tvCustomerName.setTypeface(mTypefacesemiBold);
            holder.tvComplaintId.setTypeface(mTypefacemedium);
            holder.tvComplaintDate.setTypeface(mTypefacesemiBold);


            String date = DateTimeUtils.changeDateTimeFormat(listData.get(position).getTrip_createddate(),
                    DateTimeUtils.SERVER_FORMAT_DATE_TIME_T);


            holder.tvCustomerName.setText(listData.get(position).getCustomer_name());
            holder.tvComplaintId.setText(listData.get(position).getCustomer_contact());
            holder.tvComplaintDate.setText(date);


            // TODO:PRADIP  4/20/17
            // add following points in putextra
            // distance from ws
            // start time in ws
            // points from location


            holder.llTripList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent inPark = new Intent(mContext, TripDetailActivity.class);
                    inPark.putExtra("user_loc_id", listData.get(position).getUser_loc_id());
                    inPark.putExtra("user_id", listData.get(position).getUser_id());
                    inPark.putExtra("organization_id", listData.get(position).getOrganization_id());
                    inPark.putExtra("customer_name", listData.get(position).getCustomer_name());
                    inPark.putExtra("customer_contact", listData.get(position).getCustomer_contact());
                    inPark.putExtra("trip_endtime", listData.get(position).getTrip_endtime());
                    inPark.putExtra("trip_starttime", listData.get(position).getTrip_starttime());
                    inPark.putExtra("trip_fromlocation", listData.get(position).getTrip_fromaddress());
                    inPark.putExtra("trip_tolocation", listData.get(position).getTrip_tolocation());
                    inPark.putExtra("trip_createddate", listData.get(position).getTrip_createddate());
                    inPark.putExtra("trip_fromaddress", listData.get(position).getTrip_fromaddress());
                    inPark.putExtra("trip_toaddress", listData.get(position).getTrip_toaddress());
                    inPark.putExtra("distance", listData.get(position).getTrip_distance());
                    inPark.putExtra("discription", listData.get(position).getTrip_description());
                    inPark.putExtra("positoin", position);
                    //  inPark.putExtra("location",listData.get(position).getLocations());
                    inPark.putExtra("location", listData.get(position).getLocations());
                   inPark.putExtra("images", listData.get(position).getTrip_images());


                    mContext.startActivity(inPark);
                }
            });


        } else {
            ((ProgressBarFav) holder1).progressBar.setIndeterminate(true);
        }


    }

    public class ProgressBarFav extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public ProgressBarFav(View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressBar1);
            progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tvCustomerName, tvComplaintId, tvComplaintDate;
        LinearLayout llTripList;

        CustomViewHolder(View view) {
            super(view);

            tvCustomerName = view.findViewById(R.id.tv_trip_list_name);
            tvComplaintId = view.findViewById(R.id.tv_trip_list_no);
            tvComplaintDate = view.findViewById(R.id.tv_trip_list_date);
            llTripList = view.findViewById(R.id.ll_trip_list);
        }

    }

    @Override
    public int getItemViewType(int position) {

        int view_type;

        if (listData.get(position) != null) {
            view_type = DATAVIEW;
        } else {
            view_type = PROGRES;
        }

        return view_type;
    }

    public interface onLoadMoreListener {
        void onLoadMore();

    }

    public void setOnLoadListener(onLoadMoreListener onLoadListener) {

        this.onLoadListener = onLoadListener;

    }

    public void setLoaded() {
        loading = false;
    }
}


package com.nichetech.smartonsite.benchmark.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.Common.Utility;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseTripList;

import java.util.ArrayList;

/**
 * Created by android-2 on 4/21/17.
 */

public class RoutePointAdapter extends RecyclerView.Adapter<RoutePointAdapter.CustomViewHolder> {
    ArrayList<ResponseTripList.TripListData.Location> locations = new ArrayList<>();
    private Context mContext;
    Typeface mTypefaceMyriad, mTypefaceBold, mTypefacesemiBold, mTypefacemedium;

    public RoutePointAdapter(Context context, ArrayList<ResponseTripList.TripListData.Location> locations) {
        mContext = context;
        this.locations = locations;

        //------------- TYPEFACE ------------//
        mTypefaceBold = TypefaceUtils.RalewayBold(context);
        mTypefacesemiBold = TypefaceUtils.RalewaySemiBold(context);
        mTypefacemedium = TypefaceUtils.RalewayMedium(context);
        mTypefaceMyriad = TypefaceUtils.MyriadProRegular(context);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_route_point, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder,  int position) {

        final ResponseTripList.TripListData.Location location = locations.get(position);
        holder.tvRoutePoint.setTypeface(mTypefacemedium);
        holder.tvRoutePoint.setText(String.format("%s", location.getTrip_address()));

        holder.tvRoutePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.openMap(view.getContext(), location.getTrip_address(),
                        location.getTrip_latitude(), location.getTrip_longitude());
            }
        });


    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tvRoutePoint;

        public CustomViewHolder(View view) {
            super(view);

            tvRoutePoint = view.findViewById(R.id.tv_route_point);
        }

    }
}

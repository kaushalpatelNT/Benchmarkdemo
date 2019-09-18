package com.nichetech.smartonsite.benchmark.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.nichetech.smartonsite.benchmark.Activities.AssignedComplaintDetailsActivity;
import com.nichetech.smartonsite.benchmark.Common.Constant;
import com.nichetech.smartonsite.benchmark.Common.DateTimeUtils;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseAssignedList;

import java.util.ArrayList;

/**
 * Created by nichetech on 1/12/16.
 */

public class AssignedAdapter extends RecyclerView.Adapter<AssignedAdapter.CustomViewHolder> {
    private ArrayList<ResponseAssignedList.ComplaintData> listData;
    private Context mContext;
    Typeface mTypefaceMyriad, mTypefaceBold, mTypefacesemiBold, mTypefacemedium;

    public AssignedAdapter(Context context, ArrayList<ResponseAssignedList.ComplaintData> listData) {
        mContext = context;
        this.listData = listData;

        //------------- TYPEFACE ------------//
        mTypefaceBold = TypefaceUtils.RalewayBold(context);
        mTypefacesemiBold = TypefaceUtils.RalewaySemiBold(context);
        mTypefacemedium = TypefaceUtils.RalewayMedium(context);
        mTypefaceMyriad = TypefaceUtils.MyriadProRegular(context);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_assigned_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {


        holder.tvCustomerName.setTypeface(mTypefaceBold);
        holder.tvComplaintId.setTypeface(mTypefacesemiBold);
        holder.tvComplaintDate.setTypeface(mTypefacesemiBold);

        String date = DateTimeUtils.changeDateTimeFormat(listData.get(position).getComplaintDate(),
                DateTimeUtils.SERVER_FORMAT_DATE_TIME);

        holder.tvCustomerName.setText(listData.get(position).getCustomerName());
        holder.tvComplaintId.setText(listData.get(position).getComplainId());
        holder.tvComplaintDate.setText(date);

        holder.llAssigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inPark = new Intent(mContext, AssignedComplaintDetailsActivity.class);
                inPark.putExtra(Constant.ACTIVITY_NAME, Constant.ActivityTitle.ASSIGNED_LIST);
                inPark.putExtra(Constant.Complaint.ComplainId, listData.get(position).getComplainId());
                inPark.putExtra(Constant.Complaint.CustomerName, listData.get(position).getCustomerName());
                inPark.putExtra(Constant.Complaint.CustomerAddress, listData.get(position).getCustomerAddress());
                inPark.putExtra(Constant.Complaint.CustomerPhone, listData.get(position).getCustomerPhone());
                inPark.putExtra(Constant.Complaint.ProductName, listData.get(position).getProductName());
                inPark.putExtra(Constant.Complaint.ComplaintType, listData.get(position).getComplaintType());
                inPark.putExtra(Constant.Complaint.Priority, listData.get(position).getPriority());
                inPark.putExtra(Constant.Complaint.CustomerRemark, listData.get(position).getCustomerRemark());
                inPark.putExtra(Constant.Complaint.CustomerAlternatePhone, listData.get(position).getCustomerAlternatePhone());
                inPark.putExtra(Constant.Complaint.ComplaintDate, listData.get(position).getComplaintDate());
                mContext.startActivity(inPark);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tvCustomerName, tvComplaintId, tvComplaintDate;
        LinearLayout llAssigned;

        public CustomViewHolder(View view) {
            super(view);

            tvCustomerName = view.findViewById(R.id.custom_customer_name);
            tvComplaintId = view.findViewById(R.id.custom_complaint_id);
            tvComplaintDate = view.findViewById(R.id.custom_complaint_date);
            llAssigned = view.findViewById(R.id.ll_assigned);
        }

    }
}

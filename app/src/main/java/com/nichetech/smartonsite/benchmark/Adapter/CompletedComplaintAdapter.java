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
import com.nichetech.smartonsite.benchmark.Activities.ComplaintDetailsActivity;
import com.nichetech.smartonsite.benchmark.Common.Constant;
import com.nichetech.smartonsite.benchmark.Common.DateTimeUtils;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseAssignedList;

import java.util.List;

/**
 * Created by nichetech on 6/12/16.
 */

public class CompletedComplaintAdapter extends RecyclerView.Adapter<CompletedComplaintAdapter.CustomViewHolder> {
    private List<ResponseAssignedList.ComplaintData> listData;
    private Context context;
    Typeface mTypefaceMyriad, mTypefaceBold, mTypefacesemiBold, mTypefacemedium;

    public CompletedComplaintAdapter(Context context, List<ResponseAssignedList.ComplaintData> listData) {
        this.context = context;
        this.listData = listData;

        //------------- TYPEFACE ------------//
        mTypefaceBold = TypefaceUtils.RalewayBold(context);
        mTypefacesemiBold = TypefaceUtils.RalewaySemiBold(context);
        mTypefacemedium = TypefaceUtils.RalewayMedium(context);
        mTypefaceMyriad = TypefaceUtils.MyriadProRegular(context);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_completed_complaint, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        holder.tvCustomerName.setTypeface(mTypefaceBold);
        holder.tvCompanyName.setTypeface(mTypefaceBold);
        holder.tvComplaintId.setTypeface(mTypefacesemiBold);
        holder.tvComplaintDate.setTypeface(mTypefacesemiBold);

        String date = DateTimeUtils.changeDateTimeFormat(listData.get(position).getComplaintDate(),
                DateTimeUtils.SERVER_FORMAT_DATE_TIME);

        holder.tvCustomerName.setText(listData.get(position).getCustomerName());
        holder.tvCompanyName.setText(listData.get(position).getPriority());
        holder.tvComplaintId.setText(listData.get(position).getComplainId());
        holder.tvComplaintDate.setText(date);

        holder.llCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ComplaintDetailsActivity.class);
                intent.putExtra(Constant.ACTIVITY_NAME, Constant.ActivityTitle.COMPLETED);
                intent.putExtra(Constant.Complaint.ComplainId, listData.get(position).getComplainId());
                intent.putExtra(Constant.Complaint.CustomerName, listData.get(position).getCustomerName());
                intent.putExtra(Constant.Complaint.CustomerAddress, listData.get(position).getCustomerAddress());
                intent.putExtra(Constant.Complaint.CustomerPhone, listData.get(position).getCustomerPhone());
                intent.putExtra(Constant.Complaint.ProductName, listData.get(position).getProductName());
                intent.putExtra(Constant.Complaint.ComplaintType, listData.get(position).getComplaintType());
                intent.putExtra(Constant.Complaint.Priority, listData.get(position).getPriority());
                intent.putExtra(Constant.Complaint.CustomerRemark, listData.get(position).getCustomerRemark());
                intent.putExtra(Constant.Complaint.CompanyRemark, listData.get(position).getCompanyRemark());
                intent.putExtra(Constant.Complaint.CustomerAlternatePhone, listData.get(position).getCustomerAlternatePhone());
                intent.putExtra(Constant.Complaint.ComplaintDate, listData.get(position).getComplaintDate());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listData.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tvCustomerName, tvCompanyName, tvComplaintId, tvComplaintDate;
        LinearLayout llCompleted;

        CustomViewHolder(View view) {
            super(view);


            Typeface typeface = TypefaceUtils.RalewayBold(context);
            tvCustomerName = view.findViewById(R.id.tv_completed_name);
            tvCompanyName = view.findViewById(R.id.tv_completed_company_name);
            tvComplaintId = view.findViewById(R.id.tv_completed_id);
            tvComplaintDate = view.findViewById(R.id.tv_completed_date);
            llCompleted = view.findViewById(R.id.ll_completed);

            tvCustomerName.setTypeface(typeface);
        }

    }
}

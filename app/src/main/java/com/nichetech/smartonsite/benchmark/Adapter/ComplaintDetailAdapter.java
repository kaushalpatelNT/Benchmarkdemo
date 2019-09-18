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
import com.nichetech.smartonsite.benchmark.Activities.CustomFormDetailActivity;
import com.nichetech.smartonsite.benchmark.Common.DateTimeUtils;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseCustomFormDetail;

/**
 * Created by android-2 on 3/24/17.
 */

public class ComplaintDetailAdapter extends RecyclerView.Adapter<ComplaintDetailAdapter.CustomViewHolder> {

    private ResponseCustomFormDetail.Data listData;
    private Context mContext;
    Typeface mTypefaceMyriad, mTypefaceBold, mTypefacesemiBold, mTypefacemedium;

    public ComplaintDetailAdapter(Context context, ResponseCustomFormDetail.Data listData) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_complaint_detail, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        holder.tvAction.setText("Action - " + (position + 1));

        final String date = DateTimeUtils.changeDateTimeFormat(listData.getAction().get(position).getComplaint_action_date(),
                DateTimeUtils.SERVER_FORMAT_DATE_TIME);

        holder.tvDate.setText(date);
        holder.llDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   if (listData.getOrg_form_id().equals("1")) {
                    Intent intent = new Intent(mContext, ActionDetailActivity.class);
                    intent.putExtra("company_id", listData.getComp_org_id());
                    intent.putExtra("company_code", listData.getComp_code());
                    intent.putExtra("complaint_id", listData.getComp_id());
                    intent.putExtra("org_form_id", listData.getOrg_form_id());
                    intent.putExtra("date", date);
                    intent.putExtra("position", position);
                    intent.putExtra("action_taken", listData.getComp_description());
                    intent.putExtra("status", listData.getComp_status());
                    intent.putExtra("activity", "Completed");
                    mContext.startActivity(intent);
                } else {*/
                Intent intent = new Intent(mContext, CustomFormDetailActivity.class);
                intent.putExtra("company_id", listData.getComp_org_id());
                intent.putExtra("company_code", listData.getComp_code());
                intent.putExtra("complaint_id", listData.getComp_id());
                intent.putExtra("org_form_id", listData.getOrg_form_id());
                intent.putExtra("date", date);
                intent.putExtra("position", position);
                intent.putExtra("action_taken", listData.getComp_description());
                intent.putExtra("status", listData.getComp_status());
                intent.putExtra("activity", "Completed");
                mContext.startActivity(intent);
                // }
            }
        });


    }

    @Override
    public int getItemCount() {
        return listData.action.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tvAction, tvDate;
        LinearLayout llDetail;

        public CustomViewHolder(View view) {
            super(view);

            tvAction = view.findViewById(R.id.tv_action_name);
            tvDate = view.findViewById(R.id.tv_date);
            tvAction.setTypeface(TypefaceUtils.RalewaySemiBold(mContext));
            tvDate.setTypeface(TypefaceUtils.RalewaySemiBold(mContext));
            llDetail = view.findViewById(R.id.ll_detail);

        }

    }
}

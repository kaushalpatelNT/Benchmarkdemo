package com.nichetech.smartonsite.benchmark.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.nichetech.smartonsite.benchmark.R;

import java.util.ArrayList;

/**
 * Created by android-2 on 3/25/17.
 */

public class ActionDetailImageAdapter extends BaseAdapter {

    private AlertDialog Add_popup;
    private ArrayList<String> images = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater = null;

    public ActionDetailImageAdapter(Context context, ArrayList<String> images) {

        this.images = images;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.image_adepter_row, parent, false);
        holder.img = rowView.findViewById(R.id.image_row);

        Glide.with(context)
                .load(images.get(position))
               .apply(RequestOptions.centerCropTransform())
                //.override(100, 100)
                .into(holder.img);

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup_dialog(images.get(position));
            }
        });

        return rowView;
    }

    public void popup_dialog(final String bitmap) {
        LayoutInflater factory = LayoutInflater.from(context);
        final View dialogview = factory.inflate(R.layout.custom_complaints_detail_image_popup, null);
        dialogview.setPadding(0, 20, 0, 20);

        Add_popup = new AlertDialog.Builder(context).create();
        Add_popup.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Add_popup.setView(dialogview);
        Add_popup.setCancelable(false);
        Add_popup.setCanceledOnTouchOutside(false);

        final ImageView iv_popup = dialogview.findViewById(R.id.iv_popup);
        final ImageView iv_popup_close = dialogview.findViewById(R.id.iv_popup_close);
        final ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setMessage("Please wait while loading image...");
        progressBar.show();
        final RelativeLayout relPopup = dialogview.findViewById(R.id.rel_popup);

     /*   Glide.with(context)
                .load(bitmap)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.dismiss();
                        Add_popup.show();
                        relPopup.setVisibility(View.VISIBLE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.dismiss();
                        Add_popup.show();
                        relPopup.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .centerCrop()
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(iv_popup);
*/

        iv_popup_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_popup.dismiss();
            }
        });
    }
}


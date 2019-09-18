package com.nichetech.smartonsite.benchmark.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import com.nichetech.smartonsite.benchmark.R;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    public AlertDialog Add_popup;
    ArrayList<Bitmap> images;
    Context context;
    private static LayoutInflater inflater = null;

    public ImageAdapter(Context context, ArrayList<Bitmap> images) {

        this.images = images;
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
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
        View rowView;


        rowView = inflater.inflate(R.layout.image_adepter_row, null);
        holder.img = rowView.findViewById(R.id.image_row);
        holder.img.setImageBitmap(images.get(position));


        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup_dialog(images.get(position));
            }
        });

        return rowView;
    }

    public void popup_dialog(final Bitmap bitmap) {
        LayoutInflater factory = LayoutInflater.from(context);
        final View dialogview = factory.inflate(R.layout.custom_image_popup, null);

        final ImageView iv_popup = dialogview.findViewById(R.id.iv_popup);
        final Button iv_cancel = dialogview.findViewById(R.id.iv_cancel);
        final Button iv_remove = dialogview.findViewById(R.id.iv_remove);

        iv_popup.setImageBitmap(bitmap);
        Add_popup = new AlertDialog.Builder(context).create();
        Add_popup.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Add_popup.setView(dialogview);


        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_popup.setImageBitmap(null);
                Add_popup.dismiss();
            }
        });

        iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iv_popup.setImageBitmap(null);
                images.remove(images.indexOf(bitmap));
                notifyDataSetChanged();
                Add_popup.dismiss();
            }
        });
        Add_popup.show();


    }

}

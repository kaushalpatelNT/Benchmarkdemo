package com.nichetech.smartonsite.benchmark.Common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.nichetech.smartonsite.benchmark.R;


/***
 * IPv4 address:	192.168.0.214
 * IPv4 DNS Servers:	192.168.0.1
 * 8.8.8.8
 * Manufacturer:	Realtek
 * Description:	Realtek PCIe FE Family Controller
 * Driver version:	8.1.510.2013
 * Physical address:	‎A0-8C-FD-1A-27-74
 */

/**
 * ContactSlectable(in.nichetech.widget) <br />
 * Developed by <b><a href="http://RohitSuthar.com/">Suthar Rohit</a></b>  <br />
 * on 4/7/16.
 *
 * @author Suthar Rohit
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    // private final String TAG = "DividerItemDecoration";
    private Drawable mDivider;
    private int leftMargin = 0;


    public DividerItemDecoration(Context context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider_horizontal);
        this.leftMargin = 0;
    }

    public DividerItemDecoration(Context context, int color, int leftMargin) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider_horizontal);
        mDivider.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        this.leftMargin = Utility.intToDP(context, leftMargin);
    }

    public DividerItemDecoration(Context context, int leftMargin) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider_horizontal);
        this.leftMargin = Utility.intToDP(context, leftMargin);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) == 0) {
            return;
        }
        outRect.top = mDivider.getIntrinsicHeight();
    }


    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int dividerLeft = parent.getPaddingLeft() + leftMargin;
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            mDivider.draw(canvas);
        }
    }
}

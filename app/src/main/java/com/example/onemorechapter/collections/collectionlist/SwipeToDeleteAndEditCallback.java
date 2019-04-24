package com.example.onemorechapter.collections.collectionlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.example.onemorechapter.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

abstract public class SwipeToDeleteAndEditCallback extends ItemTouchHelper.Callback {

    private Bitmap deleteIcon;
    private Bitmap editIcon;


    SwipeToDeleteAndEditCallback(Context context) {
        deleteIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.delete);
        editIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.feather);
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            float itemHeight = (float) itemView.getBottom() - (float) itemView.getTop();
            float itemWidth = itemHeight / 3;

            Paint p = new Paint();
            p.setColor(itemView.getResources().getColor(R.color.colorMaroon));

            if(dX > 0) {
                RectF icon_dest = new RectF(
                        (float) itemView.getLeft() + itemWidth,
                        (float) itemView.getTop() + itemWidth,
                        (float) itemView.getLeft() + 2 * itemWidth,
                        (float) itemView.getBottom() - itemWidth
                );
                c.drawBitmap(editIcon, null, icon_dest, p);
            }else{
                RectF icon_dest = new RectF(
                        (float) itemView.getRight() - 2*itemWidth ,
                        (float) itemView.getTop() + itemWidth,
                        (float) itemView.getRight() - itemWidth,
                        (float)itemView.getBottom() - itemWidth
                );
                c.drawBitmap(deleteIcon,null,icon_dest,p);
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.6f;
    }
}


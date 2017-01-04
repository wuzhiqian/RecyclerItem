package com.wzq.recyclerdealer;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by wzq on 17-1-2.
 */

public class ItemTouchCallBack extends ItemTouchHelper.Callback {

    private ItemChangeImp itemChangeImp;

    public ItemTouchCallBack(ItemChangeImp itemChangeImp) {
        this.itemChangeImp = itemChangeImp;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int upDownFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int leftRightFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(upDownFlag, leftRightFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        itemChangeImp.changePos(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        itemChangeImp.deletePos(viewHolder.getAdapterPosition());
    }


}

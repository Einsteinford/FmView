package com.einstinford.fmview.deprecated;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by lt on 2016/5/25.
 */
public abstract class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(View convertView) {
        super(convertView);
        ButterKnife.bind(this, convertView);
    }

    /**
     * 点击事件回调
     */
    public interface OnItemClickListener {
        /**
         * on item click call back
         *
         * @param convertView convertView
         * @param position    position
         */
        void onItemClick(View convertView, int position);
    }

    /**
     * 长点击事件回调
     */
    public interface OnItemLongClickListener {
        /**
         * on item long click call back
         *
         * @param convertView convertView
         * @param position    position
         */
        boolean onItemLongClick(View convertView, int position);
    }
}

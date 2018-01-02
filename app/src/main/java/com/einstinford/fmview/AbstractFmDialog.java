//package com.einstinford.fmview;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.hzpz.ninebirdsfm.android.base.BaseRecyclerViewAdapter;
//import com.hzpz.ninebirdsfm.android.base.BaseRecyclerViewHolder;
//
//import java.util.List;
//
//import butterknife.BindView;
//
//
///**
// * 改变电台区域和频道
// *
// * @author kk
// *
// */
//public abstract class AbstractFmDialog extends Dialog implements BaseRecyclerViewHolder.OnItemClickListener {
//
//    private Context mContext;
//    private RecyclerView rvOption;
//    private List<String> mList;
//    private FmDialogAdapter mAdapter;
//
//    /**
//     * @param cxt
//     */
//    public AbstractFmDialog(Context cxt, @NonNull List<String> list) {
//        super(cxt, R.style.MyDialog);
//        this.mContext = cxt;
//        this.mList = list;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(LayoutInflater.from(mContext).inflate(R.layout.dialog_fm, null));
//        mAdapter = new FmDialogAdapter();
//        mAdapter.setOnItemClickListener(this);
//        initView();
//    }
//
//    /**
//     * 初始化控件
//     */
//    private void initView() {
//        rvOption = (RecyclerView) findViewById(R.id.rvOption);
//        rvOption.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvOption.setAdapter(mAdapter);
//    }
//
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            this.dismiss();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    class FmDialogAdapter extends BaseRecyclerViewAdapter {
//
//        @Override
//        protected BaseRecyclerViewHolder onCreatedRecycleViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
//            return new FmHolder(inflater.inflate(R.layout.item_fm_dialog, null));
//        }
//
//        @Override
//        protected void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
//            ((FmHolder) viewHolder).mString.setText(mList.get(position));
//        }
//
//        @Override
//        public int getItemCount() {
//            return mList == null ? 0 : mList.size();
//        }
//    }
//
//    class FmHolder extends BaseRecyclerViewHolder {
//        @BindView(R.id.string)
//        TextView mString;
//
//        public FmHolder(View convertView) {
//            super(convertView);
//        }
//    }
//}

package com.yutao.yilan_master_mine.Easyrecycle;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yutao.yilan_master_mine.HtmlData.MeiNv;

/**
 * Created by Administrator on 2017/1/7.
 */

public class ImageAdapter extends RecyclerArrayAdapter<MeiNv> {

    public ImageAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(parent);
    }
}

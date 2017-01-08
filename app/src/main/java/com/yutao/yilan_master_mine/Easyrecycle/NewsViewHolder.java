package com.yutao.yilan_master_mine.Easyrecycle;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yutao.yilan_master_mine.HtmlData.News;
import com.yutao.yilan_master_mine.R;

/**
 * Created by Administrator on 2017/1/7.
 */

public class NewsViewHolder extends BaseViewHolder<News> {

    public ImageView personFace;
    public TextView personName;
    public TextView personSign;

    public NewsViewHolder(ViewGroup parent) {
        super(parent, R.layout.news_recycler_item);
        personName = $(R.id.person_name);
        personSign = $(R.id.person_sign);
        personFace = $(R.id.person_face);
    }

    @Override
    public void setData(News data) {
        personName.setText(data.getTitle());
        personSign.setText(data.getCtime());
        Glide.with(getContext())
                .load(data.getPicUrl())
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(personFace);
    }
}

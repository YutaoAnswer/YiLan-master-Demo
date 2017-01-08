package com.yutao.yilan_master_mine.Easyrecycle;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yutao.yilan_master_mine.HtmlData.MeiNv;

/**
 * Created by Administrator on 2017/1/7.
 */

public class ImageViewHolder extends BaseViewHolder<MeiNv> {

    ImageView imageView;

    public ImageViewHolder(ViewGroup parent) {
        super(new ImageView(parent.getContext()));
        imageView = ((ImageView) itemView);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void setData(MeiNv data) {
        final DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        Glide.with(getContext())
                .load(data.getPicUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        int width = displayMetrics.widthPixels / 2 - 10;//宽度为屏幕宽度的一半
                        int height = resource.getHeight() * width / resource.getWidth();//计算View的高度
                        //获取bitmap信息，可赋值给外部变量操作，也可在此时操作
                        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                        layoutParams.height = height;
                        layoutParams.width = width;
                        imageView.setLayoutParams(layoutParams);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setImageBitmap(resource);
                    }
                });
    }
}

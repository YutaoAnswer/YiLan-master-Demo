package com.yutao.yilan_master_mine.Activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.yutao.yilan_master_mine.R;
import com.yutao.yilan_master_mine.Util.PictureUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/7.
 */

public class PictureDescribeActivity extends BaseActivity {

    @BindView(R.id.image)
    ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picturedescribe);
        ButterKnife.bind(this);
        //新页面接受数据
        Bundle bundle = this.getIntent().getExtras();
        //接受Name值
        final ArrayList<String> data = bundle.getStringArrayList("data");
        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels / 2 ;//宽度为屏幕的一半
        Glide.with(this)
                .load(data.get(0))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        Log.d("bitmap","高"+bitmap.getHeight()+"宽"+bitmap.getWidth());
                        int width = displayMetrics.widthPixels;//宽度为屏幕宽度一半
                        int height = bitmap.getHeight() * width / bitmap.getWidth();//计算View的高度
                        Log.d("picture", "高"+height + "宽" +width); //获取bitmap信息，可赋值给外部变量操作，也可在此时行操作。
                        ViewGroup.LayoutParams params = image.getLayoutParams();
                        params.height = height;
                        params.width = width;
                        image.setLayoutParams(params);
                        image.setScaleType(ImageView.ScaleType.FIT_XY);
                        image.setImageBitmap(bitmap);
                    }
                });
        image.setDrawingCacheEnabled(true);
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(PictureDescribeActivity.this)
                        .setMessage("保存图片")
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                PictureUtil.saveImage(image, PictureDescribeActivity.this);
                            }
                        }).show();
                return true;
            }
        });
    }
}

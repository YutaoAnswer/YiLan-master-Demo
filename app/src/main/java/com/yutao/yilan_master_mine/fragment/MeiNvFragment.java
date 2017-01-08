package com.yutao.yilan_master_mine.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.yutao.yilan_master_mine.Activity.PictureDescribeActivity;
import com.yutao.yilan_master_mine.Easyrecycle.ImageAdapter;
import com.yutao.yilan_master_mine.HtmlData.ApiService;
import com.yutao.yilan_master_mine.HtmlData.MeiNv;
import com.yutao.yilan_master_mine.HtmlData.MeiNvGson;
import com.yutao.yilan_master_mine.R;
import com.yutao.yilan_master_mine.Util.PixUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/1/7.
 */
public class MeiNvFragment extends Fragment {

    private ImageAdapter adapter;
    private int page = 0;

    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meizi_fragment, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter = new ImageAdapter(getActivity()));
        //添加边框
        SpaceDecoration itemDecoration = new SpaceDecoration(((int) PixUtil.convertDpToPixel(10, getContext())));
        itemDecoration.setPaddingEdgeSide(true);
        itemDecoration.setPaddingStart(true);
        itemDecoration.setPaddingHeaderFooter(false);
        recyclerView.addItemDecoration(itemDecoration);
        //更多加载
        adapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                addData();
            }

            @Override
            public void onMoreClick() {

            }
        });
        //刷新事件
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //TODO
                    }
                }, 1000);
            }
        });
        //点击事件
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ArrayList<String> data = new ArrayList<>();
                data.add(adapter.getAllData().get(position).getPicUrl());
                data.add(adapter.getAllData().get(position).getUrl());
                new Intent(getActivity(), PictureDescribeActivity.class);
            }
        });
        addData();
        return view;
    }

    private void addData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.tianapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);//采用Java的动态代理模式
        apiService.getPictureData("cb1df0b3977025de0ac5e241dba5cbc4", "10", page)
                .subscribeOn(Schedulers.io())
                .map(new Func1<MeiNvGson, List<MeiNv>>() {
                    @Override
                    public List<MeiNv> call(MeiNvGson meiNvGson) {
                        List<MeiNv> meiNvList = new ArrayList<>();
                        for (MeiNvGson.NewslistBean newslistBean : meiNvGson.getNewslist()) {
                            MeiNv m1 = new MeiNv();
                            m1.setTitle(newslistBean.getTitle());
                            m1.setCtime(newslistBean.getCtime());
                            m1.setDescription(newslistBean.getDescription());
                            m1.setPicUrl(newslistBean.getPicUrl());
                            m1.setUrl(newslistBean.getUrl());
                            meiNvList.add(m1);
                        }
                        return meiNvList;// 返回类型
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<MeiNv>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(),
                                "网络连接失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(List<MeiNv> meiNvList) {
                        adapter.addAll(meiNvList);
                    }
                });
        page = page + 1;
    }

}

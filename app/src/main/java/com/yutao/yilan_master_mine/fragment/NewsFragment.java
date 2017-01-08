package com.yutao.yilan_master_mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.yutao.yilan_master_mine.Easyrecycle.NewsAdapter;
import com.yutao.yilan_master_mine.Easyrecycle.NewsDetailsActivity;
import com.yutao.yilan_master_mine.HtmlData.ApiService;
import com.yutao.yilan_master_mine.HtmlData.News;
import com.yutao.yilan_master_mine.HtmlData.NewsGson;
import com.yutao.yilan_master_mine.R;
import com.yutao.yilan_master_mine.Util.PixUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/1/7.
 */
public class NewsFragment extends Fragment {

    public static final String TAG = "NewsFragment";
    private NewsAdapter adapter;
    private int page = 0;

    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setAdapter(adapter = new NewsAdapter(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //添加边框
        SpaceDecoration itemDecoration = new SpaceDecoration(((int) PixUtil.convertDpToPixel(8, getActivity())));
        itemDecoration.setPaddingEdgeSide(true);
        itemDecoration.setPaddingStart(true);
        itemDecoration.setPaddingHeaderFooter(false);
        recyclerView.addItemDecoration(itemDecoration);
        //更多加载
        adapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                getData();
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
                        adapter.clear();
                        page = 0;
                        getData();
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
                Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
                //用Bundle携带数据
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("data", data);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    private void getData() {
        Log.d(TAG, page + "");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.tianapi.com/")
                .addConverterFactory(ScalarsConverterFactory.create())//String
                .addConverterFactory(GsonConverterFactory.create())//json转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//添加RxJava适配器
                .build();
        ApiService apiService = retrofit.create(ApiService.class);//采用Java动态代理模式
        apiService.getNewsData("cb1df0b3977025de0ac5e241dba5cbc4", "10", page)
                .subscribeOn(Schedulers.io())
                .map(new Func1<NewsGson, List<News>>() {
                    @Override
                    public List<News> call(NewsGson newsGson) {
                        List<News> newsList = new ArrayList<>();
                        for (NewsGson.NewslistBean newslistBean : newsGson.getNewslist()) {
                            News new1 = new News();
                            new1.setTitle(newslistBean.getTitle());
                            new1.setCtime(newslistBean.getCtime());
                            new1.setDescription(newslistBean.getDescription());
                            new1.setPicUrl(newslistBean.getPicUrl());
                            new1.setUrl(newslistBean.getUrl());
                            newsList.add(new1);
                        }
                        return newsList;//返回类型
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<News>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<News> newsList) {
                        adapter.addAll(newsList);
                    }
                });
        page = page + 1;
    }
}

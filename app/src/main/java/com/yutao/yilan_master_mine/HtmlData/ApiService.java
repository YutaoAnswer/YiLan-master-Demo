package com.yutao.yilan_master_mine.HtmlData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/1/7.
 */

public interface ApiService {
    @GET("social/")
    Observable <String> getString(@Query("key")String key, @Query("num") String num, @Query("page") int page);

    @GET("social/")
    Observable <NewsGson> getNewsData(@Query("key")String key,@Query("num") String num,@Query("page") int page);

    @GET("meinv/")
    Observable <MeiNvGson> getPictureData(@Query("key")String key,@Query("num") String num,@Query("page") int page);
}

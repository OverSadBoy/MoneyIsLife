package Kazuki.moneyislife;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;
public interface Api {
    @GET("/items")
    Call<List<Item>> getItems(@Query("type") String type);
}

package Kazuki.moneyislife.api;

import com.google.gson.annotations.SerializedName;

public class BalanceResponse {
    public String status;
    @SerializedName("total_expenses")
    public int expense;
    @SerializedName("total_income")
    public int income;
}
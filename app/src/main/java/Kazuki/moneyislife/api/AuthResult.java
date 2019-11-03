package Kazuki.moneyislife.api;

import com.google.gson.annotations.SerializedName;

public class AuthResult {
    private String status;
    private int id;
    @SerializedName("auth_token")
    private String token;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}



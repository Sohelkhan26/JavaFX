package com.example.new_login_page;

public class SharedDataModel {
    private static SharedDataModel instance;
    private String data;

    private SharedDataModel() {}

    public static SharedDataModel getInstance() {
        if (instance == null) {
            instance = new SharedDataModel();
        }
        return instance;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
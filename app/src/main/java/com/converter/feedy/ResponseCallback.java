package com.converter.feedy;

public interface ResponseCallback {
    void onResponse(String response);
    void onError(Throwable throwable);
}

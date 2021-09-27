package com.creadle.vaccinesos;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class kakaoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this,"7be28d1255ec8798c7665bc682a40c34");
    }
}

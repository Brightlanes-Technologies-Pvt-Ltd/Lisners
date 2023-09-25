package com.lisners.counsellor.zWork.daggerClient;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Application mApplication;

    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Singleton
    @Provides
    Application provideApplication() {
        return this.mApplication;
    }




}


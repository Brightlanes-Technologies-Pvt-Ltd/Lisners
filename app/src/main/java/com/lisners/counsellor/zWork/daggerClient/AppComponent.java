package com.lisners.counsellor.zWork.daggerClient;


import com.lisners.counsellor.zWork.restApi.api.ApiS;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {ApiModule.class, AppModule.class})
@Singleton
public interface AppComponent {

     ApiS getApiS();

}
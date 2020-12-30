package com.ashutoku.userprofilemanageservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ashutoku.userprofilemanageservice.logging.interceptor.LogInterceptor;



@SuppressWarnings("deprecation")
@Component
public class LogInterceptorAppConfig extends WebMvcConfigurerAdapter {
   @Autowired
   LogInterceptor logInterceptor;

   @Override
   public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(logInterceptor);
   }
   
   
}
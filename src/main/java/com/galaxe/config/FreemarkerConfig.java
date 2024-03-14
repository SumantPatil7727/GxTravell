package com.galaxe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

public class FreemarkerConfig {

  @Bean
  @Primary
  public FreeMarkerConfigurationFactoryBean factoryBean() {
    FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
    bean.setTemplateLoaderPath("classpath:/templates");
    return bean;
  }
}

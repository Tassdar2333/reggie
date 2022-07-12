package com.itheima.reggie.config;

import com.itheima.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @version 1.0
 * @Auther wangchengyang
 * @Date 2022/6/4 13:43
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");


    }

    /**
     * 扩展mvc框架的消息转换器
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        //新建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        //设置对象转换器
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        //将自定义消息转换器 追加到mvc 转换器列表中
        converters.add(0,messageConverter);
    }
}

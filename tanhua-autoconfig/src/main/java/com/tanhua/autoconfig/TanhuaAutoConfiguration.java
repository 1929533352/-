package com.tanhua.autoconfig;


import com.tanhua.autoconfig.properties.AipFaceProperties;
import com.tanhua.autoconfig.properties.OssProperties;
import com.tanhua.autoconfig.properties.SmsProperties;
import com.tanhua.autoconfig.template.AipFaceTemplate;
import com.tanhua.autoconfig.template.OSSTemplate;
import com.tanhua.autoconfig.template.SmsTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({
        SmsProperties.class,
        OssProperties.class,
        AipFaceProperties.class
})
public class TanhuaAutoConfiguration {

    @Bean
    public SmsTemplate smsTemplate(SmsProperties properties) {
        return new SmsTemplate(properties);
    }
    @Bean
    public OSSTemplate OssTemplate(OssProperties properties) {
        return new OSSTemplate(properties);
    }
    @Bean
    public AipFaceTemplate  AipFaceTemplate() {
        return new AipFaceTemplate();
    }
}

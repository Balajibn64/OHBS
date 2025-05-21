package com.ohbs.security;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dkhfisd1d");
        config.put("api_key", "211544612893788");
        config.put("api_secret", "ME1w4ztSTORdaAz60YTLjTvy4MA");
        return new Cloudinary(config);
    }
}

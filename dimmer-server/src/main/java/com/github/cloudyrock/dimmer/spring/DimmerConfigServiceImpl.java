package com.github.cloudyrock.dimmer.spring;

import com.github.cloudyrock.dimmer.DimmerConfigResponse;
import com.github.cloudyrock.dimmer.DimmerConfigServiceSpring;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class DimmerConfigServiceImpl implements DimmerConfigServiceSpring {


    @Override
    public DimmerConfigResponse getConfigByEnvironment(@PathVariable("environment") String environment) {
        final String value = "OK -  " + environment + LocalDateTime.now().toString();
        return new DimmerConfigResponse(value);
//        try {
//            Thread.sleep(1000 * 60 * 10);
//        } catch(Throwable ex) {
//            ex.printStackTrace();
//        }
//        if(true) {
//            throw new RuntimeException("Problemmmm!");
//        }
//        return value;

    }

}

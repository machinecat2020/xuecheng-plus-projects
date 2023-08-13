package com.xuecheng.content.feignclient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author dongguohui
 * @description
 */
@Slf4j
@Component
public class MediaServiceClientFallbackFactory implements FallbackFactory<MediaServiceClient> {// MediaServiceClient为自定义的远程调用方法所在类
    //拿到了熔断的异常信息throwable，该方法(fallbackFactory)对比fallback好处就是可以拿到熔断异常信息
    @Override
    public MediaServiceClient create(Throwable throwable) {

        return new MediaServiceClient() {
            //发生熔断上传服务调用此方法执行降级逻辑
            @Override
            public String upload(MultipartFile filedata, String objectName) throws IOException {
                log.debug("远程调用上传文件的接口发生熔断:{}", throwable.toString(), throwable);
                return null;
            }
        };
    }
}

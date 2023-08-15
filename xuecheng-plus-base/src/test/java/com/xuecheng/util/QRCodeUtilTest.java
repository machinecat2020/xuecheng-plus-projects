package com.xuecheng.util;

import com.xuecheng.base.utils.QRCodeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class QRCodeUtilTest {

    @Test
    public void testQRCodeUtil() throws IOException {
        QRCodeUtil qrCodeUtil = new QRCodeUtil();

//        System.out.println(qrCodeUtil.createQRCode("http://www.itcast.cn/",200,200));
    }


}

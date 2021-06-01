package com.ming.ls.modules.sys.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PayService {

    void alipay(String actualPayment,HttpServletRequest request, HttpServletResponse response) throws IOException;
}

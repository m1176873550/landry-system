package com.ming.ls.modules.common.controller;

import com.ming.ls.modules.sys.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class PayController {

    @Autowired
    private PayService payService;

    @RequestMapping("/pay/{actualPayment}")
    public void payController(@PathVariable("actualPayment") String actualPayment, HttpServletRequest request, HttpServletResponse response) throws IOException {
        payService.alipay(actualPayment,request,response);
    }
}


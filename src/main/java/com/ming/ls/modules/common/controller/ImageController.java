package com.ming.ls.modules.common.controller;

import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.sys.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author : 明杰
 * @date : 2020/1/13 图片控制器

 * @return : null
 */

@Controller
@RequestMapping("/images/")
public class ImageController {

    /**
     * 职员业务类
     */
    @Autowired
    private IEmployeeService iEmployeeService;

    @GetMapping(value = "/employee/{id}")
    public ServerResponse view(@PathVariable(name = "id") String id, HttpServletResponse res) throws IOException {
        iEmployeeService.urlImage(id,res);
        return ServerResponse.createBySuccess();
    }
}

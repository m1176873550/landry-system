package com.ming.ls.modules.sys.controller;


import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.sys.service.ILaundryService;
import com.ming.ls.modules.sys.vo.Laundry.IdAndNameList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mj
 * @since 2020-01-10
 */
@RestController
@RequestMapping("/laundry")
public class LaundryController {

    @Autowired
    private ILaundryService iLaundryService;

    @GetMapping("/id_and_name_list")
    public ServerResponse idAndNameList(){
        List<IdAndNameList> idAndNameLists = iLaundryService.laundryIdAndNameList();
        return ServerResponse.createBySuccess(idAndNameLists);
    }

}

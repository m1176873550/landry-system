package com.ming.ls.modules.sys.controller;


import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.common.util.ValidUtils;
import com.ming.ls.modules.sys.entity.Color;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.service.IColorService;
import com.ming.ls.modules.sys.vo.color.ColorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@RestController
@RequestMapping("/color")
public class ColorController {

    @Autowired
    private IColorService iColorService;

    /**
     * 列表
     * @param query
     * @return
     */
    @GetMapping()
    public ServerResponse list(BaseQuery query){
        List<Color> list = iColorService.list(query);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ServerResponse delColor(@PathVariable("id") String id){
        iColorService.checkId(id);
        if (iColorService.removeById(id)){
            return ServerResponse.createBySuccess();
        }else {
            return ServerResponse.createByErrorMessage("数据库异常,删除颜色失败");
        }
    }

    /**
     * 编辑
     * @param colorVo
     * @param errors
     * @return
     */
    @PutMapping()
    @Validated(ColorVo.Update.class)
    public ServerResponse editColor(@RequestBody ColorVo colorVo, Errors errors){
        ValidUtils.validateArg(errors);
        if (iColorService.editColor(colorVo)) {
            return ServerResponse.createBySuccess();
        }
            return ServerResponse.createByErrorMessage("数据库异常，编辑颜色失败");
    }

    /**
     * 新增
     * @param colorVo
     * @return
     */
    @PostMapping()
    @Validated(ColorVo.Create.class)
    public ServerResponse addColor(@RequestBody ColorVo colorVo, Errors errors){
        ValidUtils.validateArg(errors);
        if (iColorService.addColor(colorVo.getName())) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("数据库异常，编辑颜色失败");
    }
}

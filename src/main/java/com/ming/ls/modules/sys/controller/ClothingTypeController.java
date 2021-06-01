package com.ming.ls.modules.sys.controller;


import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.sys.entity.ClothingType;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.service.IClothingTypeService;
import com.ming.ls.modules.sys.vo.cloth.ClothTypeGroup;
import com.ming.ls.modules.sys.vo.cloth.ClothVo;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/clothingType")
public class ClothingTypeController {

    @Autowired
    private IClothingTypeService iClothingTypeService;

    /**
     * 列表
     * @param query
     * @return
     */
    @PostMapping("/list")
    public ServerResponse list(@RequestBody BaseQuery query){
        List<ClothingType> list = iClothingTypeService.list(query);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 新增
     * @param clothVo
     * @return
     */
    @PostMapping()
    @Validated(value = ClothVo.Create.class)
    public ServerResponse addCloth(@RequestBody ClothVo clothVo){
        if (iClothingTypeService.add(clothVo)){
            return ServerResponse.createBySuccess();
        }else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 编辑
     * @param clothVo
     * @return
     */
    @PutMapping()
    @Validated(value = ClothVo.Update.class)
    public ServerResponse editCloth(@RequestBody ClothVo clothVo){
        if (iClothingTypeService.edit(clothVo)){
            return ServerResponse.createBySuccess();
        }else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ServerResponse delCloth(@PathVariable String id) {
        if (iClothingTypeService.removeById(id)) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("数据库异常，衣物类型删除失败");
        }
    }
    /**
     * 检查父级是否存在，不存在就新建
     *
     * @param addClothParent
     */
    @PostMapping("/check-parent/{addClothParent}")
    public ServerResponse checkParent(@PathVariable String addClothParent){
        iClothingTypeService.checkParent(addClothParent);
        return ServerResponse.createBySuccess();
    }

    /**
     * 父级列表
     * @return
     */
    @GetMapping("/parent-types")
    public ServerResponse parentTypes(){
        List<ClothingType> types = iClothingTypeService.parentTypes();
        return ServerResponse.createBySuccess(types);
    }
    /**
     * 级联分组
     *
     * @return
     */
    @GetMapping("/group")
    public ServerResponse group(){
        List<ClothTypeGroup> list = iClothingTypeService.group();
        return ServerResponse.createBySuccess(list);
    }
    /**
     * 改变价格
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ServerResponse changePrice(@PathVariable() String id){
        String price = iClothingTypeService.changePrice(id);
        return ServerResponse.createBySuccess(price);
    }
}

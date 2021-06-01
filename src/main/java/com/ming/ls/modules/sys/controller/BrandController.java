package com.ming.ls.modules.sys.controller;


import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.common.util.ValidUtils;
import com.ming.ls.modules.sys.entity.Brand;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.service.IBrandService;
import com.ming.ls.modules.sys.vo.brand.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private IBrandService iBrandService;


    /**
     * 列表
     *
     * @return
     */
    @GetMapping()
    public ServerResponse list(BaseQuery query) {
        List<Brand> list = iBrandService.list(query);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 编辑
     *
     * @param brandVo
     * @return
     */
    @PutMapping()
    @Validated({BrandVo.Update.class})
    public ServerResponse editBrand(@RequestBody BrandVo brandVo, Errors errors) {
        ValidUtils.validateArg(errors);
        iBrandService.editBrand(brandVo);
        return ServerResponse.createBySuccess();
    }

    /**
     * 新增
     *
     * @param brandVo
     * @return
     */
    @PostMapping()
    @Validated({BrandVo.Create.class})
    public ServerResponse addBrand(@RequestBody BrandVo brandVo, Errors errors) {
        ValidUtils.validateArg(errors);
        iBrandService.addBrand(brandVo);
        return ServerResponse.createBySuccess();
    }

    /**
     * 删除
     *
     * @param
     * @return
     */
    @DeleteMapping("/{id}")
    public ServerResponse delBrand(@PathVariable("id") String id) {
        iBrandService.checkId(id);
        if (iBrandService.removeById(id)){
            return ServerResponse.createBySuccess();
        }else {
            return ServerResponse.createByErrorMessage("删除失败，数据库异常");
        }
    }
}

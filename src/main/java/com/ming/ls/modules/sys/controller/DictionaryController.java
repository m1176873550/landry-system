package com.ming.ls.modules.sys.controller;


import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.common.util.ValidUtils;
import com.ming.ls.modules.sys.service.IDictionaryService;
import com.ming.ls.modules.sys.vo.dictionary.DictionaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {


    @Autowired
    private IDictionaryService iDictionaryService;

    /**
     * 根据name查找value
     * @param name
     * @return
     */
    @GetMapping("/{name}")
    public ServerResponse findValueByName(@PathVariable String name){
        Integer value = iDictionaryService.findValueByName(name);
        return ServerResponse.createBySuccess(value);
    }


    /**
     * 编辑
     * @param dct
     * @param errors
     * @return
     */
    @PutMapping
    @Validated(value = DictionaryVo.Update.class)
    public ServerResponse findValueByName(@RequestBody DictionaryVo dct, Errors errors){
        ValidUtils.validateArg(errors);
        iDictionaryService.edit(dct);
        return ServerResponse.createBySuccess();
    }
}

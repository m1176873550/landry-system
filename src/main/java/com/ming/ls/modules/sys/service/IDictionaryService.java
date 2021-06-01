package com.ming.ls.modules.sys.service;

import com.ming.ls.modules.sys.entity.Dictionary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.ls.modules.sys.vo.dictionary.DictionaryVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
public interface IDictionaryService extends IService<Dictionary> {
     /**
      * 根据名称查询value
      * @param name
      * @return
      */
     Integer findValueByName(String name);

     /**
      * 新增
      * @return
      */
     boolean add(DictionaryVo dictionaryVo);

     /**
      * 编辑
      * @return
      */
     boolean edit(DictionaryVo dictionaryVo);
     /**
      * 检查名称
      * @param name
      * @return
      */
     Dictionary checkName(String name);
}

package com.ming.ls.modules.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.dao.DictionaryDao;
import com.ming.ls.modules.sys.entity.Dictionary;
import com.ming.ls.modules.sys.service.IDictionaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.sys.vo.dictionary.DictionaryVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryDao, Dictionary> implements IDictionaryService {
    @Autowired
    private IDictionaryService iDictionaryService;
    @Autowired
    private DictionaryDao dictionaryDao;

    /**
     * 根据唯一的名字查找VALUE
     *
     * @param name
     * @return
     */
    @Override
    public Integer findValueByName(String name) {
        Dictionary dictionary = iDictionaryService.checkName(name);
        if (dictionary!=null){
            return Integer.parseInt(dictionary.getValue());
        }else {
            throw new LsServiceException("字典名称异常");
        }
    }

    /**
     * 新增
     *
     * @return
     */
    @Override
    public boolean add(DictionaryVo dictionaryVo) {
        return false;
    }

    /**
     * 编辑
     *
     * @param dictionaryVo
     * @return
     */
    @Override
    public boolean edit(DictionaryVo dictionaryVo) {
        Dictionary dct = iDictionaryService.checkName(dictionaryVo.getName());
        //修改
        if (!StringUtils.equals(dct.getName(), dictionaryVo.getName()) ||
                !StringUtils.equals(dct.getValue(), dictionaryVo.getValue()) ||
                !StringUtils.equals(dct.getDes(), dictionaryVo.getDes())) {
            BeanUtil.copyProperties(dictionaryVo,dct,"id");
            if (iDictionaryService.updateById(dct)){
                return true;
            }else {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "更新失败，数据库异常");
            }
        }
        //未修改
        return true;
    }

    /**
     * 字典名称检查
     *
     * @param name
     * @return
     */
    @Override
    public Dictionary checkName(String name) {
        String laundryId = "1";
        Dictionary dct = dictionaryDao.selectOne(new LambdaQueryWrapper<Dictionary>()
                .eq(Dictionary::getName, name)
                .eq(Dictionary::getLaundryId, laundryId));
        if (dct != null) {
            return dct;
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "字典名称异常");
        }

    }
}

package com.ming.ls.modules.sys.service;

import com.ming.ls.modules.sys.entity.Laundry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.ls.modules.sys.vo.Laundry.IdAndNameList;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mj
 * @since 2020-01-10
 */
public interface ILaundryService extends IService<Laundry> {
    /**
     * 店铺ID和名字
     * @return
     */
    List<IdAndNameList> laundryIdAndNameList();

}

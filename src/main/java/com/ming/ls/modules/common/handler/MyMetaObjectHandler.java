package com.ming.ls.modules.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ming.ls.modules.common.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis自动注入
 *
 * @PackgeName: com.gdcccn.oa.modules.common.handler
 * @ClassName: MyMetaObjectHandler
 * @Author: mj
 * Date: 2019/10/9 16:18
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        // 获取到需要被填充的字段值
        Object updatedAt = getFieldValByName("updatedAt", metaObject);
        Object createdAt = getFieldValByName("createdAt", metaObject);
        Object creatorId = getFieldValByName("creatorId", metaObject);
        Object editorId = getFieldValByName("editorId", metaObject);
        Object laundryId=getFieldValByName("laundryId",metaObject);

        if (updatedAt == null) {
            setInsertFieldValByName("updatedAt", LocalDateTime.now(), metaObject);
        }
        if (createdAt == null) {
            setInsertFieldValByName("createdAt", LocalDateTime.now(), metaObject);
        }

        if (creatorId == null) {
            setInsertFieldValByName("creatorId", SessionUtil.getCurrentUser().getId(), metaObject);
        }
        if (editorId == null) {
            setInsertFieldValByName("editorId", SessionUtil.getCurrentUser().getId(), metaObject);
        }
        if (laundryId == null) {
            setInsertFieldValByName("laundryId",
                    SessionUtil.getCurrentUser().getLaundryId(),
                    metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 获取到需要被填充的字段值
        Object updatedAt = getFieldValByName("updatedAt", metaObject);
        Object editorId = getFieldValByName("editorId", metaObject);

        if (updatedAt == null) {
            setUpdateFieldValByName("updatedAt", LocalDateTime.now(), metaObject);
        }

        if (editorId == null) {
            setInsertFieldValByName("editorId", SessionUtil.getCurrentUser().getId(), metaObject);
        }
    }
}

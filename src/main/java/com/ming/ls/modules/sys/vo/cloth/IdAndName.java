package com.ming.ls.modules.sys.vo.cloth;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author : 明杰
 * @date : 2020/2/10 13:22
    父级实体类
 * @return : null
 */
@Data
@Accessors(chain = true)
public class IdAndName {
    private String id;
    private String name;
}

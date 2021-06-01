package com.ming.ls.modules.sys.vo.dictionary;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class DictionaryVo {
//    private String id;
    @NotNull(message = "字典名称不能为空",groups = {Update.class,Create.class})
    private String name;
    @NotNull(message = "字典值不能为空",groups = {Update.class,Create.class})
    private String value;
    private String des;
    public static interface Create{}
    public static interface Update{}
}

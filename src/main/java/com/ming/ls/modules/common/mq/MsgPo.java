package com.ming.ls.modules.common.mq;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MsgPo implements Serializable {
    public MsgPo() {
    }

    private static final long serialVersionUID = 71550616471159984L;
    private String phones;
    private String text;

    public MsgPo(String phones, String text) {
        this.phones = phones;
        this.text = text;
    }
}

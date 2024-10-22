package org.xltx.bizrouter.demo;

import org.xltx.bizrouter.annotations.BizComponentDefault;

@BizComponentDefault()
public class BizAComponentDefaultImpl implements BizAComponent{
    @Override
    public String executeA(String param) {
        return "BizA default component, param = {" + param + "}";
    }
}

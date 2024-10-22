package org.xltx.bizrouter.demo;

import org.xltx.bizrouter.annotations.BizComponentDefault;

@BizComponentDefault()
public class BizBComponentDefaultImpl implements BizBComponent{
    @Override
    public String executeB(String param) {
        return "BizB default component, param = {" + param + "}";
    }
}

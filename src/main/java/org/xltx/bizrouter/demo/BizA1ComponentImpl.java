package org.xltx.bizrouter.demo;

import org.xltx.bizrouter.annotations.BizComponentExt;

@BizComponentExt(bizTypes = {"A11", "A12"})
public class BizA1ComponentImpl implements BizAComponent{
    @Override
    public String executeA(String param) {
        return "BizA A1 component, param = {" + param + "}";
    }
}

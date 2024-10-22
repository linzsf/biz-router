package org.xltx.bizrouter.demo;

import org.xltx.bizrouter.annotations.BizComponentExt;

@BizComponentExt(bizTypes = {"A21"})
public class BizA2ComponentImpl implements BizAComponent{
    @Override
    public String executeA(String param) {
        return "BizA A2 component, param = {" + param + "}";
    }
}

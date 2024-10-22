package org.xltx.bizrouter.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xltx.bizrouter.execute.BizComponentSelector;

@Slf4j
@Component
public class TestComponent {

    public void test() {
        log.info(BizComponentSelector.execute(BizAComponent.class, "A11", BizAComponent::executeA, "hello A11"));
        log.info(BizComponentSelector.execute(BizAComponent.class, "A12", BizAComponent::executeA, "hello A12"));
        log.info(BizComponentSelector.execute(BizAComponent.class, "A21", BizAComponent::executeA, "hello A21"));
        log.info(BizComponentSelector.execute(BizAComponent.class, "A31", BizAComponent::executeA, "hello A31"));
    }
}

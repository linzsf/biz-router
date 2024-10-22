package org.xltx.bizrouter.registry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.xltx.bizrouter.annotations.BizComponentDefault;
import org.xltx.bizrouter.annotations.BizComponentExt;

@Slf4j
@Component
public class BizComponentBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 获取 bean 的类对象
        if (!ComponentHolder.existBizComponent()) {
            return bean;
        }
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(BizComponentDefault.class)) {
            // BizComponentDefault，并根据className找到对应的bizCode，并注册到ComponentHolder.componentDefaultMap中
            ComponentHolder.registComponentDefault(beanClass, bean);
        } else if (beanClass.isAnnotationPresent(BizComponentExt.class)) {
            ComponentHolder.registComponentExt(beanClass, bean);
        }
        return bean;
    }
}

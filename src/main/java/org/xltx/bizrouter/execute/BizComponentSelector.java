package org.xltx.bizrouter.execute;

import lombok.extern.slf4j.Slf4j;
import org.xltx.bizrouter.exception.NotFoundBizComponentImplException;
import org.xltx.bizrouter.registry.ComponentHolder;

@Slf4j
public class BizComponentSelector {

    @SuppressWarnings("unchecked")
    public static <T, P, R> R execute(Class<T> interfaceClazz, String bizType, MethodCaller<T, P, R> method, P request) {
        Object componentBean = ComponentHolder.select(interfaceClazz, bizType);
        if (componentBean == null) {
            log.error("found no matching biz component, interfaceClass = '{}', bizType = '{}'", interfaceClazz.getName(), bizType);
            throw new NotFoundBizComponentImplException(String.format("found no matching biz component, interfaceClass = '%s', bizType = '%s'", interfaceClazz.getName(), bizType));
        }
        if (!interfaceClazz.isInstance(componentBean)) {
            log.error("selected bean is not an instance of {}", interfaceClazz.getName());
            throw new NotFoundBizComponentImplException(String.format("selected bean is not an instance of %s", interfaceClazz.getName()));
        }
        return method.call((T) componentBean, request);
    }

    @SuppressWarnings("unchecked")
    public static <T, P> void executeVoid(Class<T> interfaceClazz, String bizType, MethodCallerWithOutResult<T, P> method, P request) {
        Object componentBean = ComponentHolder.select(interfaceClazz, bizType);
        if (componentBean == null) {
            log.error("found no matching biz component, interfaceClass = '{}', bizType = '{}'", interfaceClazz.getName(), bizType);
            throw new NotFoundBizComponentImplException(String.format("found no matching biz component, interfaceClass = '%s', bizType = '%s'", interfaceClazz.getName(), bizType));
        }
        if (!interfaceClazz.isInstance(componentBean)) {
            log.error("selected bean is not an instance of {}", interfaceClazz.getName());
            throw new NotFoundBizComponentImplException(String.format("selected bean is not an instance of %s", interfaceClazz.getName()));
        }
        method.call((T) componentBean, request);
    }

    @FunctionalInterface
    public interface MethodCaller<T, P, R> {

        R call(T instance, P request);
    }

    @FunctionalInterface
    public interface MethodCallerWithOutResult<T, P> {

        void call(T instance, P request);
    }
}

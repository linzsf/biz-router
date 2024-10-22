package org.xltx.bizrouter.annotations;

import org.springframework.context.annotation.Import;
import org.xltx.bizrouter.registry.ImportBizComponentRegistrar;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ImportBizComponentRegistrar.class)
public @interface EnableBizRouter {

    public String[] basePackages();
}

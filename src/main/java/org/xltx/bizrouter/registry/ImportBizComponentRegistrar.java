package org.xltx.bizrouter.registry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.xltx.bizrouter.annotations.BizComponent;
import org.xltx.bizrouter.annotations.BizComponentDefault;
import org.xltx.bizrouter.annotations.BizComponentExt;
import org.xltx.bizrouter.annotations.EnableBizRouter;

import java.util.*;

@Slf4j
public class ImportBizComponentRegistrar implements ImportBeanDefinitionRegistrar {
    private final Set<Class<?>> bizComponentSet = new LinkedHashSet<>();
    private final Set<Class<?>> bizComponentDefaultSet = new LinkedHashSet<>();
    private final Set<Class<?>> bizComponentExtSet = new LinkedHashSet<>();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false);
        // 设置包扫描过滤器
        scanner.addIncludeFilter((r, f) -> {
            ClassMetadata classMetadata = r.getClassMetadata();
            AnnotationMetadata annotationMetadata = r.getAnnotationMetadata();
            return bizComponentFilter(classMetadata, annotationMetadata);
        });

        // 执行包扫描，扫描BizComponent接口、默认实现和业务实现
        for (String basePackage : getBasePackages(importingClassMetadata)) {
            scanner.findCandidateComponents(basePackage);
        }

        if (CollectionUtils.isEmpty(bizComponentSet)) {
            log.info("found no interface with annotation '@BizComponent'");
            return;
        }

        // 校验是否所有BizComponent接口都存在默认实现类
        bizComponentDefaultSet.forEach(ComponentHolder::registComponentDefaultDefinition);

        // 将业务实现类和对应的接口放到ComponentHolder中，并且校验是否每个实现类都只实现了一个BizComponent接口
        bizComponentExtSet.forEach(ComponentHolder::registComponentExtDefinition);

        // 校验是否所有BizComponent都有默认实现
        bizComponentSet.forEach(ComponentHolder::checkBizComponentDefault);
    }

    /**
     * 扫描注解过滤
     * @param classMetadata
     * @param annotationMetadata
     * @return
     */
    private boolean bizComponentFilter(ClassMetadata classMetadata, AnnotationMetadata annotationMetadata) {
        String className = annotationMetadata.getClassName();
        try {
            Class<?> clazz = Class.forName(className);
            if (classMetadata.isInterface()) {
                // 扫描包含BizComponent注解的接口，校验注解参数，并注册到ComponentHolder
                if (annotationMetadata.hasAnnotation(BizComponent.class.getName())) {
                    bizComponentSet.add(clazz);
                    ComponentHolder.registComponentDefinition(className);
                    return true;
                }
            } else if (annotationMetadata.hasAnnotation(BizComponentDefault.class.getName())) {
                bizComponentDefaultSet.add(clazz);
                return true;
            } else if (annotationMetadata.hasAnnotation(BizComponentExt.class.getName())) {
                bizComponentExtSet.add(clazz);
                return true;
            }
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("class %s not found", className));
        }
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Set<String> basePackages = new HashSet<>();

        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableBizRouter.class.getCanonicalName());

        //如果没指定 value/basePackages
        if (attributes == null || !attributes.containsKey("basePackages")) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
            return basePackages;
        }

        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        //如果没指定 value/basePackages
        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }

}
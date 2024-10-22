package org.xltx.bizrouter.registry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.xltx.bizrouter.annotations.BizComponentExt;
import org.xltx.bizrouter.exception.ComponentDefinitionException;
import org.xltx.bizrouter.exception.NotFoundBizComponentImplException;

import java.util.*;

@Slf4j
public class ComponentHolder {

    // 组件接口
    private static final List<String> componentInterfaces = new ArrayList<>();

    // key: bizComponent业务实现类的全限定名，value: bizComponent接口的全限定名
    private static final Map<String, String> componentExtInterfaceMap = new HashMap<>();

    // key: bizComponent默认实现类的全限定名，value: bizComponent接口的全限定名
    private static final Map<String, String> componentDefaultInterfaceMap = new HashMap<>();

    // 外层key: BizComponent接口全限定名，value：BizComponent接口默认实现类的bean
    private static final Map<String, Object> componentDefaultMap = new HashMap<>();

    // 外层key: BizComponent接口全限定名，内层key: BizComponentExt.bizTypes，value：所有实现了BizComponent接口的bean
    private static final Map<String, Map<String, Object>> componentExtMap = new HashMap<>();

    /**
     * 注册BizComponent接口定义
     * @param interfaceClassName
     */
    public static void registComponentDefinition(String interfaceClassName) {
        if (componentInterfaces.contains(interfaceClassName)) {
            log.error("there is more than one component interface named {}", interfaceClassName);
            throw new ComponentDefinitionException("replicate component interface class name");
        }
        componentInterfaces.add(interfaceClassName);
        componentExtMap.put(interfaceClassName, new HashMap<>());
    }

    /**
     * 注册BizComponentExt定义
     * @param clazz
     */
    public static void registComponentExtDefinition(Class<?> clazz) {
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> i : interfaces) {
            // 不是BizComponent接口，跳过
            if (!componentInterfaces.contains(i.getName())) {
                continue;
            }
            // 该类实现了多个BizComponent接口
            if (componentExtInterfaceMap.containsKey(clazz.getName())) {
                log.error("bizComponentImpl '{}' implement more than one interface with @BizComponent", clazz.getName());
                throw new ComponentDefinitionException(String.format("bizComponentImpl '%s' implement more than one interface with @BizComponent", clazz.getName()));
            }
            // 绑定bizComponent接口和实现类
            componentExtInterfaceMap.put(clazz.getName(), i.getName());
        }
        // 该类没有实现任何BizComponent接口
        if (!componentExtInterfaceMap.containsKey(clazz.getName())) {
            log.error("bizComponentImpl '{}' not implement any bizComponent interface", clazz.getName());
            throw new ComponentDefinitionException(String.format("bizComponentExt '%s' not implement any bizComponent interface", clazz.getName()));
        }
    }

    /**
     * 注册BizComponentDefault定义
     * @param clazz
     */
    public static void registComponentDefaultDefinition(Class<?> clazz) {
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> i : interfaces) {
            // 不是BizComponent接口，跳过
            if (!componentInterfaces.contains(i.getName())) {
                continue;
            }
            // 该类实现了多个BizComponent接口
            if (componentDefaultInterfaceMap.containsKey(clazz.getName())) {
                log.error("bizComponentImpl '{}' implement more than one interface with @BizComponent", clazz.getName());
                throw new ComponentDefinitionException(String.format("bizComponentImpl '%s' implement more than one interface with @BizComponent", clazz.getName()));
            }
            // 绑定bizComponent接口和实现类
            componentDefaultInterfaceMap.put(clazz.getName(), i.getName());
        }
        // 该类没有实现任何BizComponent接口
        if (!componentDefaultInterfaceMap.containsKey(clazz.getName())) {
            log.error("bizComponentImpl '{}' not implement any bizComponent interface", clazz.getName());
            throw new ComponentDefinitionException(String.format("bizComponentExt '%s' not implement any bizComponent interface", clazz.getName()));
        }
    }

    /**
     * 注册BizComponentExt的Bean
     * @param clazz
     * @param bean
     */
    public static void registComponentExt(Class<?> clazz, Object bean) {
        String className = clazz.getName();
        BizComponentExt bizComponentExt = clazz.getAnnotation(BizComponentExt.class);
        List<String> bizTypes = Arrays.asList(bizComponentExt.bizTypes());
        if (CollectionUtils.isEmpty(bizTypes)) {
            log.error("bizComponentExt '{}' not defined bizTypes", className);
            throw new ComponentDefinitionException(String.format("bizComponentExt '%s' not defined bizTypes", className));
        }
        if (!componentExtInterfaceMap.containsKey(className)) {
            log.error("bizComponentExt '{}' not defined bizTypes", className);
            throw new ComponentDefinitionException(String.format("bizComponentExt '%s' not defined bizTypes", className));
        }
        String interfaceName = componentExtInterfaceMap.get(className);
        bizTypes.forEach(bizType -> componentExtMap.get(interfaceName).put(bizType, bean));
    }

    /**
     * 注册BizComponentDefault的Bean
     * @param clazz
     * @param bean
     */
    public static void registComponentDefault(Class<?> clazz, Object bean) {
        String className = clazz.getName();
        if (!componentDefaultInterfaceMap.containsKey(className)) {
            log.error("'{}' not a BizComponent default implement", className);
            throw new ComponentDefinitionException(String.format("'%s' not a BizComponent default implement", className));
        }
        String interfaceName = componentDefaultInterfaceMap.get(className);
        componentDefaultMap.put(interfaceName, bean);
    }

    /**
     * 校验BizComponent有没有默认实现
     * @param clazz
     */
    public static void checkBizComponentDefault(Class<?> clazz) {
        String className = clazz.getName();
        if (!componentDefaultInterfaceMap.containsValue(className)) {
            log.error("BizComponent '{}' has no default implement", className);
            throw new ComponentDefinitionException(String.format("BizComponent '%s' has no default implement", className));
        }
    }
    /**
     * 匹配Bean
     * @param interfaceClass
     * @param bizType
     * @return
     */
    public static Object select(Class<?> interfaceClass, String bizType) {
        String interfaceName = interfaceClass.getName();
        if (!componentExtMap.containsKey(interfaceName) || !componentExtMap.get(interfaceName).containsKey(bizType)) {
            if (!componentDefaultMap.containsKey(interfaceName)) {
                log.error("BizComponent interface {} has no matching implement", interfaceName);
                throw new NotFoundBizComponentImplException(String.format("BizComponent interface %s has no matching implement", interfaceName));
            }
            return componentDefaultMap.get(interfaceName);
        }
        return componentExtMap.get(interfaceName).get(bizType);
    }

    public static boolean existBizComponent() {
        return !CollectionUtils.isEmpty(componentInterfaces);
    }
}

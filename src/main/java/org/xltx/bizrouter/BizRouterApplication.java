package org.xltx.bizrouter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.xltx.bizrouter.annotations.EnableBizRouter;
import org.xltx.bizrouter.demo.TestComponent;

@Slf4j
@SpringBootApplication
@EnableBizRouter(basePackages = {"org.xltx.bizrouter.demo"})
public class BizRouterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BizRouterApplication.class, args);
        TestComponent testComponent = context.getBean(TestComponent.class);
        testComponent.test();
    }

}

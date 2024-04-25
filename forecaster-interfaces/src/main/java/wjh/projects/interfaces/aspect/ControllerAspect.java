package wjh.projects.interfaces.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import wjh.projects.common.util.JsonUtil;

@Aspect
@Component
public class ControllerAspect {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Pointcut("execution(public * wjh.projects.interfaces.controller.*.*(..))")
    public void request() {
    }

    @Before("request()")
    public void enhance(JoinPoint joinPoint) {
        logger.info("接收HTTP请求，请求参数：{}", JsonUtil.toJson(joinPoint.getArgs()));
    }

    @AfterReturning("request()")
    public void enhance1(JoinPoint joinPoint) {
        logger.info("完成HTTP请求，请求参数：{}", JsonUtil.toJson(joinPoint.getArgs()));
    }
}

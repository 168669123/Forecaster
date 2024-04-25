package wjh.projects.interfaces.aspect;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class MqConsumerAspect {
    private static final Logger logger = LoggerFactory.getLogger(MqConsumerAspect.class);

    @Pointcut("execution(public * wjh.projects.interfaces.mq.*.getMessage(String, String, ..))")
    public void getMessage() {
    }

    @Around("getMessage()")
    public Object enhance(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String topic = (String) args[0];
        String group = (String) args[1];
        if (args[2] instanceof ConsumerRecord) {
            ConsumerRecord<?, ?> record = (ConsumerRecord<?, ?>) args[2];
            Optional<? extends ConsumerRecord<?, ?>> message = Optional.of(record);
            try {
                logger.info("消费主题 {} 中消息：{}", topic, new JSONObject(message.get().value().toString()));
                Object proceed = joinPoint.proceed();
                logger.info("提交位移，当前主题：{}、消费者组：{} \n", topic, group);
                return proceed;
            } catch (Throwable e) {
                throw new RuntimeException("切面执行方法错误：" + e.getMessage());
            }
        }
        throw new RuntimeException("当前切点无法对目标方法进行增强操作");
    }
}

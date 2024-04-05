package wjh.projects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import wjh.projects.application.service.EstimateArriveService;
import wjh.projects.common.util.SpringContextUtil;

@SpringBootApplication
public class ForecasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForecasterApplication.class, args);
        SpringContextUtil.getBean(EstimateArriveService.class).estimate();
    }
}

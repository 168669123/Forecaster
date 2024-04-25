package application;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import wjh.projects.ForecasterApplication;
import wjh.projects.application.service.EstimateArriveAppService;

import javax.annotation.Resource;

@SpringBootTest(classes = ForecasterApplication.class)
class EstimateArriveAppServiceTest {
    @Resource
    private EstimateArriveAppService estimateArriveAppService;

    @Test
    void estimate() {
//        estimateArriveAppService.estimate();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
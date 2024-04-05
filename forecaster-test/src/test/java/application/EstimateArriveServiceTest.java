package application;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import wjh.projects.ForecasterApplication;
import wjh.projects.application.service.EstimateArriveService;

import javax.annotation.Resource;

@SpringBootTest(classes = ForecasterApplication.class)
class EstimateArriveServiceTest {
    @Resource
    private EstimateArriveService estimateArriveService;

    @Test
    void estimate() {
        estimateArriveService.estimate();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
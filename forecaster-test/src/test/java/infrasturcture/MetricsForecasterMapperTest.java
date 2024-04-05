package infrasturcture;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import wjh.projects.ForecasterApplication;
import wjh.projects.infrastructure.dao.MetricsEstimateDO;
import wjh.projects.infrastructure.dao.MetricsTrackDO;
import wjh.projects.infrastructure.mapper.MetricsForecasterMapper;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest(classes = ForecasterApplication.class)
class MetricsForecasterMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(MetricsForecasterMapperTest.class);
    @Resource
    private MetricsForecasterMapper metricsForecasterMapper;

    @Test
    void testSaveMetricsEstimateDO() {
        MetricsEstimateDO estimateDO = new MetricsEstimateDO();
        estimateDO.setRecordKey("test_saveMetricsEstimateDO");
        estimateDO.setDuration(10);
        estimateDO.setDeviation(10);
        estimateDO.setCreateTime(new Date());
        metricsForecasterMapper.saveMetricsEstimateDO(estimateDO);
        logger.info("测试-保存预计到数据记录");
    }

    @Test
    void testSaveMetricsTrackDO() {
        MetricsTrackDO trackDO = new MetricsTrackDO();
        trackDO.setRecordKey("test_testSaveMetricsTrackDO");
        trackDO.setLatitude(30.664303);
        trackDO.setLongitude(120.75632);
        trackDO.setCreateTime(new Date());
        metricsForecasterMapper.saveMetricsTrackDO(trackDO);
        logger.info("测试-保存运输车辆轨迹数据记录");
    }
}
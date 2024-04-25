package wjh.projects.interfaces.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wjh.projects.facade.metrics.MetricsFacade;
import wjh.projects.facade.metrics.request.MetricsRequest;
import wjh.projects.facade.metrics.response.MetricsDTO;
import wjh.projects.interfaces.config.ResponseCodeEnum;
import wjh.projects.interfaces.config.ResponseMessage;

import java.util.List;

@RestController
@RequestMapping("/metrics")
public class MetricsController {
    private final MetricsFacade metricsFacade;

    @Autowired
    public MetricsController(MetricsFacade metricsFacade) {
        this.metricsFacade = metricsFacade;
    }

    @GetMapping(value = "/query")
    public ResponseMessage query(@Validated MetricsRequest metricsRequest) {
        List<MetricsDTO> metricsDTOS = metricsFacade.listMetricsDTO(metricsRequest);
        return new ResponseMessage(ResponseCodeEnum.SUCCESS, metricsDTOS);
    }
}

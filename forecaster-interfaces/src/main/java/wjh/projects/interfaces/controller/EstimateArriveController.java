package wjh.projects.interfaces.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wjh.projects.facade.estimateArrive.EstimateArriveFacade;
import wjh.projects.facade.estimateArrive.request.EstimateRequest;
import wjh.projects.facade.estimateArrive.response.EstimateArriveDTO;
import wjh.projects.interfaces.config.ResponseCodeEnum;
import wjh.projects.interfaces.config.ResponseMessage;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/estimateArrive")
public class EstimateArriveController {
    @Resource
    private EstimateArriveFacade estimateArriveFacade;

    @PostMapping(value = "/estimate")
    public ResponseMessage estimate(@Validated @RequestBody EstimateRequest estimateRequest) {
        List<EstimateArriveDTO> estimateArriveDTOS = estimateArriveFacade.estimate(estimateRequest);
        if (estimateArriveDTOS.size() == 0) {
            return new ResponseMessage(ResponseCodeEnum.ESTIMATE_WAIT, estimateArriveDTOS);
        } else {
            return new ResponseMessage(ResponseCodeEnum.SUCCESS, estimateArriveDTOS);
        }
    }
}

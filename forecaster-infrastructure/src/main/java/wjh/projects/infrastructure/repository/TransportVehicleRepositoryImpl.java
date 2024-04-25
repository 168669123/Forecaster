package wjh.projects.infrastructure.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Repository;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import wjh.projects.common.constants.KafkaTopicConst;
import wjh.projects.common.util.DateUtil;
import wjh.projects.common.util.JsonUtil;
import wjh.projects.domain.estimateArrive.model.info.TransportTaskArriveInfo;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;
import wjh.projects.domain.transportVehicle.model.entity.Site;
import wjh.projects.domain.transportVehicle.model.entity.TransportTask;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;
import wjh.projects.domain.transportVehicle.model.vo.SiteIdVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportTaskIdVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleIdVO;
import wjh.projects.domain.transportVehicle.repository.TransportVehicleRepository;
import wjh.projects.infrastructure.rpc.FacadeClient;

import java.util.*;

@Repository
public class TransportVehicleRepositoryImpl implements TransportVehicleRepository {
    private static final Logger logger = LoggerFactory.getLogger(TransportVehicleRepositoryImpl.class);
    private final FacadeClient facadeClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public TransportVehicleRepositoryImpl(FacadeClient facadeClient, KafkaTemplate<String, Object> kafkaTemplate) {
        this.facadeClient = facadeClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void save(TransportVehicle transportVehicle) {
    }

    @Override
    public TransportVehicle query(TransportVehicleIdVO transportVehicleIdVO) {
        return null;
    }

    @Override
    public void delete(TransportVehicleIdVO transportVehicleIdVO) {
    }


    @Override
    public void assignTransportTasks(TransportVehicle transportVehicle) {
        ArrayList<TransportTask> transportTasks = new ArrayList<>();
        LocationVO locationVO = transportVehicle.getTransportVehicleMessageVO().getLocationVO();
        String transportVehicleId = transportVehicle.getTransportVehicleIdVO().getTransportVehicleId();
        // TODO 查询车辆实时运输任务，此处用临时数据填充
        switch (transportVehicle.getTransportVehicleIdVO().getTransportVehicleId()) {
            case "京ADP283":
                Date planTime1 = DateUtil.stringToDate("Tue Mar 05 23:05:00 GMT+08:00 2024", "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                Date planTime2 = DateUtil.stringToDate("Tue Mar 05 21:30:25 GMT+08:00 2024", "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                Site route1 = new Site(
                        new SiteIdVO(1, facadeClient.geocode("江苏省南通市通州区先锋街道周圩十三组附近47米")),
                        planTime1,
                        false,
                        null);
                Site route2 = new Site(
                        new SiteIdVO(2, facadeClient.geocode("上海市青浦区华新镇财智领地南175米")),
                        planTime2,
                        false,
                        null);

                transportTasks.add(buildTransportTask(transportVehicleId, "C0A8016100002A9F00042A31FE5010B6", locationVO, route1));
                transportTasks.add(buildTransportTask(transportVehicleId, "C0A8016100002A9F000429F55ABEBF5B", locationVO, route2));
                break;
            case "浙A63A08":
                Date planTime3 = DateUtil.stringToDate("Wed Mar 06 05:01:01 GMT+08:00 2024", "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                Date planTime4 = DateUtil.stringToDate("Wed Mar 06 04:21:25 GMT+08:00 2024", "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                Site route3 = new Site(
                        new SiteIdVO(3, facadeClient.geocode("浙江省杭州市萧山区浦阳镇国祥副食南255米")),
                        planTime3,
                        false,
                        null);
                Site route4 = new Site(
                        new SiteIdVO(4, facadeClient.geocode("浙江省嘉兴市海宁市长安镇中通快递(杭州分拨店)")),
                        planTime4,
                        false,
                        null);
                transportTasks.add(buildTransportTask(transportVehicleId, "C0A8016100002A9F00042A77FC9C027F", locationVO, route3));
                transportTasks.add(buildTransportTask(transportVehicleId, "C0A8016100002A9F00042A6814CD0E88", locationVO, route4));
                break;
            case "浙A9Z027":
                Date planTime5 = DateUtil.stringToDate("Wed Mar 06 07:57:24 GMT+08:00 2024", "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                Date planTime6 = DateUtil.stringToDate("Wed Mar 06 07:55:03 GMT+08:00 2024", "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                Site route5 = new Site(
                        new SiteIdVO(5, facadeClient.geocode("浙江省金华市浦江县黄宅镇良清水果超市南372米")),
                        planTime5,
                        false,
                        null);
                Site route6 = new Site(
                        new SiteIdVO(6, facadeClient.geocode("浙江省杭州市萧山区新塘街道垚鲜森生鲜超市南61米")),
                        planTime6,
                        false,
                        null);
                transportTasks.add(buildTransportTask(transportVehicleId, "C0A801AC00002A9F00045E38C4EC283B", locationVO, route5));
                transportTasks.add(buildTransportTask(transportVehicleId, "C0A801AC00002A9F00045E24B7122ECB", locationVO, route6));
                break;
            default:
                break;
        }
        transportVehicle.setTransportTasks(transportTasks);
    }

    private TransportTask buildTransportTask(String transportVehicleId, String transportTaskId, LocationVO current, Site route) {
        return new TransportTask(
                new TransportTaskIdVO(transportTaskId),
                route,
                getMileages(transportVehicleId, transportTaskId, current, route));
    }

    /**
     * 获取当前位置到各个未到车运输站点的里程，单位：m
     */
    private Map<Site, Double> getMileages(String transportVehicleId, String transportTaskId, LocationVO current, Site route) {
        List<Site> sites = new ArrayList<>();
        for (Site site = route; site != null; site = site.getNext()) {
            // 如果运输站点是终点站，并且原本还未到车，但根据当前位置信息判断其到车，则发送运输任务到车信息
            if (site.isTerminus() && !site.getArrived() && site.isArrived(current))
                sendTransportTaskArriveInfo(transportVehicleId, transportTaskId);

            if (!site.getArrived())
                sites.add(site);
        }
        return facadeClient.calculateMileages(current, sites);
    }

    /**
     * 发送车辆运输任务到车信息
     */
    private void sendTransportTaskArriveInfo(String transportVehicleId, String transportTaskId) {
        logger.info("车辆运输任务到车，运输车辆：{}、运输任务：{}", transportVehicleId, transportTaskId);
        TransportTaskArriveInfo transportTaskArriveInfo = new TransportTaskArriveInfo(transportVehicleId, transportTaskId);
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
                KafkaTopicConst.TRANSPORT_TASK_ARRIVE_INFO,
                transportTaskArriveInfo);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                logger.warn("发送失败，车辆运输任务到车消息：{}、异常信息：{}",
                        JsonUtil.toJson(transportTaskArriveInfo),
                        ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                logger.info("发送成功，车辆运输任务到车消息：{}、发送结果：{}",
                        JsonUtil.toJson(transportTaskArriveInfo),
                        result.toString());
            }
        });
    }
}

package wjh.projects.infrastructure.repository;

import org.springframework.stereotype.Repository;
import wjh.projects.common.util.DateUtil;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;
import wjh.projects.domain.transportVehicle.model.entity.Site;
import wjh.projects.domain.transportVehicle.model.entity.TransportTask;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;
import wjh.projects.domain.transportVehicle.model.vo.SiteIdVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportTaskIdVO;
import wjh.projects.domain.transportVehicle.repository.TransportVehicleRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@Repository
public class TransportVehicleRepositoryImpl implements TransportVehicleRepository {

    @Override
    public void save(TransportVehicle transportVehicle) {
        // TODO 将运输车辆保存在数据库中
    }


    @Override
    public void assignTransportTasks(TransportVehicle transportVehicle) {
        ArrayList<TransportTask> transportTasks = new ArrayList<>();
        // TODO 查询车辆实时运输任务，此处用临时数据填充
        switch (transportVehicle.getId()) {
            case "京ADP283":
                Date planTime1 = DateUtil.stringToDate("Tue Mar 05 23:05:00 GMT+08:00 2024", "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                Date planTime2 = DateUtil.stringToDate("Tue Mar 05 21:30:25 GMT+08:00 2024", "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                Site route1 = new Site(new SiteIdVO(1), planTime1, new LocationVO("江苏省南通市通州区先锋街道周圩十三组附近47米"));
                Site route2 = new Site(new SiteIdVO(2), planTime2, new LocationVO("上海市青浦区华新镇财智领地南175米"));

                transportTasks.add(new TransportTask(new TransportTaskIdVO("C0A8016100002A9F00042A31FE5010B6"), route1));
                transportTasks.add(new TransportTask(new TransportTaskIdVO("C0A8016100002A9F000429F55ABEBF5B"), route2));
                break;
            case "浙A63A08":
                Date planTime3 = DateUtil.stringToDate("Wed Mar 06 05:01:01 GMT+08:00 2024", "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                Date planTime4 = DateUtil.stringToDate("Wed Mar 06 04:21:25 GMT+08:00 2024", "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                Site route3 = new Site(new SiteIdVO(3), planTime3, new LocationVO("浙江省杭州市萧山区浦阳镇国祥副食南255米"));
                Site route4 = new Site(new SiteIdVO(4), planTime4, new LocationVO("浙江省嘉兴市海宁市长安镇中通快递(杭州分拨店)"));

                transportTasks.add(new TransportTask(new TransportTaskIdVO("C0A8016100002A9F00042A77FC9C027F"), route3));
                transportTasks.add(new TransportTask(new TransportTaskIdVO("C0A8016100002A9F00042A6814CD0E88"), route4));
                break;
            case "浙A9Z027":
                Date planTime5 = DateUtil.stringToDate("Wed Mar 06 07:57:24 GMT+08:00 2024", "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                Date planTime6 = DateUtil.stringToDate("Wed Mar 06 07:55:03 GMT+08:00 2024", "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                Site route5 = new Site(new SiteIdVO(5), planTime5, new LocationVO("浙江省金华市浦江县黄宅镇良清水果超市南372米"));
                Site route6 = new Site(new SiteIdVO(6), planTime6, new LocationVO("浙江省杭州市萧山区新塘街道垚鲜森生鲜超市南61米"));

                transportTasks.add(new TransportTask(new TransportTaskIdVO("C0A801AC00002A9F00045E38C4EC283B"), route5));
                transportTasks.add(new TransportTask(new TransportTaskIdVO("C0A801AC00002A9F00045E24B7122ECB"), route6));
                break;
            default:
                break;
        }
        transportVehicle.setTransportTasks(transportTasks);
    }
}

package wjh.projects.interfaces.converter;

import wjh.projects.domain.transportVehicle.model.info.TransportVehicleInfo;
import wjh.projects.facade.estimateArrive.request.EstimateRequest;

public class EstimateRequestConverter {

    public static TransportVehicleInfo toTransportVehicleInfo(EstimateRequest estimateRequest) {
        TransportVehicleInfo transportVehicleInfo = new TransportVehicleInfo();

        transportVehicleInfo.setCarCode(estimateRequest.getCarCode());
        transportVehicleInfo.setLongitude(estimateRequest.getLongitude());
        transportVehicleInfo.setLatitude(estimateRequest.getLatitude());
        transportVehicleInfo.setGpsTime(estimateRequest.getGpsTime());
        transportVehicleInfo.setCreateTime(estimateRequest.getCreateTime());
        transportVehicleInfo.setSpeed(estimateRequest.getSpeed());
        transportVehicleInfo.setAddress(estimateRequest.getAddress());

        return transportVehicleInfo;
    }
}

package wjh.projects.domain.transportVehicle.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wjh.projects.domain.base.Identifier;

@Getter
@AllArgsConstructor
public class SiteIdVO implements Identifier {
    private int id;

    private SiteIdVO() {
    }
}

package com.pig4cloud.pig.capi.dto.request.RebotCallbackParames;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RobotMaintenanceInfo {

    /**
     * 里程数
     */
    private String milage;

    /**
     * 维修时间
     */
    private String repair_date;

    /**
     * 维修内容
     */
    private String repair_labors;

    /**
     * 维修部件
     */
    private String repair_parts;

    /**
     * 维修类型
     */
    private String repair_type;

    /**
     *
     */
    private List<RobotLabors> labors;

    /**
     *
     */
    private List<RobotParts> parts;

    /**
     *
     */
    private String describe;

}
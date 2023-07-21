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
	 * 车架号
	 */
	private String vin;

	/**
	 * 维修类型
	 */
	private String repairType;

	/**
	 * 配件，List的 json 字符串
	 */
	private String material;

	/**
	 * 维修开始时间戳（毫秒）
	 */
	private Long repairBeginDate;

	/**
	 * 维修结束时间戳（毫秒）
	 */
	private Long repairFinishDate;

    /**
     * 里程数
     */
    private Integer mileage;

    /**
     * 故障描述
     */
    private String troubleDescription;

    /**
     * 故障原因
     */
    private String troubleReason;

    /**
     * 	项目列表
     */
    private List<RobotLabors> recordItems;

    /**
     * 配件列表
     */
    private List<RobotParts> laborItems;

    /**
     * 维修项目名称描述
     */
    private String laborStr;

	/**
	 * 配件名称描述
	 */
    private String partStr;

}
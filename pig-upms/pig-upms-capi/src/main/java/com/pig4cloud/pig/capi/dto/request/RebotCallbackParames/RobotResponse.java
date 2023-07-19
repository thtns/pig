package com.pig4cloud.pig.capi.dto.request.RebotCallbackParames;

import lombok.Data;

import java.util.List;

@Data
public class RobotResponse {

    private String vin;

	/**
	 * 品牌厂商
	 */
	private String brand;

	/**
	 * 车系名称
	 */
	private String seriesName;

	/**
     * 车型
     */
    private String modelName;

    /**
     * 维修详情
     */
    private List<RobotMaintenanceInfo> repairRecords;




}

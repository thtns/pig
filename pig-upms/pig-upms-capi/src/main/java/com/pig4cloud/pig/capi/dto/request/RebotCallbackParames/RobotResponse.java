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
     * 车型
     */
    private String model;
    /**
     * 里程数
     */
    private String total_milage;
    /**
     * 记录最后时间
     */
    private String last_date;

    /**
     * 维修详情
     */
    private List<RobotMaintenanceInfo> details;




}

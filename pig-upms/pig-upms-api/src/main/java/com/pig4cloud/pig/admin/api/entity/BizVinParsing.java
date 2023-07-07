package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pig4cloud.pig.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("biz_vin_parsing")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "vin解析")
public class BizVinParsing extends BaseEntity {

	/**
	 * 主键id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description ="主键id")
	private Long id;

	/**
	 * vin编码
	 */
	private java.lang.String vinCode;

	/**
	 * 品牌名称（例：马自达、大众）
	 */
	private java.lang.String brand;

	/**
	 * 厂商名称（例：长安马自达、一汽大众）
	 */
	private java.lang.String subBrand;

	/**
	 * 关联car_brand_wmi中的doctor_brand
	 */
	private java.lang.String doctorBrand;

	/**
	 * 三方查询结果
	 */
	private java.lang.String content;
}

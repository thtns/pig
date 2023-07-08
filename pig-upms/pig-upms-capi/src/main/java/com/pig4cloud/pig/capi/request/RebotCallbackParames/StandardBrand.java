package com.pig4cloud.pig.capi.request.RebotCallbackParames;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardBrand {

    /**
     * 厂商名称
     */
    private String sub_brand;
    /**
     * 品牌code
     */
    private String brand_code;

    /**
     * 品牌名称
     */
    private String brand;
}
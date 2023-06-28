/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pig.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.BizCarBrand;
import com.pig4cloud.pig.admin.api.request.AddBizCarBrandSupplierRequest;
import com.pig4cloud.pig.admin.api.request.AddCarBrandRequest;
import com.pig4cloud.pig.admin.service.BizCarBrandService;
import com.pig4cloud.pig.admin.service.BizCarBrandSupplierService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;

import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


/**
 * 汽车品牌
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/bizcarbrand" )
@Tag(name = "汽车品牌管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class BizCarBrandController {

    private final BizCarBrandService bizCarBrandService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param bizCarBrand 汽车品牌
     * @return
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('admin_bizcarbrand_get')" )
    public R getBizCarBrandPage(Page page, BizCarBrand bizCarBrand) {
        return R.ok(bizCarBrandService.page(page, Wrappers.query(bizCarBrand)));
    }


    /**
     * 通过id查询汽车品牌
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询", description = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_bizcarbrand_get')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(bizCarBrandService.getById(id));
    }

    /**
     * 新增汽车品牌
     * @param request 汽车品牌
     * @return R
     */
    @Operation(summary = "新增汽车品牌", description = "新增汽车品牌")
    @SysLog("新增汽车品牌" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin_bizcarbrand_add')" )
    public R<Void> save(@RequestBody AddCarBrandRequest request) {
		bizCarBrandService.add(request);
        return R.ok();
    }


    /**
     * 修改汽车品牌
     * @param bizCarBrand 汽车品牌
     * @return R
     */
    @Operation(summary = "修改汽车品牌", description = "修改汽车品牌")
    @SysLog("修改汽车品牌" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_bizcarbrand_edit')" )
    public R updateById(@RequestBody BizCarBrand bizCarBrand) {
        return R.ok(bizCarBrandService.updateById(bizCarBrand));
    }

    /**
     * 通过id删除汽车品牌
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id删除汽车品牌", description = "通过id删除汽车品牌")
    @SysLog("通过id删除汽车品牌" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_bizcarbrand_del')" )
    public R removeById(@PathVariable Long id) {
        return R.ok(bizCarBrandService.removeById(id));
    }

}

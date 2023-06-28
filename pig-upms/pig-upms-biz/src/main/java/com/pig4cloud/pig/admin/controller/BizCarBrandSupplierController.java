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
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.admin.api.entity.BizCarBrandSupplier;
import com.pig4cloud.pig.admin.service.BizCarBrandSupplierService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


/**
 * 品牌供应商关系表
 *
 * @author pig code generator
 * @date 2023-06-27 10:29:29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/bizcarbrandsupplier" )
@Tag(name = "品牌供应商关系表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class BizCarBrandSupplierController {

    private final  BizCarBrandSupplierService bizCarBrandSupplierService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param bizCarBrandSupplier 品牌供应商关系表
     * @return
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('admin_bizcarbrandsupplier_get')" )
    public R getBizCarBrandSupplierPage(Page page, BizCarBrandSupplier bizCarBrandSupplier) {
        return R.ok(bizCarBrandSupplierService.page(page, Wrappers.query(bizCarBrandSupplier)));
    }


    /**
     * 通过id查询品牌供应商关系表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询", description = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_bizcarbrandsupplier_get')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(bizCarBrandSupplierService.getById(id));
    }

    /**
     * 新增品牌供应商关系表
     * @param bizCarBrandSupplier 品牌供应商关系表
     * @return R
     */
    @Operation(summary = "新增品牌供应商关系表", description = "新增品牌供应商关系表")
    @SysLog("新增品牌供应商关系表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin_bizcarbrandsupplier_add')" )
    public R save(@RequestBody BizCarBrandSupplier bizCarBrandSupplier) {
        return R.ok(bizCarBrandSupplierService.save(bizCarBrandSupplier));
    }

    /**
     * 修改品牌供应商关系表
     * @param bizCarBrandSupplier 品牌供应商关系表
     * @return R
     */
    @Operation(summary = "修改品牌供应商关系表", description = "修改品牌供应商关系表")
    @SysLog("修改品牌供应商关系表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_bizcarbrandsupplier_edit')" )
    public R updateById(@RequestBody BizCarBrandSupplier bizCarBrandSupplier) {
        return R.ok(bizCarBrandSupplierService.updateById(bizCarBrandSupplier));
    }

    /**
     * 通过id删除品牌供应商关系表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id删除品牌供应商关系表", description = "通过id删除品牌供应商关系表")
    @SysLog("通过id删除品牌供应商关系表" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_bizcarbrandsupplier_del')" )
    public R removeById(@PathVariable Long id) {
        return R.ok(bizCarBrandSupplierService.removeById(id));
    }

}

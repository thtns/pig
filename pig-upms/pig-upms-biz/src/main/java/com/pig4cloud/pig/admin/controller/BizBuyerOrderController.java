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
import com.pig4cloud.pig.admin.api.entity.BizBuyerOrder;
import com.pig4cloud.pig.admin.service.BizBuyerOrderService;
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
 * 采购商订单表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/bizbuyerorder" )
@Tag(name = "采购商订单表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class BizBuyerOrderController {

    private final BizBuyerOrderService bizBuyerOrderService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param bizBuyerOrder 采购商订单表
     * @return
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('demo_bizbuyerorder_get')" )
    public R getBizBuyerOrderPage(Page page, BizBuyerOrder bizBuyerOrder) {
        return R.ok(bizBuyerOrderService.page(page, Wrappers.query(bizBuyerOrder)));
    }


    /**
     * 通过id查询采购商订单表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询", description = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('demo_bizbuyerorder_get')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(bizBuyerOrderService.getById(id));
    }

    /**
     * 新增采购商订单表
     * @param bizBuyerOrder 采购商订单表
     * @return R
     */
    @Operation(summary = "新增采购商订单表", description = "新增采购商订单表")
    @SysLog("新增采购商订单表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('demo_bizbuyerorder_add')" )
    public R save(@RequestBody BizBuyerOrder bizBuyerOrder) {
        return R.ok(bizBuyerOrderService.save(bizBuyerOrder));
    }

    /**
     * 修改采购商订单表
     * @param bizBuyerOrder 采购商订单表
     * @return R
     */
    @Operation(summary = "修改采购商订单表", description = "修改采购商订单表")
    @SysLog("修改采购商订单表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('demo_bizbuyerorder_edit')" )
    public R updateById(@RequestBody BizBuyerOrder bizBuyerOrder) {
        return R.ok(bizBuyerOrderService.updateById(bizBuyerOrder));
    }

    /**
     * 通过id删除采购商订单表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id删除采购商订单表", description = "通过id删除采购商订单表")
    @SysLog("通过id删除采购商订单表" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('demo_bizbuyerorder_del')" )
    public R removeById(@PathVariable Long id) {
        return R.ok(bizBuyerOrderService.removeById(id));
    }

}

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
import com.pig4cloud.pig.admin.api.entity.BizRobotQueryRecord;
import com.pig4cloud.pig.admin.service.BizRobotQueryRecordService;
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
 * 机器人查询记录表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/bizrobotqueryrecord" )
@Tag(name = "机器人查询记录表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class BizRobotQueryRecordController {

    private final BizRobotQueryRecordService bizRobotQueryRecordService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param bizRobotQueryRecord 机器人查询记录表
     * @return
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('admin_bizrobotqueryrecord_get')" )
    public R getBizRobotQueryRecordPage(Page page, BizRobotQueryRecord bizRobotQueryRecord) {
        return R.ok(bizRobotQueryRecordService.page(page, Wrappers.query(bizRobotQueryRecord)));
    }


    /**
     * 通过id查询机器人查询记录表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询", description = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_bizrobotqueryrecord_get')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(bizRobotQueryRecordService.getById(id));
    }

    /**
     * 新增机器人查询记录表
     * @param bizRobotQueryRecord 机器人查询记录表
     * @return R
     */
    @Operation(summary = "新增机器人查询记录表", description = "新增机器人查询记录表")
    @SysLog("新增机器人查询记录表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin_bizrobotqueryrecord_add')" )
    public R save(@RequestBody BizRobotQueryRecord bizRobotQueryRecord) {
        return R.ok(bizRobotQueryRecordService.save(bizRobotQueryRecord));
    }

    /**
     * 修改机器人查询记录表
     * @param bizRobotQueryRecord 机器人查询记录表
     * @return R
     */
    @Operation(summary = "修改机器人查询记录表", description = "修改机器人查询记录表")
    @SysLog("修改机器人查询记录表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_bizrobotqueryrecord_edit')" )
    public R updateById(@RequestBody BizRobotQueryRecord bizRobotQueryRecord) {
        return R.ok(bizRobotQueryRecordService.updateById(bizRobotQueryRecord));
    }

    /**
     * 通过id删除机器人查询记录表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id删除机器人查询记录表", description = "通过id删除机器人查询记录表")
    @SysLog("通过id删除机器人查询记录表" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_bizrobotqueryrecord_del')" )
    public R removeById(@PathVariable Long id) {
        return R.ok(bizRobotQueryRecordService.removeById(id));
    }

}

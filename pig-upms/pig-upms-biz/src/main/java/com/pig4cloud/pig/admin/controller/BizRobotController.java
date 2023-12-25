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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.BizRobot;
import com.pig4cloud.pig.admin.api.entity.BizSupplier;
import com.pig4cloud.pig.admin.api.request.AddRobotRequest;
import com.pig4cloud.pig.admin.service.BizRobotService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;

import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 * 机器人
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/bizrobot" )
@Tag(name = "机器人管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class BizRobotController {

    private final BizRobotService bizRobotService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param bizRobot 机器人
     * @return
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('admin_bizrobot_get')" )
    public R getBizRobotPage(Page page, BizRobot bizRobot) {
		QueryWrapper<BizRobot> queryWrapper = new QueryWrapper<>();
		// 构建查询条件
		if (bizRobot.getRobotProxiesName() != null) {
			queryWrapper.like("robot_proxies_name", bizRobot.getRobotProxiesName() );
		}
		if (bizRobot.getStatus() != null) {
			queryWrapper.eq("status", bizRobot.getStatus());
		}
		queryWrapper.orderByAsc(Arrays.asList("robot_proxies_name"));
        return R.ok(bizRobotService.page(page, queryWrapper));
    }


    /**
     * 通过id查询机器人
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询", description = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_bizrobot_get')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(bizRobotService.getById(id));
    }

    /**
     * 新增机器人
     * @param request 机器人
     * @return R
     */
    @Operation(summary = "新增机器人", description = "新增机器人")
    @SysLog("新增机器人" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin_bizrobot_add')" )
    public R<Void> save(@RequestBody AddRobotRequest request) {
		bizRobotService.add(request);
        return R.ok();
    }

    /**
     * 修改机器人
     * @param bizRobot 机器人
     * @return R
     */
    @Operation(summary = "修改机器人", description = "修改机器人")
    @SysLog("修改机器人" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_bizrobot_edit')" )
    public R updateById(@RequestBody BizRobot bizRobot) {
        return R.ok(bizRobotService.updateById(bizRobot));
    }

    /**
     * 通过id删除机器人
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id删除机器人", description = "通过id删除机器人")
    @SysLog("通过id删除机器人" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_bizrobot_del')" )
    public R removeById(@PathVariable Long id) {
        return R.ok(bizRobotService.removeById(id));
    }


	@Operation(summary = "全量查询", description = "全量查询")
	@GetMapping("/all" )
	@PreAuthorize("@pms.hasPermission('admin_bizrobot_get')" )
	public R getBizRobotAll() {
		return R.ok(bizRobotService.list());
	}
}

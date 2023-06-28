package com.pig4cloud.pig.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.admin.api.entity.BizRobotSupplier;
import com.pig4cloud.pig.admin.service.BizRobotSupplierService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


/**
 * 机器人供应商关系表
 *
 * @author pig code generator
 * @date 2023-06-27 14:00:16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/bizrobotsupplier" )
@Tag(name = "机器人供应商关系表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class BizRobotSupplierController {

	private final  BizRobotSupplierService bizRobotSupplierService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param bizRobotSupplier 机器人供应商关系表
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page" )
	@PreAuthorize("@pms.hasPermission('admin_bizrobotsupplier_get')" )
	public R getBizRobotSupplierPage(Page page, BizRobotSupplier bizRobotSupplier) {
		return R.ok(bizRobotSupplierService.page(page, Wrappers.query(bizRobotSupplier)));
	}


	/**
	 * 通过id查询机器人供应商关系表
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}" )
	@PreAuthorize("@pms.hasPermission('admin_bizrobotsupplier_get')" )
	public R getById(@PathVariable("id" ) Long id) {
		return R.ok(bizRobotSupplierService.getById(id));
	}

	/**
	 * 新增机器人供应商关系表
	 * @param bizRobotSupplier 机器人供应商关系表
	 * @return R
	 */
	@Operation(summary = "新增机器人供应商关系表", description = "新增机器人供应商关系表")
	@SysLog("新增机器人供应商关系表" )
	@PostMapping
	@PreAuthorize("@pms.hasPermission('admin_bizrobotsupplier_add')" )
	public R save(@RequestBody BizRobotSupplier bizRobotSupplier) {
		return R.ok(bizRobotSupplierService.save(bizRobotSupplier));
	}

	/**
	 * 修改机器人供应商关系表
	 * @param bizRobotSupplier 机器人供应商关系表
	 * @return R
	 */
	@Operation(summary = "修改机器人供应商关系表", description = "修改机器人供应商关系表")
	@SysLog("修改机器人供应商关系表" )
	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin_bizrobotsupplier_edit')" )
	public R updateById(@RequestBody BizRobotSupplier bizRobotSupplier) {
		return R.ok(bizRobotSupplierService.updateById(bizRobotSupplier));
	}

	/**
	 * 通过id删除机器人供应商关系表
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除机器人供应商关系表", description = "通过id删除机器人供应商关系表")
	@SysLog("通过id删除机器人供应商关系表" )
	@DeleteMapping("/{id}" )
	@PreAuthorize("@pms.hasPermission('admin_bizrobotsupplier_del')" )
	public R removeById(@PathVariable Long id) {
		return R.ok(bizRobotSupplierService.removeById(id));
	}

}

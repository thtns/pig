package com.pig4cloud.pig.capi.controller;

import com.alibaba.fastjson.JSON;
import com.pig4cloud.pig.capi.service.BizSupplierService;
import com.pig4cloud.pig.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class test {

	private final BizSupplierService bizSupplierService;

	@GetMapping(value = {"/test"})
	public String test(HttpServletRequest request) {
		return "你看得到我么？";
	}

	@PostMapping(value = {"/test2"})
	public String test2(HttpServletRequest request, @RequestBody String requestBody) {
		log.info("callback 再回调给商家的数据是：{}", JSON.toJSONString(requestBody));
		return "你看得到我么？ 数据是：\n {}" + JSON.toJSONString(requestBody);
	}

	@GetMapping(value = {"/test3"})
	public R test3(HttpServletRequest request) {
		return R.ok();
	}

	@GetMapping(value = {"/test4"})
	public R test4(HttpServletRequest request) {
		bizSupplierService.addSupplierCount(1695281566665580545l);
		return R.ok();
	}
}

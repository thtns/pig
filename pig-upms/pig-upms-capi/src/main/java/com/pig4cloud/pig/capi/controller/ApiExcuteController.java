package com.pig4cloud.pig.capi.controller;

import com.pig4cloud.pig.capi.entity.BizSupplier;
import com.pig4cloud.pig.capi.service.BizSupplierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "接口业务")
public class ApiExcuteController {

	private final BizSupplierService bizSupplierService;

	@RequestMapping(value = {"/changeStatus"}, method = RequestMethod.GET)
	public String changeStatus(Long sid, Integer status) {
		BizSupplier bizSupplier = new BizSupplier();
		bizSupplier.setId(sid);
		bizSupplier.setStatus(status);
		bizSupplierService.updateById(bizSupplier);
		return "请求成功！";
	}

}

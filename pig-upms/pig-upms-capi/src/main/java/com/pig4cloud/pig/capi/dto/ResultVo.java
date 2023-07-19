/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.capi.dto;

import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author Gavin.Q
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResultVo<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String code = CommonConstants.API_SUCCESS;

	@Getter
	@Setter
	private String message;

	@Getter
	@Setter
	private T data;

	public static <T> ResultVo<T> success() { return restResult(null, CommonConstants.API_SUCCESS, "操作成功"); }

	public static <T> ResultVo<T> success(T data) {
		return restResult(data, CommonConstants.API_SUCCESS, "操作成功");
	}

	public static <T> ResultVo<T> success(T data, String msg) { return restResult(data, CommonConstants.API_SUCCESS, msg); }

	public static <T> ResultVo<T> failed() {
		return restResult(null, CommonConstants.API_FAIL, "暂不支持");
	}

	public static <T> ResultVo<T> failed(String msg) {
		return restResult(null, CommonConstants.API_FAIL, msg);
	}

	public static <T> ResultVo<T> failed(T data) {
		return restResult(data, CommonConstants.API_FAIL, null);
	}

	public static <T> ResultVo<T> failed(T data, String msg) {
		return restResult(data, CommonConstants.API_FAIL, msg);
	}

	public static <T> ResultVo<T> result(T data, String code, String msg) {
		return restResult(data, code, msg);
	}

	public static <T> ResultVo<T> restResult(T data, String code, String msg) {
		ResultVo<T> apiResult = new ResultVo<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMessage(msg);
		return apiResult;
	}

	public Boolean isSuccess() {
		return (this.code.equals(CommonConstants.API_SUCCESS));
	}

}

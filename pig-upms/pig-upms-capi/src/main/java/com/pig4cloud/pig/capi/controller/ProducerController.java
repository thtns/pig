package com.pig4cloud.pig.capi.controller;

import com.pig4cloud.pig.capi.utils.rocketmq.ProducerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

	@Autowired
	ProducerUtil producerUtil;

	/**
	 * rocketmq demo
	 */
	@RequestMapping(value = {"/upOrder"}, method = RequestMethod.GET)
	public String upOrder(String orderId) {
		producerUtil.sendMsg(orderId);
//		producerUtil.sendTimeMsg("1573858210", System.currentTimeMillis() + 30 * 1000);
		return "请求成功！";
	}

	@RequestMapping(value = {"/timeTest"}, method = RequestMethod.GET)
	public String timeTest(String orderId) {
		producerUtil.sendTimeMsg(orderId, System.currentTimeMillis() + 3 * 1000);
		return "请求成功！";
	}


    /**
     * rocketmq demo
     */
    @RequestMapping(value = {"/useRocketMQ"}, method = RequestMethod.GET)
    public String useRocketMQ() {

		producerUtil.sendMsg("我是来测试的");
		producerUtil.sendTimeMsg("1573858210", System.currentTimeMillis() + 30 * 1000);
        return "请求成功！";
    }
}
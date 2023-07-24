package com.pig4cloud.pig.common.core.util.mq;


import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Admin
 */

@Slf4j
@Component
public class ProducerUtil {

    @Autowired
    private ProducerBean producer;

	@Autowired
	private MqConfig mqConfig;

    /**
     * 同步发送消息
     *
     * @param message 消息body内容，生产者自定义内容
     * @return success:SendResult or error:null
     */
    public SendResult sendMsg(String message) {
        Message msg = new Message(mqConfig.getTopic(), mqConfig.getTag(), message.getBytes());
        return send(msg, Boolean.FALSE);
    }

	public SendResult sendEasyMsg(String message, String topic, String tag) {
		Message msg = new Message(topic, tag, message.getBytes());
		return send(msg, Boolean.FALSE);
	}

	public SendResult sendTestMsg(String message) {
		Message msg = new Message(mqConfig.getTestTopic(), mqConfig.getTestTag(), message.getBytes());
		return send(msg, Boolean.FALSE);
	}

    /**
     * 同步发送单向消息
     * @param message 消息body内容，生产者自定义内容
     */
    public void sendOneWayMsg(String message) {
        Message msg = new Message(mqConfig.getTopic(), mqConfig.getTag(), message.getBytes());
        send(msg, Boolean.TRUE);
    }

    /**
     * 同步发送定时/延时消息
     * @param message 	消息body内容
     * @param delayTime   服务端发送消息时间，立即发送输入0或比更早的时间
     * @return success:SendResult or error:null
     */
    public SendResult sendTimeMsg(String message, long delayTime) {
        Message msg = new Message(mqConfig.getTimeTopic(), mqConfig.getTimeTag(), message.getBytes());
        msg.setStartDeliverTime(delayTime);
        return send(msg, Boolean.FALSE);
    }

    /**
     * 发送普通消息
     *
     * @param msg      消息
     * @param isOneWay 是否单向发送
     */
    private SendResult send(Message msg, Boolean isOneWay) {
        try {
            if (isOneWay) {
                //由于在 oneway 方式发送消息时没有请求应答处理，一旦出现消息发送失败，则会因为没有重试而导致数据丢失。
                //若数据不可丢，建议选用同步或异步发送方式。
                producer.sendOneway(msg);
                success(msg, "单向消息MsgId不返回");
                return null;
            } else {
                //可靠同步发送
                SendResult sendResult = producer.send(msg);
                //获取发送结果，不抛异常即发送成功
                assert sendResult != null;
                success(msg, sendResult.getMessageId());
                return sendResult;
            }
        } catch (Exception e) {
            error(msg, e);
            return null;
        }
    }

    /**
     * 成功日志打印
     *
     * @param msg
     * @param messageId
     */
    private void success(Message msg, String messageId) {
        log.info("发送MQ消息成功 -- Topic:{} ,msgId:{} , Key:{}, tag:{}, body:{}"
                , msg.getTopic(), messageId, msg.getKey(), msg.getTag(), new String(msg.getBody()));
    }

    /**
     * 异常日志打印
     *
     * @param msg
     * @param e
     */
    private void error(Message msg, Exception e) {
        log.error("发送MQ消息失败-- Topic:{}, Key:{}, tag:{}, body:{}"
                , msg.getTopic(), msg.getKey(), msg.getTag(), new String(msg.getBody()));
        log.error("errorMsg", e);
    }
}

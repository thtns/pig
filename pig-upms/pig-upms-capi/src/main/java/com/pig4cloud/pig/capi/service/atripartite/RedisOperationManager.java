package com.pig4cloud.pig.capi.service.atripartite;

import com.pig4cloud.pig.capi.service.apo.RedisKeyDefine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * @author Admin
 * @date 2020-09-07 14:09
 */
@Service
@Slf4j
public class RedisOperationManager {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 2次限制
     */
    private static final Integer COUNT = 3;


    /**
     * 验证redis中supplier是否有2次未知错误
     * 30秒内两次未知错误下线
     * @param supplierId 供应商ID
     * @return 供应商未知错误数据
     */
    public Boolean redisOperationUnknownErrorKey(Long supplierId) {
        String unknownErrorCount = redisTemplate.opsForValue().get(RedisKeyDefine.UNKNOWN_ERROR_KEY + supplierId);
        if (StringUtils.isNotBlank(unknownErrorCount)) {
            if (Integer.parseInt(unknownErrorCount) < COUNT) {
				redisTemplate.boundValueOps(RedisKeyDefine.UNKNOWN_ERROR_KEY + supplierId).increment(1);
            } else {
                return true;
            }
        } else {
			redisTemplate.opsForValue().set(RedisKeyDefine.UNKNOWN_ERROR_KEY + supplierId, "1", 30, TimeUnit.SECONDS);
        }
        return false;
    }


    /**
     * 获取特定时间的 毫秒数
     */
    public static Long getRemainingTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

}

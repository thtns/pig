package com.pig4cloud.pig.common.core.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
class Retry {
	private static final long MAX_SLEEP_MILLISECOND = 256 * 1000;

	public <T> T doRetry(Callable<T> callable, int retryTimes, long sleepTimeInMilliSecond, boolean exponential, List<Class<?>> retryExceptionClasss)
			throws Exception {

		// 参数校验
		if (null == callable) {
			throw new IllegalArgumentException("系统编程错误, 入参callable不能为空 ! ");
		}

		if (retryTimes < 1) {
			throw new IllegalArgumentException(String.format(
					"系统编程错误, 入参retrytime[%d]不能小于1 !", retryTimes));
		}

		Exception saveException = null;
		for (int i = 0; i < retryTimes; i++) {
			try {
				// 执行方法调用
				return call(callable);
			} catch (Exception e) {
				saveException = e;
				if (i == 0) {
					log.error(String.format("Exception when calling callable, 异常Msg:%s", saveException.getMessage()), saveException);
				}

				// 如果指定了对应的异常类，则判断当前e是否就是对应的异常类，是的话则执行重试，否则直接抛出异常
				if (null != retryExceptionClasss && !retryExceptionClasss.isEmpty()) {
					boolean needRetry = false;
					for (Class<?> eachExceptionClass : retryExceptionClasss) {
						if (eachExceptionClass == e.getClass()) {
							needRetry = true;
							break;
						}
					}
					if (!needRetry) {
						throw saveException;
					}
				}

				// 不超过最大重试次数则执行后续重试逻辑
				if (i + 1 < retryTimes && sleepTimeInMilliSecond > 0) {
					long startTime = System.currentTimeMillis();

					// 根据入参决定休息多久之后执行重试逻辑，最大不超过MAX_SLEEP_MILLISECOND
					long timeToSleep;
					if (exponential) {
						timeToSleep = sleepTimeInMilliSecond * (long) Math.pow(2, i);
						if (timeToSleep >= MAX_SLEEP_MILLISECOND) {
							timeToSleep = MAX_SLEEP_MILLISECOND;
						}
					} else {
						timeToSleep = sleepTimeInMilliSecond;
						if (timeToSleep >= MAX_SLEEP_MILLISECOND) {
							timeToSleep = MAX_SLEEP_MILLISECOND;
						}
					}

					try {
						// 当前执行线程沉睡
						Thread.sleep(timeToSleep);
					} catch (InterruptedException ignored) {
					}

					long realTimeSleep = System.currentTimeMillis() - startTime;

					log.error(String.format("Exception when calling callable, 即将尝试执行第%s次重试.本次重试计划等待[%s]ms,实际等待[%s]ms, 异常Msg:[%s]",
							i + 1, timeToSleep, realTimeSleep, e.getMessage()));

				}
			}
		}
		throw saveException;
	}

	protected <T> T call(Callable<T> callable) throws Exception {
		return callable.call();
	}
}

@Slf4j
class RetryAsync extends Retry {

	private long timeoutMs;
	private ThreadPoolExecutor executor;

	public RetryAsync(long timeoutMs, ThreadPoolExecutor executor) {
		this.timeoutMs = timeoutMs;
		this.executor = executor;
	}

	/**
	 * 使用传入的线程池异步执行任务，并且等待。
	 * <p/>
	 * future.get()方法，等待指定的毫秒数。如果任务在超时时间内结束，则正常返回。
	 * 如果抛异常（可能是执行超时、执行异常、被其他线程cancel或interrupt），都记录日志并且网上抛异常。
	 * 正常和非正常的情况都会判断任务是否结束，如果没有结束，则cancel任务。cancel参数为true，表示即使
	 * 任务正在执行，也会interrupt线程。
	 *
	 * @param callable
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	@Override
	protected <T> T call(Callable<T> callable) throws Exception {
		// 重写后的call()方法，在执行重试时，直接交由Executor执行，避免了当前线程被阻塞
		Future<T> future = executor.submit(callable);
		try {
			// 在规定时间内获取执行结果
			return future.get(timeoutMs, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			// 规定时间内没有获取到结果则直接抛出异常
			log.warn("Try once failed", e);
			throw e;
		} finally {
			if (!future.isDone()) {
				future.cancel(true);
				log.warn("Try once task not done, cancel it, active count: " + executor.getActiveCount());
			}
		}
	}
}

@UtilityClass
public final class RetryUtil {
 

    private static final long MAX_SLEEP_MILLISECOND = 256 * 1000;
    /**
     * 重试次数工具方法.
     *
     * @param callable               实际逻辑
     * @param retryTimes             最大重试次数（>1）
     * @param sleepTimeInMilliSecond 运行失败后休眠对应时间再重试
     * @param exponential            休眠时间是否指数递增
     * @param <T>                    返回值类型
     * @return 经过重试的callable的执行结果
     */
    public static <T> T executeWithRetry(Callable<T> callable,
										 int retryTimes,
										 long sleepTimeInMilliSecond,
										 boolean exponential) throws Exception {
        Retry retry = new Retry();
        return retry.doRetry(callable, retryTimes, sleepTimeInMilliSecond, exponential, null);
    }
    
    /**
     * 重试次数工具方法.
     *
     * @param callable               实际逻辑
     * @param retryTimes             最大重试次数（>1）
     * @param sleepTimeInMilliSecond 运行失败后休眠对应时间再重试
     * @param exponential            休眠时间是否指数递增
     * @param <T>                    返回值类型
     * @param retryExceptionClasss   出现指定的异常类型时才进行重试
     * @return 经过重试的callable的执行结果
     */
    public static <T> T executeWithRetry(Callable<T> callable,
                                         int retryTimes,
                                         long sleepTimeInMilliSecond,
                                         boolean exponential,
                                         List<Class<?>> retryExceptionClasss) throws Exception {
        Retry retry = new Retry();
        return retry.doRetry(callable, retryTimes, sleepTimeInMilliSecond, exponential, retryExceptionClasss);
    }
 
    /**
     * 在外部线程执行并且重试。每次执行需要在timeoutMs内执行完，不然视为失败。
     * 执行异步操作的线程池从外部传入，线程池的共享粒度由外部控制。比如，HttpClientUtil共享一个线程池。
     * <p/>
     * 限制条件：仅仅能够在阻塞的时候interrupt线程
     *
     * @param callable               实际逻辑
     * @param retryTimes             最大重试次数（>1）
     * @param sleepTimeInMilliSecond 运行失败后休眠对应时间再重试
     * @param exponential            休眠时间是否指数递增
     * @param timeoutMs              callable执行超时时间，毫秒
     * @param executor               执行异步操作的线程池
     * @param <T>                    返回值类型
     * @return 经过重试的callable的执行结果
     */
    public static <T> T asyncExecuteWithRetry(Callable<T> callable,
                                              int retryTimes,
                                              long sleepTimeInMilliSecond,
                                              boolean exponential,
                                              long timeoutMs,
                                              ThreadPoolExecutor executor) throws Exception {
		if (executor == null){
			executor = createThreadPoolExecutor();
		}
        Retry retry = new RetryAsync(timeoutMs, executor);
        return retry.doRetry(callable, retryTimes, sleepTimeInMilliSecond, exponential, null);
    }

	public ThreadPoolExecutor createThreadPoolExecutor(){
		// 缓存队列设置固定长度为2, 为了快速触发 rejectHandler
		BlockingDeque<Runnable> blockingDeque = new LinkedBlockingDeque<>(2);
		return new ThreadPoolExecutor(1, 3, 20, TimeUnit.SECONDS, blockingDeque);
	}
}
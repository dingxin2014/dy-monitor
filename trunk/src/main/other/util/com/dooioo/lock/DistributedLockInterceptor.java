package com.dooioo.lock;

import java.lang.reflect.Method;
import java.util.concurrent.locks.Lock;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.dooioo.util.Configuration;

/**
 * 分布式锁拦截器  
 * @author dingxin
 *
 */
@Aspect
@Component("distributedLockInterceptor")
public class DistributedLockInterceptor{
	
	private static final Log logger = LogFactory.getLog(DistributedLockInterceptor.class);

	private static final String zookeeperConfig = Configuration.getInstance().getZookeeperConfig();
	
	public DistributedLockInterceptor() {
		logger.info("DistributedLockInterceptor init successfully!");
	}
	
	/**
	 * 切入点
	 */
	@Pointcut("@annotation(com.dooioo.lock.DistributedSynchronized)")
    private void pointcut() {}
	
	@org.aspectj.lang.annotation.Around(value="pointcut()")
	public Object Around(ProceedingJoinPoint joinPoint) throws Throwable{
		Signature signature = joinPoint.getSignature();
    	Method method = ((MethodSignature)signature).getMethod();
    	DistributedSynchronized sync = method.getAnnotation(DistributedSynchronized.class);
		String lockName = sync.value();
		if(StringUtils.isEmpty(lockName) || lockName.contains(DistributedLock.splitStr))
			lockName = method.getDeclaringClass().getName()+"."+method.getName();
		Lock distributedLock = new DistributedLock(zookeeperConfig, lockName);
		distributedLock.lock();
		Object retObj = joinPoint.proceed();
		distributedLock.unlock();
		return retObj;
	}
	
}


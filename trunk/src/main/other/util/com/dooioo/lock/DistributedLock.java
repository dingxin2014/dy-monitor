package com.dooioo.lock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;


/**
 * <p>基于zookeeper的分布式锁</p>
 *
 */
public class DistributedLock implements Lock, Watcher{
	
	private static final Log logger = LogFactory.getLog(DistributedLock.class);
	
	private ZooKeeper zk;
	private String root = "/locks";//根
	private final String lockName;//竞争资源的标志
	private String waitNode;//等待前一个锁
	private String myZnode;//当前锁
	private CountDownLatch latch;//计数器
	private int sessionTimeout = 30000; //ms
	private int lockTimeout = 1800;  //s
	private List<Exception> exception = new ArrayList<Exception>();
	public static final String splitStr = "_dooioo_distributed_lock_";
	
	/**
	 * 创建分布式锁,使用前请确认config配置的zookeeper服务可用
	 * @param config 127.0.0.1:4180
	 * @param lockName 竞争资源标志,lockName中不能包含单词{@code _dooioo_distributed_lock_}
	 */
	public DistributedLock(String config, String lockName){
		this.lockName = lockName;
		// 创建一个与服务器的连接
		 try {
			zk = new ZooKeeper(config, sessionTimeout, this);
			Stat stat = zk.exists(root, false);
			if(stat == null){
				// 创建根节点
				zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
			}
		} catch (IOException e) {
			exception.add(e);
		} catch (KeeperException e) {
			exception.add(e);
		} catch (InterruptedException e) {
			exception.add(e);
		}
	}

	/**
	 * zookeeper节点的监视器
	 */
	@Override
	public void process(WatchedEvent event) {
		if(this.latch != null) {  
            this.latch.countDown();  
        }
	}
	
	public void lock() {
		if(exception.size() > 0){
			throw new LockException(exception.get(0));
		}
		try {
			if(this.tryLock()){
				if(logger.isInfoEnabled())
					logger.info("Thread " + Thread.currentThread().getId() + " " +myZnode + " get lock!");
				return;
			}
			else{
				waitForLock(waitNode, lockTimeout, TimeUnit.SECONDS);//等待锁
			}
		} catch (KeeperException e) {
			throw new LockException(e);
		} catch (InterruptedException e) {
			throw new LockException(e);
		} 
	}
	
	public boolean tryLock() {
		try {
			if(lockName.contains(splitStr))
				throw new LockException("lockName can not contains ［"+splitStr+"］");
			//创建临时子节点
			myZnode = zk.create(root + "/" + lockName + splitStr, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
			if(logger.isInfoEnabled())
				logger.info(myZnode + " is created");
			//取出所有子节点
			List<String> subNodes = zk.getChildren(root, false);
			//取出所有lockName的锁
			List<String> lockObjNodes = new ArrayList<String>();
			for (String node : subNodes) {
				String _node = node.split(splitStr)[0];
				if(_node.equals(lockName)){
					lockObjNodes.add(node);
				}
			}
			Collections.sort(lockObjNodes);
			if(logger.isInfoEnabled())
				logger.info(myZnode + "==" + lockObjNodes.get(0));
			if(myZnode.equals(root+"/"+lockObjNodes.get(0))){
				//如果是最小的节点,则表示取得锁
	            return true;
	        }
			//如果不是最小的节点，找到比自己小1的节点
			String subMyZnode = myZnode.substring(myZnode.lastIndexOf("/") + 1);
			waitNode = lockObjNodes.get(Collections.binarySearch(lockObjNodes, subMyZnode) - 1);
		} catch (KeeperException e) {
			throw new LockException(e);
		} catch (InterruptedException e) {
			throw new LockException(e);
		}
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) {
		try {
			if(this.tryLock()){
				return true;
			}
	        return waitForLock(waitNode,time, unit);
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
		}
		return false;
	}

	private boolean waitForLock(String lower, long waitTime, TimeUnit unit) throws InterruptedException, KeeperException {
        Stat stat = zk.exists(root + "/" + lower,true);
        //判断比自己小一个数的节点是否存在,如果不存在则无需等待锁,同时注册监听
        if(stat != null){
        	if(logger.isInfoEnabled())
        		logger.info("Thread " + Thread.currentThread().getId() + " waiting for " + root + "/" + lower);
        	this.latch = new CountDownLatch(1);
        	this.latch.await(waitTime, unit);
        	this.latch = null;
        }
        if(logger.isInfoEnabled())
			logger.info("Thread " + Thread.currentThread().getId() + " " +myZnode + " get lock!");
        return true;
    }

	public void unlock() {
		try {
			if(logger.isInfoEnabled())
				logger.info("unlock " + myZnode);
			zk.delete(myZnode,-1);
			myZnode = null;
			zk.close();
		} catch (InterruptedException e) {
			logger.info(e.getMessage(), e);
		} catch (KeeperException e) {
			logger.info(e.getMessage(), e);
		}
	}

	public void lockInterruptibly() throws InterruptedException {
		this.lock();
	}

	public Condition newCondition() {
		return null;
	}
	
	public class LockException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public LockException(String e){
			super(e);
		}
		public LockException(Exception e){
			super(e);
		}
	}

}
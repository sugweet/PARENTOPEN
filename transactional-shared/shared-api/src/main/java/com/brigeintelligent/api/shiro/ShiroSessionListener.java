package com.brigeintelligent.api.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description：shiro的Session监听器
 * @Author：Sugweet
 * @Time：2019/4/28 16:07
 */
public class ShiroSessionListener implements SessionListener {
    // 统计在线人数 juc包下线程安全自增
    private final AtomicInteger sessionCount = new AtomicInteger();

    /**
     * 会话创建时触发
     * @param session
     */
    @Override
    public void onStart(Session session) {
        // 会话创建，在线人数加一
        sessionCount.incrementAndGet();
    }

    /**
     * 会话退出时触发
     * @param session
     */
    @Override
    public void onStop(Session session) {
        // 会话退出，在线人数减一
        sessionCount.decrementAndGet();
    }

    /**
     * 会话过期时触发
     * @param session
     */
    @Override
    public void onExpiration(Session session) {
        // 会话过期，在线人数减一
        sessionCount.decrementAndGet();
    }

    /**
     * 获取在线人数
     * @return
     */
    public AtomicInteger getSessionCount() {
        return sessionCount;
    }
}

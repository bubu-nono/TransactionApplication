package com._2853_.config;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * 事务中的业务
 */
public interface TransactionBusiness<T> {

    /**
     * 获取PlatformTransactionManager对象
     * @return
     */
    PlatformTransactionManager getPlatformTransactionManager();

    /**
     * 获取ThreadConnection对象
     * @return
     */
    ThreadConnection getThreadConnection();

    /**
     * 执行业务
     */
    T doInTransaction();

}

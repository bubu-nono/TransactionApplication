package com._2853_.config;

import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 事务业务管理
 */
public class TransactionBusinessManager {

    /**
     * 执行业务逻辑
     * @param transactionBusiness 事务中的业务类
     */
    public <T> T execute(TransactionBusiness transactionBusiness) {
        /**事务定义*/
        TransactionDefinition def = new DefaultTransactionDefinition();
        /**通过事务中的业务类，获取事务管理器*/
        PlatformTransactionManager txManager = transactionBusiness.getPlatformTransactionManager();
        /**通过事务定义开启事务*/
        txManager.getTransaction(def);
        /**执行核心业务*/
        T result = (T) transactionBusiness.doInTransaction();
        /**获取事务管理器中的数据源*/
        DataSource dataSource = ((DataSourceTransactionManager)txManager).getDataSource();
        /**通过数据源获取connectionHolder对象，
         * TransactionSynchronizationManager对象只能在当前线程中有效*/
        ConnectionHolder connectionHolder =
                (ConnectionHolder)TransactionSynchronizationManager.getResource(dataSource);
        /**判断ConnectionHolder对象是否为空*/
        if(connectionHolder != null) {
            /** TransactionSynchronizationManager对象只能在当前线程中有效
             * 原来连接是从TransactionSynchronizationManager中获取，如果TransactionSynchronizationManager中已经有了，
             * 那么拿过来然后调用conHolder.requested()。否则从原始的DataSource这创建一个连接，放到一个ConnectionHolder，
             * 然后再调用TransactionSynchronizationManager.bindResource绑定。*/
            connectionHolder.requested();
            /**通过ConnectionHolder对象，获取连接*/
            Connection conn = connectionHolder.getConnection();
            /**通过事务中的业务类，获取ThreadConnection对象*/
            ThreadConnection threadConnection = transactionBusiness.getThreadConnection();
            /**将连接添加到ThreadConnection对象集合中*/
            threadConnection.addConn(conn);
            /**释放ConnectionHolder对象*/
            connectionHolder.released();

            return result;
        }
        return null;
    }

}

package com._2853_.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 管理线程连接
 */
public class ThreadConnection {

    /**定义Connection集合，存放连接*/
    private List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());

    /**是否回滚 true:回滚 false:不回滚*/
    private boolean isRollBack;

    /**
     * 添加连接
     * @param connection
     */
    public void addConn(Connection connection) {
        connections.add(connection);
    }

    /**
     * 集合中的连接回滚
     */
    public void rollback() {
       if(connections != null && connections.size() > 0) {
           /***标记为回滚 下面的commit方法就不能提交事务*/
           isRollBack = true;
           for(int i = 0; i < connections.size(); i++) {
               Connection connection = connections.get(i);;
               try {
                   connection.rollback();
               } catch (SQLException e) {
                   e.printStackTrace();
               } finally {
                   try {
                       connection.close();
                   } catch (SQLException e) {
                       e.printStackTrace();
                   }
               }
           }
       }
    }

    /**
     * 占时没有用上
     * @param ex
     * @return
     */
    private boolean rollbackOn(Throwable ex) {
        return ex instanceof RuntimeException || ex instanceof Error;
    }

    /**
     * 集合中的连接提交
     */
    public void commit() {
        /**不回滚isRollBack为false，连接不等于空并且大于0，才会执行事务提交操作*/
        if(!isRollBack && connections != null && connections.size() > 0) {
            for(int i = 0; i < connections.size(); i++) {
                Connection connection = connections.get(i);
                try {
                    connection.commit();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}

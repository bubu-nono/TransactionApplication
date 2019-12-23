package com._2853_.service;


import com._2853_.bean.Demo;
import com._2853_.config.ThreadConnection;
import com._2853_.config.TransactionBusiness;
import com._2853_.config.TransactionBusinessManager;
import com._2853_.mapper.DemoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DemoService {

    /**mapper层对象*/
    @Autowired
    private DemoMapper mapper;

    /**事务管理器*/
    @Resource(name = "platformTransactionManager")
    private PlatformTransactionManager platformTransactionManager;

    public void es(String name) {

        /**管理线程连接*/
        ThreadConnection threadConnection = new ThreadConnection();
        /**事务业务管理*/
        TransactionBusinessManager tbm = new TransactionBusinessManager();
        /**线程池*/
        ExecutorService es = Executors.newFixedThreadPool(2);
        /**模拟循环线程*/
        for(int i = 0; i < 2; i++) {
            final int index = i;
            es.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                      Integer rs = tbm.execute(new TransactionBusiness<Integer>() {
                            /**获取PlatformTransactionManager对象 主要定义事务*/
                            @Override
                            public PlatformTransactionManager getPlatformTransactionManager() {
                                return platformTransactionManager;
                            }
                            /**获取ThreadConnection对象 主要用来保存Connection连接*/
                            @Override
                            public ThreadConnection getThreadConnection() {
                                return threadConnection;
                            }
                            /**执行业务*/
                            @Override
                            public Integer doInTransaction() {
                                /**测试数据*/
                                Demo demo = new Demo();
                                demo.setName(name + "" + index);
                                /**持久化*/
                                int result = add(demo);
                                /**测试事务回滚 运行时异常 只要有一个线程出现异常就全部回滚*/
                                /**if(index == 1) {
                                    int tx = 1 / 0;
                                }*/
                                return result;
                            }
                        });
                        /**打印返回结果*/
                        System.out.println(Thread.currentThread() +" " + rs);
                    } catch (Exception e) {
                        e.printStackTrace();
                        /**事务回滚*/
                        threadConnection.rollback();
                    }
                }
            });
        }

        /**释放线程池资源*/
        es.shutdown();
        /**等待所有线程池执行完毕*/
        for(;;) {
            if(es.isTerminated()) {
                break;
            }
        }
        /**事务提交  注意：事务执行回滚操作就不会执行事务提交操作，反之执行事务提交*/
        threadConnection.commit();

    }

    /**持久化*/
    public int add(Demo demo) {
        int result = mapper.add(demo);
        return result;
    }

}

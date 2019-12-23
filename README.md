spring多线程事务统一提交和回滚


1. 核心类在工程src/main/java/目录下
   测试表在src/main/resources/demo.sql
        
2.注意事项
    1）DataSourceConfig类只实例化一个事务管理器
    事务管理器中的DataSource在application.yml中配置
    2）TransactionBusinessManager类中这段代码
       DataSource dataSource = 
            ((DataSourceTransactionManager)txManager).getDataSource();
       不一定是DataSourceTransactionManager事务管理器，可能是别的
       例如以下事务管理器
       JtaTransactionManager
       JpaTransactionManager
       HibernateTransactionManager
     3）TransactionBusinessManager类中有些位置没有判断非空操作，
                                  如果需要请自行加入
                                  
3.运行App类
  打开浏览器访问 http://localhost:8080/es?name=xxx          

4.优化（本工程没有使用AOP封装） 
   可以使用AOP方式进行封装 

       
        
        
      
# gmall   本地修改版本



gmall-user 服务端口号  8080

debug: F8逐步进入

项目架构
    工程架构
        以maven为基础  对项目的分层架构
    项目架构
        分布式  （SOA）  面向 服务的分布式架构
        

maven jar包引用
    
    
#抽取gmall-util
    1 项目种的通用框架  是所有工程需要引入的包  
        springboot   common-langs common-beanutils    
        
         
    2 项目分为 web前端controller  cookie工具类
    3 web后端  service   依赖mybatis  mysql  jsp thymeleaf redis  但是这些在web  的controller中不需要
          controller通过doubbo 和service进行通信    
          
          webUtils + commonUtils       
          serviceUtils + commonUtils
          分别是3个工程   在web  和service  中引入公共的common 
          
          
   抽取步骤
        1 将user项目拆分成user-service  和 user-web
            
        2 将项目改造成分布式的架构
            dubbo 放在那？
            放在公共里面的
            
            
            
            
       服务层  service  使用8070
       前端web层        使用8080
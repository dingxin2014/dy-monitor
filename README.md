JOB 监控 
配置方式为


  	<!-- job client 配置 配置jobHandler将会使本项目所有job被job sever监听-->
    <bean id="jobHandler" class="com.dooioo.monitor.job.JobHandler" scope="singleton">
    	<property name="appCode" value="${appCode}"></property>
    	<property name="server" value="${jobMonitorServer}"></property>
    	<property name="port" value="${jobMonitorPort}"></property>
    </bean>
    
    test环境 jobMonitorServer＝10.8.1.162 jobMonitorPort＝13406
    打开 huidan.dooioo.net  -> 打开JOB监控（如果有权限）
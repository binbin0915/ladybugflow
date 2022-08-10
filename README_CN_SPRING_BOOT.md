# LadybugFlow

[English](README.md) | [简体中文](README_CN.md)

![](https://img.shields.io/badge/license-Apache2.0-yellow)
![](https://img.shields.io/badge/Java-1.8-orange)

Ladybugflow是一个java的工作流框架，<br />
它可以在java中通过json运行工作流。<br />
我们还支持UI工具来绘制流程并转换为json文件。

[基本使用](README_CN.md) | [Properties配置](README_CN_PROPERTIES.md) | [SpringBoot整合](README_CN_SPRING_BOOT.md) | [SpringBatch整合](README_CN_SPRING_BATCH.md)

### 3. SpringBoot整合

在系统结束时，需要调用FlowStarter.shutdown();来关闭Flow管理器。

App.java

```
@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@PreDestroy
	public void onExit() {
		FlowStarter.shutdown();
	}
}
```

在Service中注入自定义的Flow类

```
@Service
public class MyService {

	@Autowired
	private MyFlow myFlow;
	
	public void startMyFlow() {
		myFlow.startFlow();
	}

}
```

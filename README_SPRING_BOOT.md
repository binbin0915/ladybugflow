# LadybugFlow

[English](README.md) | [简体中文](README_CN.md)

![](https://img.shields.io/badge/license-Apache2.0-yellow)
![](https://img.shields.io/badge/Java-1.8-orange)

Ladybugflow is a job flow framework for java, <br />
It can run a jobflow design by json in java.<br />
Also we support a UI tool to draw a flow and convert to json file.

[Basic Usage](README.md) | [Configuration](README_PROPERTIES.md) | [SpringBoot](README_SPRING_BOOT.md) | [SpringBatch](README_SPRING_BATCH.md)

### 3. Integration With SpringBoot


At the system shutdown hook, you need to call FlowStarter.shutdown(); to shutdown the Flow manager.

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

Inject custom Flow class into Service

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

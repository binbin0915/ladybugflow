# LadybugFlow

[English](README.md) | [简体中文](README_CN.md)

![](https://img.shields.io/badge/license-Apache2.0-yellow)
![](https://img.shields.io/badge/Java-1.8-orange)

Ladybugflow is a job flow framework for java, <br />
It can run a jobflow design by json in java.<br />
Also we support a UI tool to draw a flow and convert to json file.

[Basic Usage](README.md) | [Configuration](README_PROPERTIES.md) | [SpringBoot](README_SPRING_BOOT.md) | [SpringBatch](README_SPRING_BATCH.md)

### 4. Integration with SpringBatch


App.java

```
@SpringBootApplication
public class App implements ApplicationRunner {

	@Autowired
	private MyFlow myFlow;
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		myFlow.startFlow();
		
	}

}
```
# LadybugFlow

[English](README.md) | [简体中文](README_CN.md)

![](https://img.shields.io/badge/license-Apache2.0-yellow)
![](https://img.shields.io/badge/Java-1.8-orange)

Ladybugflow is a job flow framework for java, <br />
It can run a jobflow design by json in java.<br />
Also we support a UI tool to draw a flow and convert to json file.

[Basic Usage](README.md) | [Configuration](README_PROPERTIES.md) | [SpringBoot](README_SPRING_BOOT.md) | [SpringBatch](README_SPRING_BATCH.md)

### 1. Usage

#### 1.1. Import

##### Maven

```
<!-- https://mvnrepository.com/artifact/io.github.nobuglady/ladybugflow -->
<dependency>
    <groupId>io.github.nobuglady</groupId>
    <artifactId>ladybugflow</artifactId>
    <version>0.0.6</version>
</dependency>
```

##### Gradle
```
// https://mvnrepository.com/artifact/io.github.nobuglady/ladybugflow
implementation 'io.github.nobuglady:ladybugflow:0.0.6'
```

#### 1.2. Generate flowchart
There is a graphical tool under the directory [html/network.html] <br />
that can help you generate a json file for Flow description.

Draw a flowchart

<img src="https://github.com/nobuglady/nobuglady-network/blob/main/readme/4.gif?raw=true" alt="" width="400px"/>

Convert flowchart to json

<img src="https://github.com/nobuglady/nobuglady-network/blob/main/readme/5.gif?raw=true" alt="" width="400px"/>

#### 1.3. Configuration

You need to write a class that extends from FlowRunner, <br />
and a json file describing the Flow process, <br />
these two files need to be placed under the same package

```
src/main/java
- com.me
  + MyFlow1.java
  + MyFlow1.json
```

<details>
<summary> <b>MyFlow1.java</b> </summary>

```
public class MyFlow1 extends FlowRunner {
 
    @Node (label=  "a" )
    public void process_a() {
        System.out.println(  "processing a" );
    }
   
    @Node (label=  "b" )
    public void process_b() {
        System.out.println(  "processing b" );
    }
   
    @Node (label=  "c" )
    public void process_c() {
        System.out.println(  "processing c" );
    }
   
    @Node (label=  "d" )
    public void process_d() {
        System.out.println(  "processing d" );
    }
}
```

</details>


<details>
<summary><b>MyFlow1.json</b></summary>

```
{
	"flowId": "123",
	"nodes": [
		{
			"id": "1",
			"label": "a"
		},
		{
			"id": "2",
			"label": "b"
		},
		{
			"id": "0b5ba9df-b6c7-4752-94e2-debb6104015c",
			"label": "c"
		},
		{
			"id": "29bc32c7-acd8-4893-9410-e9895da38b2e",
			"label": "d"
		}
	],
	"edges": [
		{
			"id": "1",
			"from": "1",
			"to": "2",
			"arrows": "to"
		},
		{
			"id": "078ffa82-5eff-4d33-974b-53890f2c9a18",
			"from": "1",
			"to": "0b5ba9df-b6c7-4752-94e2-debb6104015c",
			"arrows": "to"
		},
		{
			"id": "90663193-7077-4aca-9011-55bc8745403f",
			"from": "2",
			"to": "29bc32c7-acd8-4893-9410-e9895da38b2e",
			"arrows": "to"
		},
		{
			"id": "a6882e25-c07a-4abd-907e-e269d4eda0ec",
			"from": "0b5ba9df-b6c7-4752-94e2-debb6104015c",
			"to": "29bc32c7-acd8-4893-9410-e9895da38b2e",
			"arrows": "to"
		}
	]
}
```

</details>

#### 1.4. Start
The flow you define can be started by the following code
```
MyFlow1 myFlow1 =  new MyFlow1();
myFlow1.startFlow();
```
#### 1.5. Stop
When the system stops, you need to call the following code to stop the flow management system
```
FlowStarter.shutdown();
```

### 2. Run Result

#### 2.1. Success log


<details>
<summary>The following is the log file of the success log</summary>

```
Ready queue thread started.
Complete queue thread started.
json:
{"flowId":"123","nodes":[{"id":"1","label":"a"},{"id":"2","label":"b"},{"id":"0b5ba9df-b6c7-4752-94e2-debb6104015c","label":"c"},{"id":"29bc32c7-acd8-4893-9410-e9895da38b2e","label":"d"}],"edges":[{"id":"1","from":"1","to":"2","arrows":"to"},{"id":"078ffa82-5eff-4d33-974b-53890f2c9a18","from":"1","to":"0b5ba9df-b6c7-4752-94e2-debb6104015c","arrows":"to"},{"id":"90663193-7077-4aca-9011-55bc8745403f","from":"2","to":"29bc32c7-acd8-4893-9410-e9895da38b2e","arrows":"to"},{"id":"a6882e25-c07a-4abd-907e-e269d4eda0ec","from":"0b5ba9df-b6c7-4752-94e2-debb6104015c","to":"29bc32c7-acd8-4893-9410-e9895da38b2e","arrows":"to"}]}
execute:1
node name:a
processing a
execute:2
node name:b
processing b
execute:0b5ba9df-b6c7-4752-94e2-debb6104015c
node name:c
processing c
execute:29bc32c7-acd8-4893-9410-e9895da38b2e
node name:d
processing d
Complete success.
json:
{"nodes":[{"id": "1","label": "a" ,"color": "#36AE7C"},{"id": "2","label": "b" ,"color": "#36AE7C"},{"id": "0b5ba9df-b6c7-4752-94e2-debb6104015c","label": "c" ,"color": "#36AE7C"},{"id": "29bc32c7-acd8-4893-9410-e9895da38b2e","label": "d" ,"color": "#36AE7C"}],"edges":[{"id": "1","from": "1","to": "2","arrows": "to"},{"id": "078ffa82-5eff-4d33-974b-53890f2c9a18","from": "1","to": "0b5ba9df-b6c7-4752-94e2-debb6104015c","arrows": "to"},{"id": "90663193-7077-4aca-9011-55bc8745403f","from": "2","to": "29bc32c7-acd8-4893-9410-e9895da38b2e","arrows": "to"},{"id": "a6882e25-c07a-4abd-907e-e269d4eda0ec","from": "0b5ba9df-b6c7-4752-94e2-debb6104015c","to": "29bc32c7-acd8-4893-9410-e9895da38b2e","arrows": "to"}]}
```
</details>

After the process runs, a json string describing the process status will be output, <br />
which can be pasted into a graphical tool to view the process running status.
(green:success, red:error, white:waiting).

<img src="https://github.com/nobuglady/nobuglady-network/blob/main/readme/2.gif?raw=true" alt="" width="400px"/>

#### 2.2. Error log


<details>
<summary>Below is the log file of the run error</summary>

```
Ready queue thread started.
Complete queue thread started.
json:
{"flowId":"123","nodes":[{"id":"1","label":"a"},{"id":"2","label":"b"},{"id":"0b5ba9df-b6c7-4752-94e2-debb6104015c","label":"c"},{"id":"29bc32c7-acd8-4893-9410-e9895da38b2e","label":"d"}],"edges":[{"id":"1","from":"1","to":"2","arrows":"to"},{"id":"078ffa82-5eff-4d33-974b-53890f2c9a18","from":"1","to":"0b5ba9df-b6c7-4752-94e2-debb6104015c","arrows":"to"},{"id":"90663193-7077-4aca-9011-55bc8745403f","from":"2","to":"29bc32c7-acd8-4893-9410-e9895da38b2e","arrows":"to"},{"id":"a6882e25-c07a-4abd-907e-e269d4eda0ec","from":"0b5ba9df-b6c7-4752-94e2-debb6104015c","to":"29bc32c7-acd8-4893-9410-e9895da38b2e","arrows":"to"}]}
execute:1
node name:a
processing a
execute:2
node name:b
processing b
execute:0b5ba9df-b6c7-4752-94e2-debb6104015c
node name:c
processing c
java.lang.reflect.InvocationTargetException
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at io.github.nobuglady.network.fw.FlowRunner.execute(FlowRunner.java:49)
	at io.github.nobuglady.network.fw.executor.NodeRunner.run(NodeRunner.java:93)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
	at java.base/java.lang.Thread.run(Thread.java:834)
Caused by: java.lang.RuntimeException: test
	at io.github.nobuglady.network.MyFlow1.process_b(MyFlow1.java:16)
	... 11 more
java.lang.reflect.InvocationTargetException
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at io.github.nobuglady.network.fw.FlowRunner.execute(FlowRunner.java:49)
	at io.github.nobuglady.network.fw.executor.NodeRunner.run(NodeRunner.java:93)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
	at java.base/java.lang.Thread.run(Thread.java:834)
Caused by: java.lang.RuntimeException: test
	at io.github.nobuglady.network.MyFlow1.process_b(MyFlow1.java:16)
	... 11 more
Complete error.
json:
{"nodes":[{"id": "1","label": "a" ,"color": "#36AE7C"},{"id": "2","label": "b" ,"color": "#EB5353"},{"id": "0b5ba9df-b6c7-4752-94e2-debb6104015c","label": "c" ,"color": "#36AE7C"},{"id": "29bc32c7-acd8-4893-9410-e9895da38b2e","label": "d" ,"color": "#E8F9FD"}],"edges":[{"id": "1","from": "1","to": "2","arrows": "to"},{"id": "078ffa82-5eff-4d33-974b-53890f2c9a18","from": "1","to": "0b5ba9df-b6c7-4752-94e2-debb6104015c","arrows": "to"},{"id": "90663193-7077-4aca-9011-55bc8745403f","from": "2","to": "29bc32c7-acd8-4893-9410-e9895da38b2e","arrows": "to"},{"id": "a6882e25-c07a-4abd-907e-e269d4eda0ec","from": "0b5ba9df-b6c7-4752-94e2-debb6104015c","to": "29bc32c7-acd8-4893-9410-e9895da38b2e","arrows": "to"}]}
```

</details>

After the process runs, a json string describing the process status will be output, <br />
which can be pasted into a graphical tool to view the process running status.
(green:success, red:error, white:waiting).

<img src="https://github.com/nobuglady/nobuglady-network/blob/main/readme/3.gif?raw=true" alt="" width="400px"/>

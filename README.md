# :izakaya_lantern:nobuglady-network:izakaya_lantern:

![](https://img.shields.io/badge/license-Apache2.0-yellow)

### :blue_book: Usage

#### Configuration

You need to make a class that extends from FlowRunner, and a configuration file of flow, which is placed in the same directory.

<img src="https://github.com/nobuglady/nobuglady-network/blob/main/readme/1.png?raw=true" alt="" width="400px"/>

MyFlow1.java

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
    public void process_c() {
        System.out.println(  "processing d" );
    }
}
```

MyFlow1.json
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
#### Start
Then start the flow with the following code.
```
MyFlow1 myFlow1 =  new MyFlow1();
myFlow1.startFlow();
```
#### Shutdown
When the system shutdown, close the flow manager by the following code

```
FlowStarter.shutdown();
```

### :blue_book: Run

#### Success log
The success log is as follows
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

After the process is ended, you can get the execution result json in the last line.
Paste json to the following location can check the status of the flow. 
(green:success, red:error, white:waiting).

<img src="https://github.com/nobuglady/nobuglady-network/blob/main/readme/2.gif?raw=true" alt="" width="400px"/>

#### Error log
The error log is as follows
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

After the process is ended, you can get the execution result json in the last line.
Paste json to the following location can check the status of the flow. 
(green:success, red:error, white:waiting).

<img src="https://github.com/nobuglady/nobuglady-network/blob/main/readme/3.gif?raw=true" alt="" width="400px"/>

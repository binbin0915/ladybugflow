package io.github.nobuglady.network;

import io.github.nobuglady.network.fw.starter.FlowStarter;

public class Test {

	public static void main(String[] args) {
		MyFlow1 myFlow1 = new MyFlow1();
		myFlow1.startFlow();

		try {
			Thread.sleep(10000);
			FlowStarter.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

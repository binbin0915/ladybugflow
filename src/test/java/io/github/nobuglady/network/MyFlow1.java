package io.github.nobuglady.network;

import io.github.nobuglady.network.fw.FlowRunner;
import io.github.nobuglady.network.fw.annotation.Node;

public class MyFlow1 extends FlowRunner {

	@Node(label = "a")
	public void process_a() {
		System.out.println("processing a");
	}

	@Node(label = "b")
	public void process_b() {
		System.out.println("processing b");
//		throw new RuntimeException("test");
	}

	@Node(label = "c")
	public void process_c() {
		System.out.println("processing c");
	}

	@Node(label = "d")
	public void process_d() {
		System.out.println("processing d");
	}
}

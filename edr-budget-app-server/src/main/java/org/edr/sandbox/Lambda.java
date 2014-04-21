package org.edr.sandbox;

public class Lambda {

	@FunctionalInterface
	interface TestLambda
	{
		void print(String x);
	}
	
	public static void doeIets(TestLambda f)
	{
		f.print("hello world!");
	}
	
	public static void main(String[] args)
	{
		String x = "!";
		doeIets(System.out::println);
		doeIets(s -> System.out.println(s + x));
	}
	
}

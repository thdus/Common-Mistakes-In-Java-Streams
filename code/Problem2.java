package step02;

import java.awt.SystemColor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Problem2 {
	
	public static void P2() {
		List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
		
		names.stream()
			.filter(name -> {
				if(name.startsWith("B")) {
					names.remove(name);
				}
				return true;
			})
			.forEach(System.out::println);
		
		System.out.println("Remaining names: " + names);	
	}
	
	public static void S2_origin() {
		List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
		
		List<String> filteredNames = names.stream()
										.filter(name -> !name.startsWith("B"))
										.collect(Collectors.toList());
		
		System.out.println("Filtered names: " + filteredNames);
		System.out.println("Original names remain unchanged: "+ names);
	}
	
	public static void S2_new() {
		List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
		
		List<String> filteredNames = names.stream()
										.filter(name -> !name.startsWith("B"))
										.toList();
		
		System.out.println("Filtered names: " + filteredNames);
		System.out.println("Original names remain unchanged: "+ names);
	}
	
	
	
	public static void main(String[] args) {
		
		long start = System.nanoTime();
	
		// 실행 코드
		//P2();
		//S2_origin();
		S2_new();
		
		long end = System.nanoTime();
		
		double elapsedMs = (end - start) / 1_000_000.0;
		System.out.println("실행 시간 : " + elapsedMs + "ms");
		
		
	}

}

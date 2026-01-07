package step02;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Problem5 {

	public static void P5() {
		List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
		
		String firstNameStartingWithZ = names.stream()
				.filter(name -> name.startsWith("Z"))
				.findFirst()
				.get();
		
		System.out.println(firstNameStartingWithZ);
	}
	
	public static void S5_origin() {
		List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
		
		Optional<String> firstNameStartingWithZ = names.stream()
				.filter(name -> name.startsWith("Z"))
				.findFirst();
		
		if(firstNameStartingWithZ.isPresent()) {
			System.out.println(firstNameStartingWithZ.get());
		}else {
			System.out.println("No name starts with Z");
		}
		
	}
	
	public static void S5_new() {
		List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
		
		String firstNameStartingWithZ = names.stream()
				.filter(name -> name.startsWith("Z"))
				.findFirst()
				.orElse("No name starts with Z");
		
		System.out.println(firstNameStartingWithZ);
	}

	
	public static void main(String[] args) {
		
		long start = System.nanoTime();
		
		// 실행 코드
		//P5();
		//S5_origin();
		S5_new();
		
		long end = System.nanoTime();
		
		double elapsedMs = (end - start) / 1_000_000.0;
		System.out.println("실행 시간 : " + elapsedMs + "ms");
		
		
	}

}

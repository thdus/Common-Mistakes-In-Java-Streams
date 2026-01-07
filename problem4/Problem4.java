package lab02;

import java.util.Arrays;
import java.util.List;

public class Problem4 {

	public static void main(String[] args) {

		//4. Overusing Intermediate Operations:Mistake
		/*
		 * 기사 내용: Intermediate operation(filter(), map() 등) 을 너무 과도하게 사용하면 성능 문제 발생
		 * filter()를 여러 번 쓰기 보다는 ||, && 같은 연산자 이용해서 한 번만 사용하는 것이 성능에서 유리
		 * 
		 * 그 이유는?
		 * 	.filter() 메소드는 interface 이므로 사용하려면 람다식을 넘겨줘서 구현체를 생성해야 함.
		 * 	따라서 여러 번 사용하면 구현체를 생성하는 횟수가 많아져서 오버헤드 증가할 것 같음.
		 * 
		 * 실제 성능 검증
		 * 	약 7~10배 정도 실행 시간 차이 발생
		 * 
		 * 결론
		 * 	코드 가독성이 떨어지더라도 한 번에 몰아서 사용하는 것이 성능면에선 유리하다.
		 */
		
		List<String> datas = Arrays.asList("abc", "cbb", "fisa", "study", "cloud", "ce6", "csdfs");
		
		long startTime = System.nanoTime();
		List<String> datas1 = datas.stream()
									.map(String::toUpperCase)
									.map(v -> v.replace('C', 'Z'))
									.filter(v -> v.length() > 3)
									.filter(v -> v.contains("U"))
									.toList();
		long endTime = System.nanoTime();
		System.out.println(datas1);
		System.out.println("Intermediate Operations 과도하게 사용 시 : " + (double)(endTime - startTime)/1_000_000_000);
		
		long startTime2 = System.nanoTime();
		List<String> datas2 = datas.stream()
									.map(v -> v.toUpperCase().replace('C', 'Z'))
									.filter(v -> (v.length() > 3) && v.contains("U"))
									.toList();
		long endTime2 = System.nanoTime();
		System.out.println(datas1);
		System.out.println("Intermediate Operations 한번씩만 사용 시 : " + (double)(endTime2 - startTime2)/1_000_000_000);
		
	}

}

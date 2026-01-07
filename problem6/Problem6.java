package lab02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Problem6 {

	public static void main(String[] args) {

		//4. Neglecting Thread Safety:Mistake
		/*
		 * 기사 내용: mutable한 공유 자원을 조작할 때 parallel streams를 사용하면 race condition 발생 가능
		 * 
		 * 그 이유는?
		 * 	멀티쓰레드 환경에서 취약점이 있는 자료구조(ArrayList 등)에는 lock이 없어서 경합 발생 가능
		 * 
		 * 실제 검증
		 * 	10000개의 데이터가 들어있어야 할 results에 경합으로 인해 더 적은 데이터가 들어있음.
		 * 
		 * 결론
		 * 	멀티쓰레드 환경에서 자료를 다룰 때에는 멀티쓰레드 환경에도 안전한 자료구조를 사용해야 한다.
		 */
		
		List<String> datas = new ArrayList<>();
		List<String> results = new ArrayList<>();
		
		for(int k = 0; k<1000; k++) {
			datas.add("abc");
			datas.add("abc");
			datas.add("abc");
			datas.add("abc");
			datas.add("abc");
			datas.add("abc");
			datas.add("abc");
			datas.add("abc");
			datas.add("abc");
			datas.add("abc");
		}
		
		datas.parallelStream().forEach(v -> {
			results.add(v.concat("1"));
		});
			
		
		System.out.println("results의 예상 사이즈 : " + 10000);
		System.out.println("results의 실제 사이즈 : " + results.size());
		
		List<String> datas2 = new ArrayList<>();
		List<String> results2 = new CopyOnWriteArrayList<String>();
		
		//안전한 경우
		for(int k = 0; k<1000; k++) {
			datas2.add("abc");
			datas2.add("abc");
			datas2.add("abc");
			datas2.add("abc");
			datas2.add("abc");
			datas2.add("abc");
			datas2.add("abc");
			datas2.add("abc");
			datas2.add("abc");
			datas2.add("abc");
		}
		
		datas.parallelStream().forEach(v -> {
			results2.add(v.concat("1"));
		});
			
		
		System.out.println("results의 예상 사이즈 : " + 10000);
		System.out.println("results의 실제 사이즈 : " + results2.size());
		
		
	}

}

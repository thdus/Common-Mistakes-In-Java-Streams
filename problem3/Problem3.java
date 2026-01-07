package step2;
import java.util.List;
import java.util.stream.IntStream;

public class Problem3 {

    public static void main(String[] args) {

        int[] sizes = {
                1_000,
                5_000,
                10_000,
                50_000,
                100_000,
                500_000,
                1_000_000,
                5_000_000
        };

        System.out.println("사용 가능한 CPU 코어 수: "
                + Runtime.getRuntime().availableProcessors());
        System.out.println("----------------------------------------");

        for (int size : sizes) {

            List<Integer> numbers = IntStream.rangeClosed(1, size)
                                             .boxed()
                                             .toList();

            // 🔹 JVM 워밍업
            for (int i = 0; i < 3; i++) {
                numbers.stream().mapToInt(n -> n * n).sum();
                numbers.parallelStream().mapToInt(n -> n * n).sum();
            }

            long streamTime = measureStreamMicro(numbers);
            long parallelTime = measureParallelStreamMicro(numbers);

            System.out.printf(
                "데이터 개수=%d | 일반 stream=%d μs | parallelStream=%d μs%n",
                size, streamTime, parallelTime
            );

            if (parallelTime < streamTime) {
                System.out.println("👉 이 지점부터 parallelStream이 더 빠릅니다.\n");
                break;
            }

            System.out.println();
        }
    }

    private static long measureStreamMicro(List<Integer> numbers) {
        long start = System.nanoTime();
        numbers.stream()
               .mapToInt(n -> n * n)
               .sum();
        return (System.nanoTime() - start) / 1_000; // μs
    }

    private static long measureParallelStreamMicro(List<Integer> numbers) {
        long start = System.nanoTime();
        numbers.parallelStream()
               .mapToInt(n -> n * n)
               .sum();
        return (System.nanoTime() - start) / 1_000; // μs
    }
}

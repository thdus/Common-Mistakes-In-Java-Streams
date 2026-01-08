# Java Stream 사용 시 흔히 하는 실수 정리

Java Stream API는 코드 가독성과 선언형 프로그래밍을 크게 향상시키지만,  
잘못 사용할 경우 코드 미실행, 성능 저하, race condition 등 다양한 문제를 유발할 수 있습니다.

본 레포지토리는 Java Stream 사용 시 자주 발생하는 실수와 그 원인, 개선 방법을  
실제 코드 예제와 수행 시간 비교를 통해 정리한 자료입니다.

---


##  목적

- Java Stream의 중간 연산 / 종료 연산 구조 이해
- 성능 저하 및 동시성 이슈를 유발하는 대표적인 안티패턴 정리
- 실무에서 안전하고 효율적인 Stream 사용 기준 제시

---


## 1️⃣ 종료 연산을 호출하지 않는 경우

###  문제
Stream은 종료 연산이 호출되어야 실행됩니다.  
filter() 같은 중간 연산만 호출하면 Stream은 실제로 동작하지 않습니다.

```java
names.stream()
     .filter(name -> name.startsWith("A"));

System.out.println("Stream operations have not been executed");
```

###  해결
names.stream()
     .filter(name -> name.startsWith("A"))
     .forEach(System.out::println);

- 중간 연산의 반환 타입은 Stream
- 종료 연산의 반환 타입은 Stream이 아님


## 2️⃣ Stream 처리 중 원본 자료구조 변경

### 문제
Stream 처리 도중 remove() 등으로 원본 컬렉션을 수정하면
예상치 못한 결과 또는 일부 데이터만 처리되는 문제가 발생할 수 있습니다.

```java
names.stream()
     .filter(name -> {
         if (name.startsWith("B")) {
             names.remove(name);
         }
         return true;
     })
     .forEach(System.out::println);
```

### 해결
List<String> filtered =
    names.stream()
         .filter(name -> !name.startsWith("B"))
         .toList();
-  Stream 내부에서는 불변 처리(Immutable) 유지
-  결과는 새로운 컬렉션으로 생성


## 3️⃣ Parallel Stream 오버헤드 무시

### 문제
소규모 데이터나 가벼운 연산에서 parallelStream()을 사용하면
스레드 분할·병합 비용으로 인해 오히려 성능이 저하될 수 있습니다.

```java
numbers.parallelStream()
       .map(n -> n * n)
       .forEach(System.out::println);
```

### 기준
-  데이터 수가 적거나 연산이 가벼운 경우
-  I/O 작업 또는 공유 자원 접근이 있는 경우
-  대용량 데이터 + CPU 연산 위주일 때만 고려


## 4️⃣ 중간 연산 과도 사용으로 인한 성능 저하

### 문제
filter(), map() 등을 과도하게 사용하면
람다 구현체 생성 비용으로 인해 성능 저하가 발생할 수 있습니다.

```java
names.stream()
     .filter(n -> n.startsWith("A"))
     .filter(n -> n.length() > 3)
     .map(String::toUpperCase)
     .map(n -> n + " is a name")
     .toList();
```

### 개선

```java
names.stream()
     .filter(n -> n.startsWith("A") && n.length() > 3)
     .map(n -> n.toUpperCase() + " is a name")
     .toList();
```
실제 테스트 결과 약 7~10배 수행 시간 차이 발생
가독성보다 성능이 중요한 경우 연산 통합 고려


## 5️⃣ Optional 값 존재 여부 확인 없이 사용

### 문제
findFirst(), reduce() 등은 Optional을 반환합니다.
값이 없는 상태에서 get() 호출 시 예외가 발생합니다.

```java
String result =
    names.stream()
         .filter(n -> n.startsWith("Z"))
         .findFirst()
         .get(); // NoSuchElementException
```

### 해결
```java
String result =
    names.stream()
         .filter(n -> n.startsWith("Z"))
         .findFirst()
         .orElse("No name starts with Z");
```
isPresent() 또는 orElse() 사용 권장
orElse() 사용 시 성능 차이는 유의미하지 않음


## 6️⃣ Parallel Stream에서 스레드 안전성 무시

### 문제
parallelStream()에서 공유 mutable 객체를 사용하면
race condition이 발생하여 결과의 일관성이 깨질 수 있습니다.

```java
List<Integer> results = new ArrayList<>();

numbers.parallelStream()
       .forEach(n -> results.add(n * 2));
```
### 결과
예상 크기: 10000
실제 결과: 매 실행마다 다름

### 해결

```java
List<Integer> results = new CopyOnWriteArrayList<>();

numbers.parallelStream()
       .forEach(n -> results.add(n * 2));
```
 멀티스레드 환경에서는 Thread-safe 자료구조 사용 필수
 가능하면 collect() 기반의 불변 처리 권장

### 결론 요약

Stream은 종료 연산이 있어야 실행된다
Stream 내부에서 원본 컬렉션을 수정하지 말 것
Parallel Stream은 무조건 빠르지 않다
중간 연산은 적절히 통합하여 사용
Optional은 항상 값 존재 여부 확인
Parallel Stream에서는 스레드 안전성 최우선

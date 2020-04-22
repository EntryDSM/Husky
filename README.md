# Husky
Entry 5.0 Main Backend API

## Project rules

### branch-naming rule

`[module name]-[feature info]`

ex) `core-grade-output` 



### commit-message rule

`[verb] noun(feature info)`

ex) `[FIX] post`

ref) [commit verb 사전](https://www.notion.so/2020-03-31-Husky-f0bc5262b9d24b44b82f570494df0000#d3d1ec59ea9b4f90a92ff49c283b54ac)



### Multi-Module Pattern

gradle 기반 Multi-module Project

ref) [springboot multi module 설명](https://cheese10yun.github.io/gradle-multi-module/)



## Code convention

### Bean Injection

`Constructor Injection` (생성자를 이용한 의존성 삽입)

Lombok 어노테이션 활용

example code)

```java
@RequiredArgsConstructor
public class Example {
    private final TestRepository testRepository; // 생성자로 빈을 주입받는다.
}
```



## Technical Stacks

### Docs

Spring Rest Docs

ref) [Spring Rest Docs(우아한 형제들 기술 블로그)](https://woowabros.github.io/experience/2018/12/28/spring-rest-docs.html)

## Part
	- @임용성
		: 인증, PDF 생성(Apache POI)
	- @김재훈
		: 성적계산(부동소수점), 인프라 구축
	- @황신우(불참) - 저장, 최종제출 관련


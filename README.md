# springcomics-server
 <p>공부한 것을 만화로 그리며 복습하려 만든 웹툰 연재 사이트 서버입니다.</p>

## 🚀 기술 스택
### ⚡백엔드
<div>
<img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white"> 
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
<img src="https://img.shields.io/badge/security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> 
<img src="https://img.shields.io/badge/intelli j-000000?style=for-the-badge&logo=intellijidea&logoColor=white">
</div>

### ⚡인프라
<div>
  <img src="https://img.shields.io/badge/AWS EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
  <img src="https://img.shields.io/badge/AWS RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">
  <img src="https://img.shields.io/badge/AWS S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
</div>

### ⚡도구
<div>
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
</div>

### ⚡프론트엔드
<div>
  <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black">
  <img src="https://img.shields.io/badge/VS%20Code-007ACC?style=for-the-badge&logo=visualstudiocode&logoColor=white">  
  <img src="https://img.shields.io/badge/AWS S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
</div>


<br/>

## 💻 프로젝트 소개

### ⚡기간
2025-01-05 ~ 2025-02-11

 <br/>

### ⚡주요기능(v1)

| 역할 | 기능 |
| ----- | ----- |
|유저|로그인 후 만화를 열람하고 댓글을 작성할 수 있습니다.|
|작가|관리자에게 아이디를 부여받아 만화를 연재할 수 있습니다.|
|관리자|유저, 작가, 작품, 댓글 목록을 확인하고 삭제할 수 있습니다.|

 <br/>

### ⚡스크린샷

| 유저 페이지 | 작가 페이지 | 어드민 페이지 |
| ----- | ----- | ----- |
|![image](https://github.com/user-attachments/assets/63f91eb0-7fb3-4409-a87f-b7cca6cebbab)|![image](https://github.com/user-attachments/assets/f8761566-e8e1-4dd0-b697-cbc14d63c9a4)|![image](https://github.com/user-attachments/assets/0855f479-a164-4b09-97a9-654c287b6489)|

 <br/>

### ⚡프로젝트 후기

- @ControllerAdvice를 활용하여 런타임 예외 클래스를 커스텀하여 응답 하였습니다.
  - 인프런 호돌맨 강의를 참고한 것인데, 필요한 예외들을 직접 만들어 사용하니 다루기가 편했습니다.
  ```java
  public abstract class SpringComicsException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public SpringComicsException(String message) {
        super(message);
    }

    public SpringComicsException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }

  ```

- 스프링 시큐리티
  - 학원 파이널 프로젝트때 써보려다 어떻게 다루는지 몰라서 고생했던 기억이...
  - 지금도 어렵지만 UsernamePasswordAuthenticationToken 으로 아이디, 패스워드 확인
  - 필터 통과시 SecurityContextHolder에 유저를 담아 컨트롤러에 보내 사용자 확인이 필요한 기능에서 활용
  - cors 설정 등 알아갈수록 편리한 친구... 그래도 아직은 어색한 관계...
  - 작가와 유저 각각의 UserDetails를 사용하고 싶었지만 두개를 만들어 놓으니 스프링이 무엇을 주입할지 몰라서 아무것도 안써버리는 선택을 하던데
  - 빈 이름 명시해주기, 필터체인 각각 만들어 순서 나누기 등 구글링하여 여러 방법을 시도해봤으나 아직 어색한 관계로 협의가 잘 안되어
  - 우선은 얄팍한 대안으로 작가 전용 아이디 도메인명을 사용해 유저 아이디와 구분하여 한 UserDetails를 같이 쓰고 있습니다...

- Spring Data JPA & Querydsl
  - 인프런 김영한의 JPA 시리즈를 수강하며 조금은 가까워졌을까
  - 테이블 3개를 조인하여 조회하는데 한번에 쿼리가 6개가 나간다.
  - 그냥 쓰면 어떻게 날뛸지 모르는 JPA를 Querydsl로 진정시키니 2개로 줄어들었다.
  - 1개로 줄이지 못한것은 DTO에 List를 담아야하는데 쿼리를 어떻게 짜야할지 몰라서 리스트 따로 나머지 따로 해서 두번..
  - JPA를 쓰고싶다면 SQL 공부를 네이티브 쿼리 쓸때보다 더 많이 해야 통제가 되겠다.
 

 <br/>

### ⚡아키텍쳐
![image](https://github.com/user-attachments/assets/cfb38bb8-7195-4216-83e5-3ab86212b4e0)

### ⚡ERD
![image](https://github.com/user-attachments/assets/0c154722-a516-4c53-bc0f-01264492176a)


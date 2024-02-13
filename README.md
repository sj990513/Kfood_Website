# Kfood_Website
외국인들을 위한 한식 레시피 소개 사이트 입니다.
http://www.kfoodwebsite.shop

<br>

 - **개발기간:** 23/12/27 ~ 24/02/08
 
 - **플랫폼 :** Web
 
 - **개발인원 :** 1명 (개인 프로젝트)

<br>

## 개발 환경
- **언어 :** Java 17, HTML/CSS, JavaScript

- **서버 :** AWS EC2, ubuntu

  
- **프레임워크 :** Spring Boot 3.0.6

  
- **추가 프레임워크/라이브러리 :** Spring Security, Tymleaf, Jdbc Template

  
- **DB :** My SQL 8.0.35

  
- **IDE :** IntelliJ IDEA 2023, My SQL WorkBench


<br>

## ERD 설계
![d1d1d1](https://github.com/sj990513/Kfood_Website/assets/117420071/5e5c0673-ecb5-4a03-af45-2e34123ef86f)

<br>

## 구현 기능
* 계정 관련
  * 로그인, 로그아웃, 회원가입, 회원탈퇴
  * ID찾기, 비밀번호찾기
  * 회원가입, 비밀번호찾기시 이메일인증
  * 비밀번호 암호화
  * 마이페이지
    * 개인정보 확인 & 변경, 본인이 작성한 게시글, 댓글 확인 & 삭제

<br>

* 게시글 관련
  * 게시글 CRUD
    * 메뉴 등록, 삭제, 수정, 검색, 정렬
    * 메뉴에 관련된 레시피와 이미지 등록, 삭제, 수정 
  * 게시글 즐겨찾기 (북마크)
  * 메뉴 Offset 페이징
 
<br>

* 관리자 페이지 관련
  * 회원 관리 
    * 회원 등급 변경
    * 회원이 작성한 글, 댓글 확인
    * 회원 삭제

<br>
<br>

## 주요 기능 설명
* 회원 가입 및 로그인
  * 이메일 인증 기능
  * 아이디 중복검사
  * 비밀번호 일치 검사
  * 아이디 찾기
  * 비밀번호 찾기 (이메일) 
![로그인](https://github.com/sj990513/Kfood_Website/assets/117420071/ffe7e98c-8ab2-42c8-9065-734b6428e440)

<br>
<br>

* 기본 메뉴 리스트
  * 영양소 기준 정렬
  * 검색어 기준 정렬
![d2d2d2](https://github.com/sj990513/Kfood_Website/assets/117420071/c9bb7ec8-6557-4bab-8e6f-85161b4bdc1a)
<br>
<br>

* 로그인 상태의 메뉴 리스트 (북마크 기능 추가)
![c2c3](https://github.com/sj990513/Kfood_Website/assets/117420071/f1136655-0aea-413a-9287-9a5b01a2e63d)
<br>
<br>

* 상세 레시피
  * 로그인한 사용자만 댓글달기 가능
  * 게시글 작성자 본인만 게시글 수정, 삭제 가능
![c3c3c33c](https://github.com/sj990513/Kfood_Website/assets/117420071/ad29ae6b-744e-4aaf-832a-79034d4469c3)

<br>
<br>

* 게시글 수정
  * 동적으로 레시피 개수 조절 가능
![d2d2edex](https://github.com/sj990513/Kfood_Website/assets/117420071/ae442b3f-dfde-48dd-9dfc-fb1b0793b8e6)

<br>
<br>

* 게시글 추가
  * 동적으로 레시피 개수 조절 가능
![add](https://github.com/sj990513/Kfood_Website/assets/117420071/a7f0a845-7d01-435a-8f6d-e40aeeacfdce)


<br>
<br>

* 즐겨찾기
  * 즐겨찾기 삭제 가능
![asdasd](https://github.com/sj990513/Kfood_Website/assets/117420071/4b17b3ae-f4fe-4988-ba73-66fdc1591505)

<br>
<br>

* 마이페이지
  * 회원정보수정
  * 작성한 게시글, 댓글 확인
  * 회원 탈퇴
![qwe](https://github.com/sj990513/Kfood_Website/assets/117420071/7c392954-0837-48b0-b9f6-ed63e0fec6d0)

<br>
<br>

* 회원 관리 페이지
  * 관리자만 이용가능 
  * 회원등급변경
  * 회원이 작성한 게시글, 댓글 확인 및 삭제
  * 회원 삭제
![12322](https://github.com/sj990513/Kfood_Website/assets/117420071/64512dd9-06a3-48a3-a39b-fbb169abeabb)















  

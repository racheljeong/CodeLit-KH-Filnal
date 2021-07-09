# CodeL!t



영상 강의 수강 플랫폼 CodeL!t
==============================


**프로젝트 명** : CodeL!t </br>
**팀 명** : Codesmith </br>
**벤치마킹** : 인프런 </br>
**프로젝트 소개** :  </br>
웹 교육 플랫폼을 목표로 지속적으로 수요가 증가하고 있는 개발 및 IT 관련 분야에 대한 동영상 강의 사이트 구현 </br>
**개발 배경** : </br>
에듀 테크 시장의 지속적인 성장과 더불어 IT 분야에 대한 수요 증가 </br>

------------------------------------------
**개발환경**
<br/>

os : windows 10
language : java, oracle sql, HTML5, CSS3, JavaScript, JQuery
framework : spring, bootstrap, mybatis
ide : sts4, vsCode

------------------------------------------
**개발 환경 설정 방법(구동시 필요 자료)** <br/>
src/main/resources 아래 data-source.properties 파일 필요. </br>
 - datasource.driver
 - datasource.url
 - datasource.username
 - datasource.password

**키값으로 로컬디비를 만들어서 쓰셔야합니다.**
<br/><br/>

src - main - webapp - WEB-INF - lib 아래</br>
 - ojdbc6.jar
 - spring-security-taglibs-5.1.4.RELEASE.jar
 - spring-webmvc-5.1.5RELEASE.jar
 - taglib-standard-compat-1.2.5.jar
 - taglib-standard-impl-1.2.5.jar
 - taglib-standard-jstlel-1.2.5.jar
 - taglib-standard-spec-1.2.5.jar

파일을 넣어야 합니다.

------------------------------------------
**개발 과정**
<br/><br/>

2021.05.10
* 레포지토리 생성 
* 프로젝트 생성
<br/>

2021.05.11
* sql 추가  
* contributors 깃 세팅
<br/>

2021.05.12
* spring-security 세팅
* 로그인 기능 추가
* 사용자 권한별 메뉴 분기처리 (회원, 강사, 관리자)
* 로그아웃 기능
* 리멤버미 
* 강의 등록 페이지 추가
<br/>

2021.05.13
* 강사 신청 기능
* 일반 회원가입 기능
* 강의 관리
<br/>

2021.05.17
* 회원관리, 강사관리 페이지 구현
* 강의 신청 1차 완성
* 강의 목록 페이지 1차 완성
<br/>

2021.05.18
* 강의 관리 페이지 완성
* 강의신청 목록 완성
* 강사신청 목록 완성
<br/>

2021.05.21
* 강의목록 페이지 완성(평점 미완)
* 공지사항, 공부게시판 페이지 완성 (기능까지)
* 강사, 유저 프로필 페이지 프레임 완성 (내부 아직)
<br/>

2021.05.21
* 강의목록 페이지 최종 완성
* 사용자 대시보드, 강의자 대시보드 페이지 구현
<br/>

2021.05.23
* 1대1 문의 및 답변 완성
* 강의상세페이지 구매전 완성 (후기CSS 미완)
* 찜 및 장바구니 기능 구현 (업데이트 필요)
<br/>

2021.05.24
* 결제기능 및 db 처리 구현
<br/>

2021.05.26
* (메인페이지) 인기 조회 강의 키워드 롤링페이지
<br/>

2021.05.27
* 알림기능 완료 (토스트 알림, 벨표시)
<br/>

2021.05.29
* 영상재생화면 완료
<br/>

2021.05.30
* 일반강의 등록 완료
* 스트리밍강의 등록 fullcalendar까지 완료 (DB등록 남음)
* 결제 디비처리, 수강진도 디비 완료
* DB FK 제약조건 중 delete set null인 컬럼들 nullable로 수정
<br/>

2021.06.01
* 스트리밍 강의 등록 완료 (강의등록 페이지 완성)
<br/>

2021.06.03
* 다국어 처리 기능 구현
<br/>

2021.06.04
* 데이터 현황 페이지 구현
* 인터셉터 적용
<br/>

## [자바 넥스트스텝 책 정리](http://www.yes24.com/Product/Goods/31869154)

# 3장 실습을 위한 개발 환경 세팅

* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트

* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리

* 구현 단계에서는 각 요구사항을 구현하는데 집중한다.
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다.

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답

* 스트림 [정리](https://www.notion.so/dongguridong/TIL-6ce5d06c0cb347d9bfb8cee0c28b30e5)

- [x] InputStream을 한 줄 단위로 읽기 위해 BufferedReader를 생성
- [x] HHttp 요청 정보의 첫 번쨰 라인에서 요청 URL을 추출
- [x] 요청 Url에 해당하는 파일을 webapp 디렉토리에 읽어 전달

### 요구사항 2 - get 방식으로 회원가입

- [x] Http 요청의 첫 번째 라인에서 요청 URL을 추출한다.
- [x] 요청 URL에서 접근 경로와 이름=값으로 전달되는 데이터를 추출해 User 클래스에 담는다.

### 요구사항 3 - post 방식으로 회원가입

- [x] 회원가입시 입력한 모든 데이터를 추출해 User 객체를 생성한다.

### 요구사항 4 - redirect 방식으로 이동

- [x] 회원가입을 완료하면 /index.html 페이지로 이동.

### 요구사항 5 - 로그인하기

- [X] 로그인이 성공하면 /index.html로 이동
- [x] 로그인이 실패하면 /user/login_failed.html로 이동
- [x] 쿠키를 사용해 로그인 상태 유지
    - [x] 로그인이 성공할 경우 요청 헤더의 Cookie 헤더 값이 logined=true
    - [x] 로그인이 실패할 경우 요청 헤더의 Cookie 헤더 값이 logined=false

### 요구사항 6 - 사용자 목록 출력

- [X] 접근하고 있는 사용자가 "로그인" 상태(Cookie 값이 logined=true)일 경우 사용자 목록 출력

### 요구사항 7 - stylesheet 적용

*

### heroku 서버에 배포 후

* 
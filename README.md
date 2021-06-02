## [자바 넥스트스텝 책 정리](http://www.yes24.com/Product/Goods/31869154)

### Session 기능 구현 (HTTPSESSION)
- [X] 고유한 세션 아이디 반환 getId()
- [x] 전달되는 객체를 저장 setAttribute(String name, Object valeu)
- [x] 세션에 객체 값을 삭제 removeAttribute(String name)
- [x] 세션에 모든 값 삭제 invalidate()

### 쿠키 관리 클래스 따로 분리
- [x] 쿠키 클래스 생성 HttpCookie

### 쿠키 사용 기능 세션 기능으로 변경
- [X] 최초 접근시 JsessionId 발급
- [X] 기존 로직 메소드 쿠키 -> 세션으로 변경


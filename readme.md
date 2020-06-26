#카카오페이 뿌리기 기능 구현하기
1. 구성
   - 기능 구현 기술: Spring Boot, MySQL, Redis
   - 테스트 구현 기술: Spock
   - 도메인 부분과 인터페이스 부분의 2개 레이어를 모듈로 나눠서 개발하였습니다.
   - local환경에 맞춰서 세팅되어 있습니다. flyway로 간단하게 테이블 생성까지 할 수 있게 해 놓았습니다.

2. API 구현
   - 뿌리기
      - POST /distribution
      - 토큰 생성: java.util.Random 을 이용해서 영문 대소문자, 숫자로 이루어져 있는 3자리 문자열을 생성하도록 개발했습니다.
      - 저장: 요청정보를 distribution_requests 테이블에 저장하게 되어있습니다.
      - 분배: java.util.Random 을 이용해서 입력 받은 금액을 분배하여 배열에 넣는 로직을 작성하였습니다. 금액을 최소 1이상은 받을 수 있도록 범위를 지정했습니다. 생성된 배열은 토큰과 대화방 ID를 이용해 Redis에 10분후 만료되는 Set의 형태로 저장하였습니다. 
   
   - 받기
      - POST /receive/{token}
      - 분배된 금액 찾기: 토큰과 대화방 ID를 이용해 Redis에 저장된 Set에서 값 하나를 가져옵니다. spop을 이용하면 임의의 원소를 하나 얻을 수 있지만 Redis는 롤백이 되는 구조가 아니기 때문에 srandmember로 금액을 찾아 DB 작업을 한 뒤 제거하는 식으로 개발했습니다.
      - 저장: 찾아온 금액과 정보들을 이용하여 분배 결과 정보를 distribution_results 테이블에 저장합니다.
   
   - 조회
      - GET /status/{token}
      - 조회: 토큰과 대화방 ID를 이용해서 DB에서 뿌리기 요청 정보와 분배 결과 정보를 꺼내서 보여줍니다.
      
3. 그 외 구현
   - Interceptor에 request header에서 유저ID와 대화방ID를 꺼내 ThreadLocal에 해당 정보를 저장하는 로직을 작성했습니다.
   - ResponseDTO를 만들어서 api의 성공여부와 결과값, 에러메시지를 노출하게 하였습니다. ControllerAdvice에서 Exception 발생시 ResponseDTO를 실패로 내리고 메시지를 넣게 되어 있습니다. 
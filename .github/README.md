## COCOMU Back-End

### 💻 MVP

> MVP 개발 기간은 아래와 같습니다.

* 25.02.24 ~ 25.03.17

> MVP 개발 기간 동안 작업한 핵심 기능은 아래와 같습니다.

* CSR OAuth(Kakao, GitHub, Google) Login API
* Study CRUD API - 동적 쿼리 조회
* Coding Space CRUD API - 동적 쿼리 조회
* Code Execution/Submission API
* STOMP 기반 SSE 알림 서비스 구현
* Docker Sandbox 기반 Code Executor 구현
* Rabbit MQ 기반 API 서버와 Code Executor 간 통신 시스템 구축
* API 도메인 별 단위 테스트 작성

> MVP 기간 아래와 같이 DevOps 환경을 적용했습니다.

* S3 Storage를 통한 File Manage
* Front Web & Contents File CDN 적용
* Three Tier Subnet 구조를 통한 Infra 보안 적용
* ACM + Route 53을 활용한 SSL 적용
* Multiple AZ를 활용한 고가용성 인프라 적용
* Load Balancer를 활용한 도메인 기반 라우팅 적용
* Jenkins를 활용한 CI/CD 파이프라인 구축 및 자동화 적용
* JaCoCo를 통한 테스트 커버리지 측정 및 시각화 적용
* Discord 웹훅을 활용한 빌드 및 배포 결과 자동 알림 적용

--- 

### Tech Stack
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![RABBIT-MQ](https://img.shields.io/badge/-RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-0088cc?style=for-the-badge&logo=graphql)
![WebSocket](https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=socket.io)
![AWS](https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazonwebservices&logoColor=white)
![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white)

### 기술 문서




### Docs

[협업 강령](CONTRIBUTING.md)
[작업 내역](../docs/CHANGE_LOG.md)

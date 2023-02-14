# 11팀 인프라 설정 정리

## 사용 툴
- Nginx 
    - 백 프론트 분산 서버를 사용하기 위해 Apache보다 분산서버 에 특화된 Nginx를 사용
    - 보안을 위해 letsencrypt를 사용하여 암호화 통신 사용
- Docker
    - 각 아키텍처 들을 하나의 Host에 효율적으로 관리하기 위해 사용
- Docker Compose
    - 여러 Docker Container들을 보다 손쉽게 관리하기 위해 사용 
- Jenkins (Pipeline)
    - 설정이 조금 귀찮지만 비용이 들어가지 않고 CI/CD 를 구축하여 보다 간편하고, 끊임없는 배포를 위해 사용
    - 무중단 배포 전략은 Blue/Green 전략을 사용
- Redis
    - 메모리 저장소를 사용하는 특성을 이용해 사용자가 매번, 자주 해야하는 인증 관련된 Refresh Token,Access Token을 관리하기 위해 사용
- React
- Spring Boot
- AWS EC2
- MySql
---

## 주요 사용 명령어


```
docker ps //현재 실행중인 docker의 목록을 보여준다
```

```
docker images //현재 만들어진 이미지들을 보여준다
```

```
docker container ls // 현재 만들어진 컨테이너 들을 보여준다
```

```
docker-compose up // docker-compose.yml에 설정된 이미지들을 빌드,실행 시켜준다
```

```
docker logs [Container ID | Container Name] //지정한 도커의 로그를 보여준다
```

## Nginx 설정
- ### /etc/nginx/conf.d/application.conf
```Linux
server {
   listen 80;
   server_name i8b111.p.ssafy.io www.i8b111.p.ssafy.io;

    location / {
        return 301 https://i8b111.p.ssafy.io$request_uri;
    }
}


server {
   listen 443 ssl http2;
   # listen [::]:80;
   access_log off;

   #server_name localhost
   server_name i8b111.p.ssafy.io www.i8b111.p.ssafy.io;

   ssl_certificate /etc/letsencrypt/live/i8b111.p.ssafy.io/fullchain.pem;
   ssl_certificate_key /etc/letsencrypt/live/i8b111.p.ssafy.io/privkey.pem;

   include /etc/nginx/conf.d/service-url.inc;
   include /etc/nginx/conf.d/react_url.inc;

   location / {
        proxy_pass $react_url;
        proxy_set_header HOST $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
#       allow all;
#       root    /app/build;
#       index   index.html;
#       try_files $uri $uri/ /index.html;
   }
     location /api/ {
        if ($request_uri ~* "/api/(.*)") {
             proxy_pass $service_url/$1;
        }
        proxy_set_header HOST $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
  }
}

```
## Back-End 관련 설정


- ### docker-compose.green.yml
```
version: '3.1'

services:
  api:
    build: /var/lib/jenkins/workspace/miristockpipeline/back
    image: miristock
    container_name: miristock-green
    environment:
         SPRING_DATASOURCE_URL: jdbc:mysql://host.docker.internal:3306/miristockdb?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true
         SPRING_DATASOURCE_USERNAME: miristock
         SPRING_DATASOURCE_PASSWORD: miriSt@ckdb4165!
         SPRING_PROFILES_ACTIVE: green
         SPRING_REDIS_HOST: host.docker.internal
         SPRING_REDIS_PORT: 6379
         SPRING_REDIS_PASSWORD: miriSt@ckredis4365#
    extra_hosts:
      - host.docker.internal:host-gateway
    ports:
      - 8081:8081

networks:
  default:
    external:
      name: service-network

```

- Dockerfile
```
FROM azul/zulu-openjdk:11

ENV SPRING_DATASOURCE_URL jdbc:mysql://mysqldb:3306/miristockdb?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true

ENV SPRING_DATASOURCE_USERNAME miristock

ENV SPRING_DATASOURCE_PASSWORD miriSt@ckdb4165!

ARG JAR_FILE=build/libs/miristock-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

```

- ### HTTPS 관련 설정 [Let’s Encrypt]
1. certbot 설치

```jsx
sudo add-apt-repository ppa:certbot/certbot
sudo apt-get update
sudo apt-get install python-certbot-nginx
```

1. 도메인주소의 인증 키(pem 파일) 생성

```jsx
sudo certbot certonly --nginx -d [Domain Name]
```

1. 인증서 확인

```jsx
/etc/letsencrypt/live/[Domain Name]/fullchain.pem
/etc/letsencrypt/live/[Domain Name]privkey.pem 
이 있는지 확인 후 nginx설정파일에 include
```

---
## Front-End 관련
- ### docker-compose.blue.yml
```
version: "3"
services:
  miristockreact:
    container_name: miristockreact-blue
    build:
      dockerfile: Dockerfile # dockerfile이름
      context: /var/lib/jenkins/workspace/miristockpipelinereact/front # 도커파일 위치 명시
    environment:
      REACT_APP_BASE_UR: api
    extra_hosts:
      - host.docker.internal:host-gateway
    ports:
      - 3001:3000 # port 맵핑
    stdin_open: true
networks:
  default:
    external:
      name: service-network
```


- ### Dockerfile
```
# 가져올 이미지를 정의
FROM node:16.18.0
# package.json 워킹 디렉토리에 복사 (.은 설정한 워킹 디렉토리를 뜻함)
COPY package.json .
# 명령어 실행 (의존성 설치)
RUN npm install
# 현재 디렉토리의 모든 파일을 도커 컨테이너의 워킹 디렉토리에 복사한다.
COPY . .
RUN npm run build
# 각각의 명령어들은 한줄 한줄씩 캐싱되어 실행된다.
# package.json의 내용은 자주 바뀌진 않을 거지만
# 소스 코드는 자주 바뀌는데
# npm install과 COPY . . 를 동시에 수행하면
# 소스 코드가 조금 달라질때도 항상 npm install을 수행해서 리소스가 낭비된다.
# 3000번 포트 노출
EXPOSE 3000
# npm start 스크립트 실행
CMD ["npm", "start"]
```


#!/bin/bash
set -e  # 오류 발생 시 스크립트 중단 (배포 실패를 명확히 하기 위함)

# Docker Hub 로그인 - 비공개 이미지 액세스를 위해 필요
echo "$DOCKER_HUB_PAT" | docker login -u "$DOCKER_USERNAME" --password-stdin

# 애플리케이션 디렉토리 생성 및 이동
mkdir -p ~/ongi-app
cd ~/ongi-app

# 롤백을 위한 현재 docker-compose.yml 백업
# (파일이 없는 경우 오류 무시)
cp docker-compose.yml docker-compose.yml.backup 2>/dev/null || echo "No existing docker-compose.yml to backup"

# 최신 이미지 가져오기 및 컨테이너 재시작
# pull: 최신 이미지 가져오기
docker-compose pull
# down: 기존 컨테이너 중지 및 제거 (오류 무시)
docker-compose down || true
# up -d: 컨테이너를 백그라운드에서 시작
docker-compose up -d

echo "애플리케이션 시작 중... 헬스 체크를 시작합니다."

# 애플리케이션 헬스 체크를 위한 설정
MAX_RETRIES=12      # 최대 재시도 횟수
RETRY_INTERVAL=10   # 재시도 간격(초)

# 애플리케이션이 정상 작동할 때까지 반복 확인
for i in $(seq 1 $MAX_RETRIES); do
  echo "헬스 체크 시도 $i/$MAX_RETRIES..."
  # /health 엔드포인트로 상태 확인
if curl -sk https://localhost/health | grep -q "OK"; then
    echo "✅ 배포 성공!"
    break
  else
    # 최대 재시도 횟수 도달 시 롤백
    # shellcheck disable=SC2086
    if [ $i -eq $MAX_RETRIES ]; then
      echo "❌ 최대 재시도 횟수에 도달했습니다. 롤백을 시작합니다."
      # 백업된 설정으로 복원
      cp docker-compose.yml.backup docker-compose.yml
      docker-compose down || true
      docker-compose up -d
      exit 1  # 오류 코드로 종료
    fi
    echo "애플리케이션이 아직 준비되지 않았습니다. ${RETRY_INTERVAL}초 후 다시 시도합니다..."
    sleep $RETRY_INTERVAL
  fi
done

# 디스크 공간 확보를 위한 미사용 Docker 리소스 정리
echo "사용하지 않는 Docker 자원을 정리합니다..."
# 중지된 컨테이너 제거
docker container prune -f
# 7일(168시간) 이상된 사용하지 않는 이미지 제거
docker image prune -a -f --filter "until=168h"
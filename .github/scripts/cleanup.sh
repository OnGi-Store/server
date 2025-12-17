#!/bin/bash
set -e  # 오류 발생 시 스크립트 중단

echo "사용하지 않는 Docker 자원을 정리합니다..."

# 중지된 컨테이너 제거 (실행 중이지 않은 컨테이너 모두 삭제)
docker container prune -f

# 7일(168시간) 이상된 사용하지 않는 이미지 제거
# 이는 최근 7일간 사용되지 않은 모든 이미지를 삭제함
docker image prune -a -f --filter "until=168h"

# 사용하지 않는 볼륨 제거 (어떤 컨테이너에도 연결되지 않은 볼륨)
# 주의: 이 명령은 데이터 손실을 초래할 수 있으므로 신중하게 사용해야 함
docker volume prune -f

echo "정리 완료!"
FROM nginx:1.27-alpine

# Nginx 설정 (프록시 + 정적 서빙 + 헬스)
COPY deploy/nginx.conf /etc/nginx/nginx.conf

# 프론트 정적 파일 복사 (CI에서 npm run build로 dist 생성)
COPY frontend/dist/ /usr/share/nginx/html/

HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -qO- http://127.0.0.1/nginx-health || exit 1

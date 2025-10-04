const getBaseUrl = () => {
  // 1. 환경변수에서 가져오기 (운영환경)
  if (import.meta.env.VITE_API_URL) return import.meta.env.VITE_API_URL;

  // 2. 로컬 개발 환경 자동 감지
  if (window.location.hostname === "localhost") {
    return "http://localhost:8080"; // 백엔드 포트 직접 호출
  }

  // 3. 운영 서버일 경우 현재 origin 사용
  return `${window.location.protocol}//${window.location.host}`;
};

export const API_CONFIG = {
  BASE_URL: getBaseUrl(),
  ENDPOINTS: {
    KAKAO_LOGIN: (provider) => `${getBaseUrl()}/api/v1/oauth/login/${provider}`,
  },
};
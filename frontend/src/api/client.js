import axios from 'axios';

const API_BASE_URL = window.location.hostname === 'http://dailygrowth.shop/api/'  //배포수 수정 예정

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

export default apiClient;
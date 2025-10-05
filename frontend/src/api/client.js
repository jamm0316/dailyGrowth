import axios from 'axios';

export const API_BASE_URL = window.location.hostname === 'localhost'
  ? 'http://localhost:8080'
  : 'http://dailygrowth.shop/api'

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

export default apiClient;

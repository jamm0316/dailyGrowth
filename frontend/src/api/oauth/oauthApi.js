import apiClient from "/src/api/client.js";
import {ENDPOINTS} from "/src/api/oauth/endpoints.js";

export const oauthApi = {
  login: async (provider) => {
    const response = await apiClient.get(ENDPOINTS.LOGIN(provider));
    return response.data;
  },
  me: async () => {
    const response = await apiClient.get(ENDPOINTS.ME);
    return response.data;
  }
}
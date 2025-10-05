// src/hooks/useOAuthLogin.js
import { useCallback, useState } from "react";
import {API_BASE_URL} from "/src/api/client.js";

export function useOAuthLogin({ onSuccess, onError } = {}) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const loginWithProvider = useCallback(
    (provider) => {
      if (loading) return;
      setLoading(true);
      setError(null);

      try {
        const oauthUrl = `${API_BASE_URL}/api/v1/oauth/login/${provider}`
        window.location.href = oauthUrl;
      } catch (e) {
        console.error("OAuth 리다이렉트 실패", e);
        setError(e);
        onError?.(e);
      } finally {
        setLoading(false);
      }
    },
    [loading, onError]
  );

  return { loginWithProvider, loading, error };
}
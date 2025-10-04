// src/hooks/useOAuthLogin.js
import { useCallback, useState } from "react";
import { API_CONFIG } from "/src/data/apiConfig.js";

export function useOAuthLogin({ onSuccess, onError } = {}) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const loginWithProvider = useCallback(
    async (provider) => {
      if (loading) return;
      setLoading(true);
      setError(null);

      try {
        // ✅ context path를 동적으로 붙임
        const loginUrl = `${API_CONFIG.BASE_URL}/api/v1/oauth/login/${provider}`;
        window.location.href = loginUrl;

        onSuccess?.();
      } catch (e) {
        setError(e);
        onError?.(e);
      } finally {
        setLoading(false);
      }
    },
    [loading, onSuccess, onError]
  );

  return { loginWithProvider, loading, error };
}
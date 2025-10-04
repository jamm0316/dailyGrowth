// src/hooks/useOAuthLogin.js
import { useCallback, useState } from "react";
import {ENDPOINTS} from "/src/api/oauth/endpoints.js";

export function useOAuthLogin({ onSuccess, onError } = {}) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const loginWithProvider = useCallback(
    async (provider) => {
      if (loading) return;
      setLoading(true);
      setError(null);

      try {
        window.location.href = ENDPOINTS.LOGIN(provider);
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
import {useCallback, useState} from "react";

export function useOAuthLogin({onSuccess, onError} = {}) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const loginWithProvider = useCallback(async (provider) => {
    if (loading) return; // 중복 클릭 방지
    setLoading(true);
    setError(null);
    try {
      window.location.href = `/api/v1/oauth/login/${provider}`;
      onSuccess?.();
    } catch (e) {
      setError(e);
      onError?.(e);
    } finally {
      setLoading(false);
    }
  }, [loading, onSuccess, onError]);

  return {loginWithProvider, loading, error};
}

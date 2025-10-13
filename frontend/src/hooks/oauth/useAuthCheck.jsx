import { useEffect, useState } from "react";
import { oauthApi } from "/src/api/oauth/oauthApi.js";

export function useAuthCheck() {
  const [state, setState] = useState({ loading: true, ok: false, user: null });

  useEffect(() => {
    const justLoggedIn = sessionStorage.getItem("justLoggedIn");

    if (justLoggedIn) {
      sessionStorage.removeItem("justLoggedIn");
      setState({ loading: false, ok: true });
    } else {
      let canceled = false;
      (async () => {
        try {
          const profile = await oauthApi.me();
          if (!canceled) setState({ loading: false, user: profile.result, ok: true });
        } catch(e) {
          console.log("Auth check failed", e)
          if (!canceled) setState({ loading: false, user: null, ok: false });
        }
      })();
      return () => {
        canceled = true;
      };
    }
  }, []);

  return state;
}
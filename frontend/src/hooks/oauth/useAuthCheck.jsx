import { useEffect, useState } from "react";
import { oauthApi } from "/src/api/oauth/oauthApi.js";

export function useAuthCheck() {
  const [state, setState] = useState({ loading: true, ok: false });

  useEffect(() => {
    const justLoggedIn = sessionStorage.getItem("justLoggedIn");

    if (justLoggedIn) {
      sessionStorage.removeItem("justLoggedIn");
      setState({ loading: false, ok: true });
    } else {
      let canceled = false;
      (async () => {
        try {
          await oauthApi.me();
          if (!canceled) setState({ loading: false, ok: true });
        } catch {
          if (!canceled) setState({ loading: false, ok: false });
        }
      })();
      return () => {
        canceled = true;
      };
    }
  }, []);

  return state;
}

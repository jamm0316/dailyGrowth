import {useEffect} from "react";
import {useNavigate} from "react-router-dom";

export default function OAuthCallback() {
  const navigate = useNavigate();

  useEffect(() => {
    sessionStorage.setItem("justLoggedIn", "true");

    navigate("/dashboard", {replace: true});
  }, [navigate]);
  return <div>로그인 처리 중...</div>;
}
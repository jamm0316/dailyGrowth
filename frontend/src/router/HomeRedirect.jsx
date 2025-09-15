import {Navigate} from "react-router-dom";
import {useAuthCheck} from "/src/hooks/oauth/useAuthCheck.jsx";

export default function HomeRedirect() {
  const { loading, ok } = useAuthCheck();

  if (loading) return <div>로딩 중...</div>;
  return ok ? <Navigate to="/dashboard" replace /> : <Navigate to="/login" replace />;
}
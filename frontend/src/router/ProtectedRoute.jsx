// JavaScript
import {Navigate, Outlet} from "react-router-dom";
import {useAuthCheck} from "/src/hooks/oauth/useAuthCheck.jsx";

export default function ProtectedRoute() {
  const { loading, ok } = useAuthCheck();

  if (loading) return <div>로딩 중...</div>;
  return ok ? <Outlet /> : <Navigate to="/login" replace />;
}
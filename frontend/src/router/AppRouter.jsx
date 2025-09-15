import React from 'react';
import Dashboard from "/src/pages/Dashboard.jsx";
import ProjectCreatePage from "/src/pages/ProjectCreatePage.jsx";
import {Route, Routes,} from "react-router-dom";
import App from "/src/App.jsx";
import {ROUTES} from "/src/router/routes.js";
import TaskCreatePage from "/src/pages/TaskCreatePage.jsx";
import LoginPage from "/src/pages/auth/LoginPage.jsx";
import HomeRedirect from "/src/router/HomeRedirect.jsx";
import OAuthCallback from "/src/pages/auth/OAuthCallback.jsx";
import ProtectedRoute from "/src/router/ProtectedRoute.jsx";

const AppRouter = () => {
  return (
    <Routes>
      <Route element={<App/>}>
        {/* 루트에서 로그인 상태에 따라 분기 */}
        <Route path={ROUTES.ROOT} element={<HomeRedirect/>}/>

        {/* 로그인 페이지 */}
        <Route path={ROUTES.AUTH.LOGIN} element={<LoginPage/>}/>

        {/* OAuth 콜백 페이지 */}
        <Route path={ROUTES.AUTH.CALLBACK} element={<OAuthCallback/>}/>

        {/* 보호 라우트(로그인 필요) */}
        <Route element={<ProtectedRoute/>}>
          <Route path={ROUTES.DASHBOARD} element={<Dashboard/>}/>
          <Route path={ROUTES.PROJECT.CREATE} element={<ProjectCreatePage/>}/>
          <Route path={ROUTES.TASK.CREATE} element={<TaskCreatePage/>}/>
        </Route>
      </Route>
    </Routes>
  );
};

export default AppRouter;
import React from 'react';
import Dashboard from "/src/pages/Dashboard.jsx";
import ProjectCreatePage from "/src/pages/ProjectCreatePage.jsx";
import {Route, Routes,} from "react-router-dom";
import App from "/src/App.jsx";
import {ROUTES} from "/src/router/routes.js";
import TaskCreatePage from "/src/pages/TaskCreatePage.jsx";
import LoginPage from "/src/pages/LoginPage.jsx";

const AppRouter = () => {
  return (
    <Routes>
      <Route element={<App/>}>
        <Route path={ROUTES.ROOT} element={<LoginPage/>}/>
        {/*<Route path={ROUTES} element={<Dashboard/>}/>*/}
        <Route path={ROUTES.PROJECT.CREATE} element={<ProjectCreatePage />}/>
        <Route path={ROUTES.TASK.CREATE} element={<TaskCreatePage />}/>
      </Route>
    </Routes>
  );
};

export default AppRouter;
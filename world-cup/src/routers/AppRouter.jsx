import { useMemo } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';

import MainLayout from '../layouts/MainLayout';
import Home from '../Features/Home/Home';
import { useAuth } from '../context/AuthContext';
import Login from '../Features/shared/Login';
import { config } from '../config/config';
import { MainTabs, defaultByRole } from '../utils/MainTabs';

function AppRouter() {
  const { currentUser, isAuthenticated } = useAuth();
  const role = isAuthenticated ? (currentUser?.role ?? null) : null;

  const tabs = useMemo(() => getTabsByRole(role), [role]);
  const defaultRoute = useMemo(
    () => (isAuthenticated ? (defaultByRole.get(role) ?? '/home') : '/home'),
    [role, isAuthenticated],
  );

  return (
    <Routes>
      <Route path="/" element={<MainLayout tabs={tabs} />}>
        <Route index element={<Navigate to={defaultRoute} replace />} />
        <Route path="login" element={config.login ? <Login /> : <Home />} />
        {tabs.map((tab) => (
          <Route path={tab.target} element={tab.component} key={tab.id} />
        ))}
      </Route>

      <Route path="*" element={<Navigate to={defaultRoute} replace />} />
    </Routes>
  );
}

function getTabsByRole(role) {
  return MainTabs.filter((tab) => tab.roles.includes(role) || tab.public);
}

export default AppRouter;

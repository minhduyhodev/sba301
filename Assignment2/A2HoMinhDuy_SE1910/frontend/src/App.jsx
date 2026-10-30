import React, { useState } from 'react';
import { AuthProvider, useAuth } from './context/AuthContext';
import { Navbar } from './components/Navbar';
import { PublicNewsPage } from './pages/PublicNewsPage';
import { LoginPage } from './pages/LoginPage';
import { AdminAccountsPage } from './pages/AdminAccountsPage';
import { StaffCategoriesPage } from './pages/StaffCategoriesPage';
import { StaffNewsPage } from './pages/StaffNewsPage';
import { StaffHistoryPage } from './pages/StaffHistoryPage';
import { StaffProfilePage } from './pages/StaffProfilePage';

const MainContent = () => {
  const { user, isAdmin, isStaff } = useAuth();
  const [currentTab, setCurrentTab] = useState('public-news');

  const handleLoginSuccess = (userData) => {
    if (userData.accountRole === 1) {
      setCurrentTab('accounts');
    } else {
      setCurrentTab('news-mgmt');
    }
  };

  const renderContent = () => {
    switch (currentTab) {
      case 'login':
        return <LoginPage onLoginSuccess={handleLoginSuccess} />;

      case 'accounts':
        if (!isAdmin) {
          return (
            <div className="page-container">
              <div className="alert alert-danger">
                Access Denied: Only Admin (Role = 1) can view Account Management.
              </div>
            </div>
          );
        }
        return <AdminAccountsPage />;

      case 'categories':
        if (!isStaff) {
          return (
            <div className="page-container">
              <div className="alert alert-danger">
                Access Denied: Only Staff (Role = 2) can view Category Management.
              </div>
            </div>
          );
        }
        return <StaffCategoriesPage />;

      case 'news-mgmt':
        if (!isStaff) {
          return (
            <div className="page-container">
              <div className="alert alert-danger">
                Access Denied: Only Staff (Role = 2) can view News Article Management.
              </div>
            </div>
          );
        }
        return <StaffNewsPage />;

      case 'history':
        if (!isStaff) {
          return (
            <div className="page-container">
              <div className="alert alert-danger">
                Access Denied: Only Staff (Role = 2) can view News History.
              </div>
            </div>
          );
        }
        return <StaffHistoryPage />;

      case 'profile':
        if (!user) {
          return <LoginPage onLoginSuccess={handleLoginSuccess} />;
        }
        return <StaffProfilePage />;

      case 'public-news':
      default:
        return <PublicNewsPage />;
    }
  };

  return (
    <div className="app-layout">
      <Navbar currentTab={currentTab} onSelectTab={setCurrentTab} />
      <main className="main-content">
        {renderContent()}
      </main>
      <footer className="app-footer">
        <p>
          FUNews Management System &bull; SBA301 Assignment 02 &bull; Student: <strong>Ho Minh Duy</strong> (CE190378 - SE1910)
        </p>
      </footer>
    </div>
  );
};

export const App = () => {
  return (
    <AuthProvider>
      <MainContent />
    </AuthProvider>
  );
};

export default App;

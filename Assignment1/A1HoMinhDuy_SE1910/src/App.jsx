import React, { useState } from 'react';
import Header from './components/Header.jsx';
import Sidebar from './components/Sidebar.jsx';
import LoginPage from './pages/LoginPage.jsx';
import DashboardPage from './pages/DashboardPage.jsx';
import CategoryManagement from './pages/CategoryManagement.jsx';
import NewsManagement from './pages/NewsManagement.jsx';
import UserManagement from './pages/UserManagement.jsx';
import SettingsPage from './pages/SettingsPage.jsx';
import { createSeedData } from './data/seed.js';
import { loadData, persistData, saveRecord, deleteRecord } from './services/newsService.js';

export default function App() {
  const [currentUser, setCurrentUser] = useState(null);
  const [activeTab, setActiveTab] = useState('dashboard');
  const [{ data, warning }, setDataState] = useState(() => loadData(window.localStorage, createSeedData));
  const [globalError, setGlobalError] = useState('');

  function handleLoginSuccess(user) {
    setCurrentUser(user);
    setActiveTab('dashboard');
    setGlobalError('');
  }

  function handleLogout() {
    setCurrentUser(null);
    setActiveTab('dashboard');
    setGlobalError('');
  }

  function handleSaveRecord(entity, values, id) {
    try {
      const nextData = saveRecord(data, entity, values, id, currentUser?.id || 'user-1');
      persistData(window.localStorage, nextData);
      setDataState((prev) => ({ ...prev, data: nextData }));
      setGlobalError('');
    } catch (err) {
      setGlobalError(err.message || 'Operation failed.');
      throw err;
    }
  }

  function handleDeleteRecord(entity, id) {
    try {
      const nextData = deleteRecord(data, entity, id);
      persistData(window.localStorage, nextData);
      setDataState((prev) => ({ ...prev, data: nextData }));
      setGlobalError('');
    } catch (err) {
      setGlobalError(err.message || 'Deletion failed.');
      throw err;
    }
  }

  function handleResetData() {
    const freshSeed = createSeedData();
    persistData(window.localStorage, freshSeed);
    setDataState({ data: freshSeed, warning: '' });
    setGlobalError('');
  }

  // Auth Guard: If not logged in, render LoginPage
  if (!currentUser) {
    return <LoginPage onLoginSuccess={handleLoginSuccess} />;
  }

  return (
    <div className="app-container">
      <Header currentUser={currentUser} onLogout={handleLogout} />

      <div className="admin-layout">
        <Sidebar activeTab={activeTab} onSelectTab={(tab) => { setActiveTab(tab); setGlobalError(''); }} />

        <main className="admin-content">
          {warning && (
            <div className="alert alert-warning" role="alert">
              {warning}
            </div>
          )}

          {globalError && (
            <div className="alert alert-danger" role="alert">
              {globalError}
            </div>
          )}

          {activeTab === 'dashboard' && <DashboardPage data={data} />}

          {activeTab === 'categories' && (
            <CategoryManagement
              categories={data.categories}
              onSaveRecord={handleSaveRecord}
              onDeleteRecord={handleDeleteRecord}
            />
          )}

          {activeTab === 'news' && (
            <NewsManagement
              news={data.news}
              categories={data.categories}
              users={data.users}
              onSaveRecord={handleSaveRecord}
              onDeleteRecord={handleDeleteRecord}
            />
          )}

          {activeTab === 'users' && (
            <UserManagement
              users={data.users}
              onSaveRecord={handleSaveRecord}
              onDeleteRecord={handleDeleteRecord}
            />
          )}

          {activeTab === 'settings' && (
            <SettingsPage
              currentUser={currentUser}
              data={data}
              onResetData={handleResetData}
            />
          )}
        </main>
      </div>

      <footer className="app-footer">
        FUNewsManagementSystem &bull; Assignment 01 &bull; Single Page Application
      </footer>
    </div>
  );
}

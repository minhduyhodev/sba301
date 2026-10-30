import React from 'react';
import { useAuth } from '../context/AuthContext';

export const Navbar = ({ currentTab, onSelectTab }) => {
  const { user, logout, isAdmin, isStaff } = useAuth();

  return (
    <header className="app-navbar">
      <div className="navbar-brand" onClick={() => onSelectTab('public-news')}>
        <span className="brand-logo">📰</span>
        <div className="brand-text">
          <span className="brand-title">FUNews</span>
          <span className="brand-subtitle">Management System</span>
        </div>
      </div>

      <nav className="navbar-links">
        <button 
          className={`nav-btn ${currentTab === 'public-news' ? 'active' : ''}`}
          onClick={() => onSelectTab('public-news')}
        >
          🌐 Public News
        </button>

        {/* Admin Navigation */}
        {isAdmin && (
          <button 
            className={`nav-btn ${currentTab === 'accounts' ? 'active' : ''}`}
            onClick={() => onSelectTab('accounts')}
          >
            👥 Accounts (Admin)
          </button>
        )}

        {/* Staff Navigation */}
        {isStaff && (
          <>
            <button 
              className={`nav-btn ${currentTab === 'news-mgmt' ? 'active' : ''}`}
              onClick={() => onSelectTab('news-mgmt')}
            >
              📑 News Articles
            </button>
            <button 
              className={`nav-btn ${currentTab === 'categories' ? 'active' : ''}`}
              onClick={() => onSelectTab('categories')}
            >
              📂 Categories
            </button>
            <button 
              className={`nav-btn ${currentTab === 'history' ? 'active' : ''}`}
              onClick={() => onSelectTab('history')}
            >
              🕒 My News History
            </button>
            <button 
              className={`nav-btn ${currentTab === 'profile' ? 'active' : ''}`}
              onClick={() => onSelectTab('profile')}
            >
              👤 Profile
            </button>
          </>
        )}
      </nav>

      <div className="navbar-user-section">
        {user ? (
          <div className="user-info-box">
            <div className="user-details">
              <span className="user-name">{user.accountName}</span>
              <span className={`role-badge ${isAdmin ? 'badge-admin' : 'badge-staff'}`}>
                {isAdmin ? 'ADMIN' : 'STAFF'}
              </span>
            </div>
            <button className="btn btn-outline-danger btn-sm" onClick={logout}>
              Logout
            </button>
          </div>
        ) : (
          <button 
            className={`btn btn-primary ${currentTab === 'login' ? 'active' : ''}`}
            onClick={() => onSelectTab('login')}
          >
            Sign In
          </button>
        )}
      </div>
    </header>
  );
};

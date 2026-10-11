import React from 'react';

export default function Header({ currentUser, onLogout }) {
  return (
    <header className="app-header">
      <div className="header-left">
        <img src="/assets/logo.jpg" alt="FUNews Logo" className="header-logo" />
        <span className="header-title">FUNewsManagementSystem</span>
      </div>
      <div className="header-right">
        <span className="header-user-info">
          Logged in as: <span className="header-user-name">{currentUser?.username || 'Admin'}</span>
        </span>
        <button type="button" className="btn btn-outline btn-sm" onClick={onLogout}>
          Logout
        </button>
      </div>
    </header>
  );
}

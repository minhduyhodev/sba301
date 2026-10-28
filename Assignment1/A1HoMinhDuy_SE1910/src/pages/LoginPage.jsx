import React, { useState } from 'react';
import { validateLogin } from '../services/newsService.js';

export default function LoginPage({ onLoginSuccess }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  function handleSubmit(e) {
    e.preventDefault();
    const error = validateLogin(username, password);
    if (error) {
      setErrorMessage(error);
      return;
    }

    setErrorMessage('');
    onLoginSuccess({
      id: 'user-1',
      username: 'Admin',
      role: 1,
    });
  }

  return (
    <div className="login-wrapper">
      <div className="login-card">
        <div className="login-header">
          <img src="/assets/logo.jpg" alt="FUNews Logo" className="login-logo" />
          <h1 className="login-title">FUNewsManagementSystem</h1>
          <p className="login-subtitle">Sign in to your administration account</p>
        </div>

        {errorMessage && (
          <div className="alert alert-danger" role="alert">
            {errorMessage}
          </div>
        )}

        <form onSubmit={handleSubmit} noValidate>
          <div className="form-group">
            <label className="form-label" htmlFor="login-username">
              Username
            </label>
            <input
              id="login-username"
              type="text"
              className="form-input"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter username"
              autoComplete="username"
              autoFocus
            />
          </div>

          <div className="form-group">
            <label className="form-label" htmlFor="login-password">
              Password
            </label>
            <input
              id="login-password"
              type="password"
              className="form-input"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter password"
              autoComplete="current-password"
            />
          </div>

          <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '8px' }}>
            Sign In
          </button>

          <div className="login-hint">
            <strong>Default Administrator Account:</strong>
            <div>Username: <code>Admin</code></div>
            <div>Password: <code>Admin</code></div>
          </div>
        </form>
      </div>
    </div>
  );
}

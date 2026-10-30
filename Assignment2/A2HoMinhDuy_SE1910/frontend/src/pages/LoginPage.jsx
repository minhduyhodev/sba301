import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { authApi } from '../api/authApi';

export const LoginPage = ({ onLoginSuccess }) => {
  const { login } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const userData = await authApi.login({ email, password });
      login(userData);
      onLoginSuccess(userData);
    } catch (err) {
      const msg = err.response?.data?.message || 'Invalid email or password';
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  const fillCredentials = (demoEmail, demoPass) => {
    setEmail(demoEmail);
    setPassword(demoPass);
    setError(null);
  };

  return (
    <div className="login-page-container">
      <div className="login-card">
        <div className="login-header">
          <h2>System Authentication</h2>
          <p>Please enter your credentials to access management functions.</p>
        </div>

        {error && <div className="alert alert-danger">{error}</div>}

        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label className="form-label">Email Address</label>
            <input 
              type="email" 
              className="form-control" 
              required
              placeholder="e.g. admin@fpt.edu.vn"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label className="form-label">Password</label>
            <input 
              type="password" 
              className="form-control" 
              required
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <button 
            type="submit" 
            className="btn btn-primary btn-block"
            disabled={loading}
          >
            {loading ? 'Authenticating...' : 'Sign In'}
          </button>
        </form>

        <div className="demo-accounts-box">
          <span className="demo-title">Quick Demo Login (Click to fill):</span>
          <div className="demo-buttons">
            <button 
              type="button" 
              className="btn btn-outline-primary btn-sm"
              onClick={() => fillCredentials('admin@fpt.edu.vn', 'admin123')}
            >
              Fill Admin (Role = 1)
            </button>
            <button 
              type="button" 
              className="btn btn-outline-secondary btn-sm"
              onClick={() => fillCredentials('staff@fpt.edu.vn', 'staff123')}
            >
              Fill Staff (Role = 2)
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

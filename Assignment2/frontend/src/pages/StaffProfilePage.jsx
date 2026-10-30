import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { profileApi } from '../api/profileApi';

export const StaffProfilePage = () => {
  const { user, updateUser } = useAuth();
  const [accountName, setAccountName] = useState(user?.accountName || '');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [alertMsg, setAlertMsg] = useState(null);
  const [errorMsg, setErrorMsg] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setAlertMsg(null);
    setErrorMsg(null);

    if (newPassword && newPassword !== confirmPassword) {
      setErrorMsg('New password and confirm password do not match.');
      return;
    }

    setLoading(true);
    try {
      const updated = await profileApi.update(user.accountId, {
        accountName: accountName.trim(),
        newPassword: newPassword ? newPassword.trim() : null,
      });

      updateUser({ accountName: updated.accountName });
      setAlertMsg('Profile updated successfully!');
      setNewPassword('');
      setConfirmPassword('');
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Failed to update profile.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-container" style={{ maxWidth: '650px' }}>
      <div className="page-header">
        <div>
          <h1 className="page-title">Personal Profile</h1>
          <p className="page-description">
            View and manage your account details and password.
          </p>
        </div>
      </div>

      {alertMsg && <div className="alert alert-success">{alertMsg}</div>}
      {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}

      <div className="profile-card">
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Account ID</label>
            <input 
              type="text" 
              className="form-control" 
              disabled 
              value={`#${user?.accountId}`}
            />
          </div>

          <div className="form-group">
            <label className="form-label">Email Address (Read-only)</label>
            <input 
              type="email" 
              className="form-control" 
              disabled 
              value={user?.accountEmail || ''}
            />
          </div>

          <div className="form-group">
            <label className="form-label">Account Role</label>
            <input 
              type="text" 
              className="form-control" 
              disabled 
              value={user?.accountRole === 1 ? 'Administrator (Role 1)' : 'Staff Member (Role 2)'}
            />
          </div>

          <div className="form-group">
            <label className="form-label">Full Name *</label>
            <input 
              type="text" 
              className="form-control" 
              required
              value={accountName}
              onChange={(e) => setAccountName(e.target.value)}
            />
          </div>

          <hr style={{ margin: '24px 0', borderColor: 'var(--border-color)' }} />
          <h4 style={{ marginBottom: '12px' }}>Change Password (Optional)</h4>

          <div className="form-group">
            <label className="form-label">New Password</label>
            <input 
              type="password" 
              className="form-control" 
              placeholder="Leave blank to keep current password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
            />
          </div>

          {newPassword && (
            <div className="form-group">
              <label className="form-label">Confirm New Password *</label>
              <input 
                type="password" 
                className="form-control" 
                required={!!newPassword}
                placeholder="Re-type new password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
              />
            </div>
          )}

          <div className="form-actions" style={{ marginTop: '24px' }}>
            <button 
              type="submit" 
              className="btn btn-primary btn-block"
              disabled={loading}
            >
              {loading ? 'Saving...' : 'Update Profile'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

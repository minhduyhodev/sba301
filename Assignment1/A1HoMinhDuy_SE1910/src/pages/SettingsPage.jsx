import React, { useState } from 'react';
import ConfirmDialog from '../components/ConfirmDialog.jsx';
import { DATA_KEY } from '../services/newsService.js';

export default function SettingsPage({ currentUser, data, onResetData }) {
  const [showResetConfirm, setShowResetConfirm] = useState(false);
  const [message, setMessage] = useState('');

  function handleConfirmReset() {
    onResetData();
    setShowResetConfirm(false);
    setMessage('Sample data has been reloaded successfully.');
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <h1 className="page-title">System Settings & Profile</h1>
      </div>

      {message && (
        <div className="alert alert-success" role="alert">
          {message}
        </div>
      )}

      <div className="settings-section">
        <h2 className="settings-section-title">Current Profile Information</h2>
        <table className="settings-table">
          <tbody>
            <tr>
              <td>Username:</td>
              <td>{currentUser?.username || 'Admin'}</td>
            </tr>
            <tr>
              <td>Role:</td>
              <td>Administrator (Role ID: {currentUser?.role || 1})</td>
            </tr>
            <tr>
              <td>System:</td>
              <td>FUNewsManagementSystem</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div className="settings-section">
        <h2 className="settings-section-title">Data Storage Information</h2>
        <table className="settings-table">
          <tbody>
            <tr>
              <td>Storage Engine:</td>
              <td>Browser localStorage</td>
            </tr>
            <tr>
              <td>Storage Key:</td>
              <td><code>{DATA_KEY}</code></td>
            </tr>
            <tr>
              <td>Total Categories:</td>
              <td>{data?.categories?.length || 0}</td>
            </tr>
            <tr>
              <td>Total News Articles:</td>
              <td>{data?.news?.length || 0}</td>
            </tr>
            <tr>
              <td>Total Accounts:</td>
              <td>{data?.users?.length || 0}</td>
            </tr>
          </tbody>
        </table>

        <div style={{ marginTop: '16px' }}>
          <button
            type="button"
            className="btn btn-outline"
            onClick={() => setShowResetConfirm(true)}
          >
            Reset to Sample Seed Data
          </button>
        </div>
      </div>

      <ConfirmDialog
        isOpen={showResetConfirm}
        title="Confirm Reset to Seed Data"
        message="Are you sure you want to restore the default sample data? Any newly created categories, news, or users will be overwritten."
        confirmLabel="Confirm Reset"
        onConfirm={handleConfirmReset}
        onCancel={() => setShowResetConfirm(false)}
      />
    </div>
  );
}

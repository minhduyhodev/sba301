import React, { useState, useEffect } from 'react';
import { accountApi } from '../api/accountApi';
import { Modal } from '../components/Modal';
import { ConfirmDialog } from '../components/ConfirmDialog';

export const AdminAccountsPage = () => {
  const [accounts, setAccounts] = useState([]);
  const [keyword, setKeyword] = useState('');
  const [loading, setLoading] = useState(false);
  const [alertMsg, setAlertMsg] = useState(null);
  const [errorMsg, setErrorMsg] = useState(null);

  // Popup Dialog state (Create / Edit)
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingAccount, setEditingAccount] = useState(null);
  const [formData, setFormData] = useState({
    accountName: '',
    accountEmail: '',
    accountRole: 2,
    accountPassword: '',
  });

  // Delete Confirmation state
  const [deleteTarget, setDeleteTarget] = useState(null);

  useEffect(() => {
    fetchAccounts();
  }, []);

  const fetchAccounts = async (searchKw = keyword) => {
    setLoading(true);
    try {
      const data = await accountApi.getAll(searchKw);
      setAccounts(data);
    } catch (err) {
      setErrorMsg('Failed to load accounts. ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    fetchAccounts(keyword);
  };

  const openCreateModal = () => {
    setEditingAccount(null);
    setFormData({
      accountName: '',
      accountEmail: '',
      accountRole: 2,
      accountPassword: '',
    });
    setIsModalOpen(true);
    setErrorMsg(null);
  };

  const openEditModal = (acc) => {
    setEditingAccount(acc);
    setFormData({
      accountName: acc.accountName,
      accountEmail: acc.accountEmail,
      accountRole: acc.accountRole,
      accountPassword: '', // keep blank unless modifying
    });
    setIsModalOpen(true);
    setErrorMsg(null);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setErrorMsg(null);
    try {
      if (editingAccount) {
        await accountApi.update(editingAccount.accountId, formData);
        setAlertMsg(`Account #${editingAccount.accountId} updated successfully.`);
      } else {
        await accountApi.create(formData);
        setAlertMsg('New account created successfully.');
      }
      setIsModalOpen(false);
      fetchAccounts();
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Action failed. Please verify input data.');
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;
    setErrorMsg(null);
    setAlertMsg(null);
    try {
      await accountApi.delete(deleteTarget.accountId);
      setAlertMsg(`Account '${deleteTarget.accountName}' was deleted successfully.`);
      fetchAccounts();
    } catch (err) {
      const message = err.response?.data?.message || 'Failed to delete account.';
      setErrorMsg(`⚠️ Cannot delete account: ${message}`);
    } finally {
      setDeleteTarget(null);
    }
  };

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1 className="page-title">Account Management (Admin)</h1>
          <p className="page-description">
            Create, search, update, and remove system accounts. Accounts with existing news articles cannot be deleted.
          </p>
        </div>
        <button className="btn btn-primary" onClick={openCreateModal}>
          ➕ Add New Account
        </button>
      </div>

      {alertMsg && <div className="alert alert-success">{alertMsg}</div>}
      {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}

      {/* Search Bar */}
      <form className="search-filter-bar" onSubmit={handleSearch}>
        <input 
          type="text" 
          className="form-control" 
          placeholder="Search by account name or email..."
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <button type="submit" className="btn btn-primary">Search</button>
        {keyword && (
          <button 
            type="button" 
            className="btn btn-secondary" 
            onClick={() => {
              setKeyword('');
              fetchAccounts('');
            }}
          >
            Clear
          </button>
        )}
      </form>

      {/* Accounts Table */}
      {loading ? (
        <div className="loading-state">Loading accounts...</div>
      ) : (
        <div className="table-card">
          <table className="data-table">
            <thead>
              <tr>
                <th style={{ width: '80px' }}>ID</th>
                <th>Account Name</th>
                <th>Email</th>
                <th>Role</th>
                <th style={{ width: '160px', textAlign: 'center' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {accounts.length === 0 ? (
                <tr>
                  <td colSpan="5" className="text-center py-4">No accounts found.</td>
                </tr>
              ) : (
                accounts.map((acc) => (
                  <tr key={acc.accountId}>
                    <td>#{acc.accountId}</td>
                    <td><strong>{acc.accountName}</strong></td>
                    <td>{acc.accountEmail}</td>
                    <td>
                      <span className={`role-badge ${acc.accountRole === 1 ? 'badge-admin' : 'badge-staff'}`}>
                        {acc.accountRole === 1 ? 'Admin (1)' : 'Staff (2)'}
                      </span>
                    </td>
                    <td className="text-center">
                      <div className="action-buttons-group">
                        <button 
                          className="btn btn-sm btn-outline-primary"
                          onClick={() => openEditModal(acc)}
                        >
                          Edit
                        </button>
                        <button 
                          className="btn btn-sm btn-outline-danger"
                          onClick={() => setDeleteTarget(acc)}
                        >
                          Delete
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}

      {/* Create / Edit Popup Dialog */}
      <Modal 
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingAccount ? `Edit Account #${editingAccount.accountId}` : 'Add New Account'}
      >
        <form onSubmit={handleSave}>
          <div className="form-group">
            <label className="form-label">Account Name *</label>
            <input 
              type="text" 
              className="form-control" 
              required
              value={formData.accountName}
              onChange={(e) => setFormData({ ...formData, accountName: e.target.value })}
              placeholder="e.g. Nguyen Van A"
            />
          </div>

          <div className="form-group">
            <label className="form-label">Email Address *</label>
            <input 
              type="email" 
              className="form-control" 
              required
              value={formData.accountEmail}
              onChange={(e) => setFormData({ ...formData, accountEmail: e.target.value })}
              placeholder="e.g. staff1@fpt.edu.vn"
            />
          </div>

          <div className="form-group">
            <label className="form-label">Role *</label>
            <select 
              className="form-control"
              value={formData.accountRole}
              onChange={(e) => setFormData({ ...formData, accountRole: Number(e.target.value) })}
            >
              <option value={1}>Admin (Role 1)</option>
              <option value={2}>Staff (Role 2)</option>
            </select>
          </div>

          <div className="form-group">
            <label className="form-label">
              Password {editingAccount ? '(Leave blank to keep unchanged)' : '*'}
            </label>
            <input 
              type="password" 
              className="form-control" 
              required={!editingAccount}
              value={formData.accountPassword}
              onChange={(e) => setFormData({ ...formData, accountPassword: e.target.value })}
              placeholder="••••••••"
            />
          </div>

          <div className="modal-actions">
            <button type="button" className="btn btn-secondary" onClick={() => setIsModalOpen(false)}>
              Cancel
            </button>
            <button type="submit" className="btn btn-primary">
              {editingAccount ? 'Save Changes' : 'Create Account'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Delete Confirmation Dialog */}
      <ConfirmDialog 
        isOpen={!!deleteTarget}
        onClose={() => setDeleteTarget(null)}
        onConfirm={handleDelete}
        title="Confirm Account Deletion"
        message={`Are you sure you want to delete account "${deleteTarget?.accountName}" (${deleteTarget?.accountEmail})? Note: If this account has created any news articles, deletion will be blocked by business rules.`}
      />
    </div>
  );
};

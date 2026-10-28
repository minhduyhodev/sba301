import React, { useState, useEffect } from 'react';
import Modal from './Modal.jsx';

export default function UserDialog({ isOpen, mode, initialData, onSave, onClose }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState(2);
  const [status, setStatus] = useState(1);
  const [error, setError] = useState('');

  useEffect(() => {
    if (isOpen) {
      if (mode === 'update' && initialData) {
        setUsername(initialData.username || '');
        setPassword(''); // Empty to indicate keep current password unless changed
        setRole(initialData.role ?? 2);
        setStatus(initialData.status ?? 1);
      } else {
        setUsername('');
        setPassword('');
        setRole(2);
        setStatus(1);
      }
      setError('');
    }
  }, [isOpen, mode, initialData]);

  function handleSubmit(e) {
    e.preventDefault();
    if (!username.trim()) {
      setError('Username is required.');
      return;
    }
    if (mode === 'create' && !password.trim()) {
      setError('Password is required for new accounts.');
      return;
    }
    try {
      onSave({
        username: username.trim(),
        password: password, // will be resolved in newsService
        role: Number(role),
        status: Number(status),
      });
      setError('');
    } catch (err) {
      setError(err.message || 'Failed to save account.');
    }
  }

  const dialogTitle = mode === 'update' ? 'Edit User' : 'Create User';

  return (
    <Modal isOpen={isOpen} title={dialogTitle} onClose={onClose}>
      <form onSubmit={handleSubmit}>
        <div className="modal-body">
          {error && <div className="alert alert-danger">{error}</div>}
          <div className="form-group">
            <label className="form-label" htmlFor="user-username">
              Username *
            </label>
            <input
              id="user-username"
              type="text"
              className="form-input"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter username"
              autoFocus
            />
          </div>
          <div className="form-group">
            <label className="form-label" htmlFor="user-password">
              Password {mode === 'create' ? '*' : '(leave blank to keep current)'}
            </label>
            <input
              id="user-password"
              type="password"
              className="form-input"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder={mode === 'create' ? 'Enter password' : 'Enter new password or leave blank'}
            />
          </div>
          <div className="form-group">
            <label className="form-label" htmlFor="user-role">
              Role *
            </label>
            <select
              id="user-role"
              className="form-select"
              value={role}
              onChange={(e) => setRole(Number(e.target.value))}
            >
              <option value={1}>Admin</option>
              <option value={2}>Staff</option>
            </select>
          </div>
          <div className="form-group">
            <label className="form-label" htmlFor="user-status">
              Status *
            </label>
            <select
              id="user-status"
              className="form-select"
              value={status}
              onChange={(e) => setStatus(Number(e.target.value))}
            >
              <option value={1}>Active</option>
              <option value={0}>Inactive</option>
            </select>
          </div>
        </div>
        <div className="modal-footer">
          <button type="button" className="btn btn-secondary" onClick={onClose}>
            Cancel
          </button>
          <button type="submit" className="btn btn-primary">
            Save
          </button>
        </div>
      </form>
    </Modal>
  );
}

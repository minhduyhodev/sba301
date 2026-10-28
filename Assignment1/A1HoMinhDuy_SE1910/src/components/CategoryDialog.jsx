import React, { useState, useEffect } from 'react';
import Modal from './Modal.jsx';

export default function CategoryDialog({ isOpen, mode, initialData, onSave, onClose }) {
  const [name, setName] = useState('');
  const [status, setStatus] = useState(1);
  const [error, setError] = useState('');

  useEffect(() => {
    if (isOpen) {
      if (mode === 'update' && initialData) {
        setName(initialData.name || '');
        setStatus(initialData.status ?? 1);
      } else {
        setName('');
        setStatus(1);
      }
      setError('');
    }
  }, [isOpen, mode, initialData]);

  function handleSubmit(e) {
    e.preventDefault();
    if (!name.trim()) {
      setError('Category name is required.');
      return;
    }
    try {
      onSave({
        name: name.trim(),
        status: Number(status),
      });
      setError('');
    } catch (err) {
      setError(err.message || 'Failed to save category.');
    }
  }

  const title = mode === 'update' ? 'Edit Category' : 'Create Category';

  return (
    <Modal isOpen={isOpen} title={title} onClose={onClose}>
      <form onSubmit={handleSubmit}>
        <div className="modal-body">
          {error && <div className="alert alert-danger">{error}</div>}
          <div className="form-group">
            <label className="form-label" htmlFor="category-name">
              Category Name *
            </label>
            <input
              id="category-name"
              type="text"
              className="form-input"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Enter category name"
              autoFocus
            />
          </div>
          <div className="form-group">
            <label className="form-label" htmlFor="category-status">
              Status *
            </label>
            <select
              id="category-status"
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

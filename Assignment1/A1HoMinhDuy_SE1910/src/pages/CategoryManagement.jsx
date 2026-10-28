import React, { useState } from 'react';
import CategoryDialog from '../components/CategoryDialog.jsx';
import ConfirmDialog from '../components/ConfirmDialog.jsx';
import { STATUS_LABELS, searchRecords } from '../services/newsService.js';

export default function CategoryManagement({ categories = [], onSaveRecord, onDeleteRecord }) {
  const [searchTerm, setSearchTerm] = useState('');
  const [dialogState, setDialogState] = useState({ isOpen: false, mode: 'create', item: null });
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');

  // Source list is preserved. Displayed list is filtered based on searchTerm.
  const displayedCategories = searchTerm.trim()
    ? searchRecords(categories, 'name', searchTerm)
    : categories;

  function handleOpenCreate() {
    setErrorMessage('');
    setDialogState({ isOpen: true, mode: 'create', item: null });
  }

  function handleOpenEdit(item) {
    setErrorMessage('');
    setDialogState({ isOpen: true, mode: 'update', item });
  }

  function handleCloseDialog() {
    setDialogState({ isOpen: false, mode: 'create', item: null });
  }

  function handleSave(values) {
    try {
      setErrorMessage('');
      onSaveRecord('categories', values, dialogState.item ? dialogState.item.id : null);
      handleCloseDialog();
    } catch (err) {
      setErrorMessage(err.message || 'Error saving category.');
      throw err;
    }
  }

  function handleRequestDelete(item) {
    setErrorMessage('');
    setDeleteTarget(item);
  }

  function handleConfirmDelete() {
    if (!deleteTarget) return;
    try {
      setErrorMessage('');
      onDeleteRecord('categories', deleteTarget.id);
      setDeleteTarget(null);
    } catch (err) {
      setErrorMessage(err.message || 'Error deleting category.');
      setDeleteTarget(null);
    }
  }

  function handleCancelDelete() {
    setDeleteTarget(null);
  }

  function handleClearSearch() {
    setSearchTerm('');
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <h1 className="page-title">Category Management</h1>
        <button type="button" className="btn btn-primary" onClick={handleOpenCreate}>
          Add New Category
        </button>
      </div>

      {errorMessage && (
        <div className="alert alert-danger" role="alert">
          {errorMessage}
        </div>
      )}

      <div className="toolbar-container">
        <div className="search-form">
          <input
            type="text"
            className="search-input"
            placeholder="Search category by name..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          {searchTerm && (
            <button type="button" className="btn btn-outline btn-sm" onClick={handleClearSearch}>
              Clear
            </button>
          )}
        </div>
        <div style={{ fontSize: '0.9rem', color: '#666666' }}>
          Total: {categories.length} item(s) {searchTerm.trim() ? `(Filtered: ${displayedCategories.length})` : ''}
        </div>
      </div>

      {categories.length === 0 ? (
        <div className="empty-state">No categories available. Click "Add New Category" to create one.</div>
      ) : displayedCategories.length === 0 ? (
        <div className="empty-state">
          No categories match the keyword "<strong>{searchTerm}</strong>".
          <div style={{ marginTop: '10px' }}>
            <button type="button" className="btn btn-outline btn-sm" onClick={handleClearSearch}>
              Reset Filter
            </button>
          </div>
        </div>
      ) : (
        <div className="data-table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th style={{ width: '120px' }}>ID</th>
                <th>Category Name</th>
                <th style={{ width: '120px' }}>Status</th>
                <th style={{ width: '140px' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {displayedCategories.map((item) => (
                <tr key={item.id}>
                  <td><code>{item.id}</code></td>
                  <td><strong>{item.name}</strong></td>
                  <td>
                    <span className={`badge ${item.status === 1 ? 'badge-active' : 'badge-inactive'}`}>
                      {STATUS_LABELS[item.status] || 'Unknown'}
                    </span>
                  </td>
                  <td className="actions-cell">
                    <div className="btn-group">
                      <button
                        type="button"
                        className="btn btn-outline btn-sm"
                        onClick={() => handleOpenEdit(item)}
                      >
                        Edit
                      </button>
                      <button
                        type="button"
                        className="btn btn-danger btn-sm"
                        onClick={() => handleRequestDelete(item)}
                      >
                        Delete
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <CategoryDialog
        isOpen={dialogState.isOpen}
        mode={dialogState.mode}
        initialData={dialogState.item}
        onSave={handleSave}
        onClose={handleCloseDialog}
      />

      <ConfirmDialog
        isOpen={Boolean(deleteTarget)}
        title="Confirm Category Deletion"
        message={
          deleteTarget
            ? `Are you sure you want to delete category "${deleteTarget.name}"? This action cannot be undone.`
            : ''
        }
        onConfirm={handleConfirmDelete}
        onCancel={handleCancelDelete}
      />
    </div>
  );
}

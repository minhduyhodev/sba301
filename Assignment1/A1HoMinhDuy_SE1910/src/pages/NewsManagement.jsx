import React, { useState } from 'react';
import NewsDialog from '../components/NewsDialog.jsx';
import ConfirmDialog from '../components/ConfirmDialog.jsx';
import { STATUS_LABELS, searchRecords } from '../services/newsService.js';

export default function NewsManagement({ news = [], categories = [], users = [], onSaveRecord, onDeleteRecord }) {
  const [searchTerm, setSearchTerm] = useState('');
  const [dialogState, setDialogState] = useState({ isOpen: false, mode: 'create', item: null });
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');

  // Source list is preserved. Filter displayed news by title.
  const displayedNews = searchTerm.trim()
    ? searchRecords(news, 'title', searchTerm)
    : news;

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
      onSaveRecord('news', values, dialogState.item ? dialogState.item.id : null);
      handleCloseDialog();
    } catch (err) {
      setErrorMessage(err.message || 'Error saving news article.');
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
      onDeleteRecord('news', deleteTarget.id);
      setDeleteTarget(null);
    } catch (err) {
      setErrorMessage(err.message || 'Error deleting news article.');
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
        <h1 className="page-title">News Management</h1>
        <button type="button" className="btn btn-primary" onClick={handleOpenCreate}>
          Add New Article
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
            placeholder="Search news by title..."
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
          Total: {news.length} article(s) {searchTerm.trim() ? `(Filtered: ${displayedNews.length})` : ''}
        </div>
      </div>

      {news.length === 0 ? (
        <div className="empty-state">No news articles found. Click "Add New Article" to publish one.</div>
      ) : displayedNews.length === 0 ? (
        <div className="empty-state">
          No news articles match the keyword "<strong>{searchTerm}</strong>".
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
                <th style={{ width: '100px' }}>ID</th>
                <th>Title</th>
                <th style={{ width: '180px' }}>Category</th>
                <th style={{ width: '120px' }}>Author</th>
                <th style={{ width: '100px' }}>Status</th>
                <th style={{ width: '140px' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {displayedNews.map((item) => {
                const category = categories.find((c) => c.id === item.categoryId);
                const author = users.find((u) => u.id === item.createdBy);
                return (
                  <tr key={item.id}>
                    <td><code>{item.id}</code></td>
                    <td>
                      <div><strong>{item.title}</strong></div>
                      <div style={{ fontSize: '0.85rem', color: '#666666', marginTop: '4px' }}>
                        {item.content.length > 80 ? `${item.content.substring(0, 80)}...` : item.content}
                      </div>
                    </td>
                    <td>{category ? category.name : 'Unknown Category'}</td>
                    <td>{author ? author.username : item.createdBy}</td>
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
                );
              })}
            </tbody>
          </table>
        </div>
      )}

      <NewsDialog
        isOpen={dialogState.isOpen}
        mode={dialogState.mode}
        initialData={dialogState.item}
        categories={categories}
        onSave={handleSave}
        onClose={handleCloseDialog}
      />

      <ConfirmDialog
        isOpen={Boolean(deleteTarget)}
        title="Confirm News Deletion"
        message={
          deleteTarget
            ? `Are you sure you want to delete article "${deleteTarget.title}"? This action cannot be undone.`
            : ''
        }
        onConfirm={handleConfirmDelete}
        onCancel={handleCancelDelete}
      />
    </div>
  );
}

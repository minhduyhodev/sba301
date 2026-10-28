import React, { useState, useEffect } from 'react';
import Modal from './Modal.jsx';

export default function NewsDialog({ isOpen, mode, initialData, categories = [], onSave, onClose }) {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [categoryId, setCategoryId] = useState('');
  const [status, setStatus] = useState(1);
  const [error, setError] = useState('');

  useEffect(() => {
    if (isOpen) {
      if (mode === 'update' && initialData) {
        setTitle(initialData.title || '');
        setContent(initialData.content || '');
        setCategoryId(initialData.categoryId || (categories[0]?.id || ''));
        setStatus(initialData.status ?? 1);
      } else {
        setTitle('');
        setContent('');
        setCategoryId(categories[0]?.id || '');
        setStatus(1);
      }
      setError('');
    }
  }, [isOpen, mode, initialData, categories]);

  function handleSubmit(e) {
    e.preventDefault();
    if (!title.trim()) {
      setError('Title is required.');
      return;
    }
    if (!content.trim()) {
      setError('Content is required.');
      return;
    }
    if (!categoryId) {
      setError('Please select a category.');
      return;
    }
    try {
      onSave({
        title: title.trim(),
        content: content.trim(),
        categoryId,
        status: Number(status),
      });
      setError('');
    } catch (err) {
      setError(err.message || 'Failed to save news.');
    }
  }

  const dialogTitle = mode === 'update' ? 'Edit News Article' : 'Create News Article';

  return (
    <Modal isOpen={isOpen} title={dialogTitle} onClose={onClose}>
      <form onSubmit={handleSubmit}>
        <div className="modal-body">
          {error && <div className="alert alert-danger">{error}</div>}
          <div className="form-group">
            <label className="form-label" htmlFor="news-title">
              Title *
            </label>
            <input
              id="news-title"
              type="text"
              className="form-input"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Enter news title"
              autoFocus
            />
          </div>
          <div className="form-group">
            <label className="form-label" htmlFor="news-category">
              Category *
            </label>
            <select
              id="news-category"
              className="form-select"
              value={categoryId}
              onChange={(e) => setCategoryId(e.target.value)}
            >
              {categories.length === 0 ? (
                <option value="">No categories available</option>
              ) : (
                categories.map((cat) => (
                  <option key={cat.id} value={cat.id}>
                    {cat.name} {cat.status === 0 ? '(Inactive)' : ''}
                  </option>
                ))
              )}
            </select>
          </div>
          <div className="form-group">
            <label className="form-label" htmlFor="news-content">
              Content *
            </label>
            <textarea
              id="news-content"
              className="form-textarea"
              value={content}
              onChange={(e) => setContent(e.target.value)}
              placeholder="Enter news content"
              rows={4}
            />
          </div>
          <div className="form-group">
            <label className="form-label" htmlFor="news-status">
              Status *
            </label>
            <select
              id="news-status"
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

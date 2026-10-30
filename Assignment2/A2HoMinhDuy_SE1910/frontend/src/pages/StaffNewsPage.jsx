import React, { useState, useEffect } from 'react';
import { newsApi } from '../api/newsApi';
import { categoryApi } from '../api/categoryApi';
import { tagApi } from '../api/tagApi';
import { useAuth } from '../context/AuthContext';
import { Modal } from '../components/Modal';
import { ConfirmDialog } from '../components/ConfirmDialog';

export const StaffNewsPage = () => {
  const { user } = useAuth();
  const [articles, setArticles] = useState([]);
  const [categories, setCategories] = useState([]);
  const [availableTags, setAvailableTags] = useState([]);
  const [keyword, setKeyword] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [loading, setLoading] = useState(false);
  const [alertMsg, setAlertMsg] = useState(null);
  const [errorMsg, setErrorMsg] = useState(null);

  // Modal state
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingArticle, setEditingArticle] = useState(null);
  const [formData, setFormData] = useState({
    newsArticleId: '',
    newsTitle: '',
    headline: '',
    newsContent: '',
    newsSource: '',
    categoryId: '',
    newsStatus: true,
    tagIds: [],
  });

  // Fast tag creation inline
  const [newTagName, setNewTagName] = useState('');

  // Delete dialog state
  const [deleteTarget, setDeleteTarget] = useState(null);

  useEffect(() => {
    loadMetadata();
    fetchArticles();
  }, []);

  const loadMetadata = async () => {
    try {
      const [cats, tags] = await Promise.all([
        categoryApi.getAll(),
        tagApi.getAll(),
      ]);
      setCategories(cats);
      setAvailableTags(tags);
    } catch (err) {
      console.error('Failed to load categories/tags', err);
    }
  };

  const fetchArticles = async (kw = keyword, catId = selectedCategory) => {
    setLoading(true);
    try {
      const data = await newsApi.getAllForStaff(kw, catId ? Number(catId) : null);
      setArticles(data);
    } catch (err) {
      setErrorMsg('Failed to load news articles: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    fetchArticles(keyword, selectedCategory);
  };

  const openCreateModal = () => {
    setEditingArticle(null);
    setFormData({
      newsArticleId: 'NA' + String(Date.now()).slice(-6),
      newsTitle: '',
      headline: '',
      newsContent: '',
      newsSource: 'FPT University',
      categoryId: categories[0]?.categoryId || '',
      newsStatus: true,
      tagIds: [],
    });
    setIsModalOpen(true);
    setErrorMsg(null);
  };

  const openEditModal = (article) => {
    setEditingArticle(article);
    setFormData({
      newsArticleId: article.newsArticleId,
      newsTitle: article.newsTitle,
      headline: article.headline || '',
      newsContent: article.newsContent,
      newsSource: article.newsSource || '',
      categoryId: article.category?.categoryId || '',
      newsStatus: Boolean(article.newsStatus),
      tagIds: article.tags?.map(t => t.tagId) || [],
    });
    setIsModalOpen(true);
    setErrorMsg(null);
  };

  const handleTagToggle = (tagId) => {
    setFormData(prev => {
      const current = prev.tagIds || [];
      if (current.includes(tagId)) {
        return { ...prev, tagIds: current.filter(id => id !== tagId) };
      } else {
        return { ...prev, tagIds: [...current, tagId] };
      }
    });
  };

  const handleCreateNewTag = async (e) => {
    e.preventDefault();
    if (!newTagName.trim()) return;
    try {
      const created = await tagApi.create({ tagName: newTagName.trim(), note: 'User added' });
      setAvailableTags([...availableTags, created]);
      setFormData(prev => ({ ...prev, tagIds: [...prev.tagIds, created.tagId] }));
      setNewTagName('');
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Could not create tag');
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setErrorMsg(null);
    try {
      const payload = {
        newsArticleId: formData.newsArticleId.trim(),
        newsTitle: formData.newsTitle.trim(),
        headline: formData.headline.trim(),
        newsContent: formData.newsContent.trim(),
        newsSource: formData.newsSource.trim(),
        categoryId: Number(formData.categoryId),
        newsStatus: Boolean(formData.newsStatus),
        tagIds: formData.tagIds,
        createdById: user?.accountId,
      };

      if (editingArticle) {
        await newsApi.update(editingArticle.newsArticleId, payload);
        setAlertMsg(`Article '${payload.newsArticleId}' updated successfully.`);
      } else {
        await newsApi.create(payload);
        setAlertMsg(`New article '${payload.newsArticleId}' created successfully.`);
      }
      setIsModalOpen(false);
      fetchArticles();
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Failed to save news article.');
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;
    setErrorMsg(null);
    setAlertMsg(null);
    try {
      await newsApi.delete(deleteTarget.newsArticleId);
      setAlertMsg(`Article '${deleteTarget.newsArticleId}' deleted successfully.`);
      fetchArticles();
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Delete article failed.');
    } finally {
      setDeleteTarget(null);
    }
  };

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1 className="page-title">News Article Management (Staff)</h1>
          <p className="page-description">
            Create, update, manage tags, and publish news articles. Active articles appear on the public portal.
          </p>
        </div>
        <button className="btn btn-primary" onClick={openCreateModal}>
          Create News Article
        </button>
      </div>

      {alertMsg && <div className="alert alert-success">{alertMsg}</div>}
      {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}

      {/* Search and Category Filter Bar */}
      <form className="search-filter-bar" onSubmit={handleSearch}>
        <input 
          type="text" 
          className="form-control" 
          placeholder="Search by title or content..."
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <select 
          className="form-control"
          value={selectedCategory}
          onChange={(e) => {
            setSelectedCategory(e.target.value);
            fetchArticles(keyword, e.target.value);
          }}
        >
          <option value="">All Categories</option>
          {categories.map((c) => (
            <option key={c.categoryId} value={c.categoryId}>
              {c.categoryName}
            </option>
          ))}
        </select>
        <button type="submit" className="btn btn-primary">Search</button>
        {(keyword || selectedCategory) && (
          <button 
            type="button" 
            className="btn btn-secondary"
            onClick={() => {
              setKeyword('');
              setSelectedCategory('');
              fetchArticles('', '');
            }}
          >
            Reset
          </button>
        )}
      </form>

      {/* News Table */}
      {loading ? (
        <div className="loading-state">Loading news articles...</div>
      ) : (
        <div className="table-card">
          <table className="data-table">
            <thead>
              <tr>
                <th style={{ width: '100px' }}>Article ID</th>
                <th>Title & Headline</th>
                <th>Category</th>
                <th>Status</th>
                <th>Author</th>
                <th>Tags</th>
                <th style={{ width: '160px', textAlign: 'center' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {articles.length === 0 ? (
                <tr>
                  <td colSpan="7" className="text-center py-4">No articles found.</td>
                </tr>
              ) : (
                articles.map((art) => (
                  <tr key={art.newsArticleId}>
                    <td><code>{art.newsArticleId}</code></td>
                    <td>
                      <div className="table-title-cell">
                        <strong>{art.newsTitle}</strong>
                        {art.headline && <small className="text-muted">{art.headline}</small>}
                      </div>
                    </td>
                    <td>
                      <span className="category-pill">
                        {art.category?.categoryName || '-'}
                      </span>
                    </td>
                    <td>
                      <span className={`status-pill ${art.newsStatus ? 'status-active' : 'status-inactive'}`}>
                        {art.newsStatus ? 'Active (1)' : 'Inactive (0)'}
                      </span>
                    </td>
                    <td>{art.createdBy?.accountName || 'Staff'}</td>
                    <td>
                      <div className="table-tags-group">
                        {art.tags && art.tags.length > 0 ? (
                          art.tags.map(t => (
                            <span key={t.tagId} className="tag-chip tag-chip-sm">
                              #{t.tagName}
                            </span>
                          ))
                        ) : (
                          <span className="text-muted">-</span>
                        )}
                      </div>
                    </td>
                    <td className="text-center">
                      <div className="action-buttons-group">
                        <button 
                          className="btn btn-sm btn-outline-primary"
                          onClick={() => openEditModal(art)}
                        >
                          Edit
                        </button>
                        <button 
                          className="btn btn-sm btn-outline-danger"
                          onClick={() => setDeleteTarget(art)}
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
        title={editingArticle ? `Edit Article [${editingArticle.newsArticleId}]` : 'Create News Article'}
        maxWidth="700px"
      >
        <form onSubmit={handleSave}>
          <div className="form-row">
            <div className="form-group flex-1">
              <label className="form-label">Article ID *</label>
              <input 
                type="text" 
                className="form-control" 
                required
                disabled={!!editingArticle}
                value={formData.newsArticleId}
                onChange={(e) => setFormData({ ...formData, newsArticleId: e.target.value })}
                placeholder="e.g. NA0001"
              />
            </div>
            <div className="form-group flex-1">
              <label className="form-label">Category *</label>
              <select 
                className="form-control"
                required
                value={formData.categoryId}
                onChange={(e) => setFormData({ ...formData, categoryId: e.target.value })}
              >
                <option value="">Select Category</option>
                {categories.map((c) => (
                  <option key={c.categoryId} value={c.categoryId}>
                    {c.categoryName}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Title *</label>
            <input 
              type="text" 
              className="form-control" 
              required
              value={formData.newsTitle}
              onChange={(e) => setFormData({ ...formData, newsTitle: e.target.value })}
              placeholder="News headline title"
            />
          </div>

          <div className="form-group">
            <label className="form-label">Headline (Summary subtitle)</label>
            <input 
              type="text" 
              className="form-control" 
              value={formData.headline}
              onChange={(e) => setFormData({ ...formData, headline: e.target.value })}
              placeholder="Brief 1-sentence teaser"
            />
          </div>

          <div className="form-group">
            <label className="form-label">News Content *</label>
            <textarea 
              className="form-control" 
              rows="5"
              required
              value={formData.newsContent}
              onChange={(e) => setFormData({ ...formData, newsContent: e.target.value })}
              placeholder="Full article content text..."
            />
          </div>

          <div className="form-row">
            <div className="form-group flex-1">
              <label className="form-label">News Source</label>
              <input 
                type="text" 
                className="form-control" 
                value={formData.newsSource}
                onChange={(e) => setFormData({ ...formData, newsSource: e.target.value })}
                placeholder="e.g. FPT University Press"
              />
            </div>
            <div className="form-group flex-1 checkbox-group" style={{ marginTop: '28px' }}>
              <label className="checkbox-label">
                <input 
                  type="checkbox" 
                  checked={formData.newsStatus}
                  onChange={(e) => setFormData({ ...formData, newsStatus: e.target.checked })}
                />
                <span>Active (NewsStatus = 1, Visible publicly)</span>
              </label>
            </div>
          </div>

          {/* Tags Selection & Creation */}
          <div className="form-group">
            <label className="form-label">Article Tags (Multi-select via NewsTag)</label>
            <div className="tags-selector-box">
              {availableTags.map((tag) => {
                const isSelected = formData.tagIds.includes(tag.tagId);
                return (
                  <button 
                    key={tag.tagId}
                    type="button"
                    className={`tag-toggle-btn ${isSelected ? 'tag-selected' : ''}`}
                    onClick={() => handleTagToggle(tag.tagId)}
                  >
                    #{tag.tagName}
                  </button>
                );
              })}
            </div>

            <div className="inline-add-tag-box">
              <input 
                type="text" 
                className="form-control form-control-sm"
                placeholder="New tag name..."
                value={newTagName}
                onChange={(e) => setNewTagName(e.target.value)}
              />
              <button 
                type="button" 
                className="btn btn-secondary btn-sm"
                onClick={handleCreateNewTag}
              >
                Add Tag
              </button>
            </div>
          </div>

          <div className="modal-actions">
            <button type="button" className="btn btn-secondary" onClick={() => setIsModalOpen(false)}>
              Cancel
            </button>
            <button type="submit" className="btn btn-primary">
              {editingArticle ? 'Save Changes' : 'Create Article'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Delete Confirmation Dialog */}
      <ConfirmDialog 
        isOpen={!!deleteTarget}
        onClose={() => setDeleteTarget(null)}
        onConfirm={handleDelete}
        title="Confirm Article Deletion"
        message={`Are you sure you want to delete article "${deleteTarget?.newsTitle}" (${deleteTarget?.newsArticleId})? Its tag associations will also be removed.`}
      />
    </div>
  );
};

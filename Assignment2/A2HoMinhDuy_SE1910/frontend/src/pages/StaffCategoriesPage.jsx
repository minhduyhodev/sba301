import React, { useState, useEffect } from 'react';
import { categoryApi } from '../api/categoryApi';
import { Modal } from '../components/Modal';
import { ConfirmDialog } from '../components/ConfirmDialog';

export const StaffCategoriesPage = () => {
  const [categories, setCategories] = useState([]);
  const [keyword, setKeyword] = useState('');
  const [loading, setLoading] = useState(false);
  const [alertMsg, setAlertMsg] = useState(null);
  const [errorMsg, setErrorMsg] = useState(null);

  // Modal state
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingCategory, setEditingCategory] = useState(null);
  const [formData, setFormData] = useState({
    categoryName: '',
    categoryDesciption: '',
    parentCategoryId: '',
    isActive: true,
  });

  // Delete dialog state
  const [deleteTarget, setDeleteTarget] = useState(null);

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async (kw = keyword) => {
    setLoading(true);
    try {
      const data = await categoryApi.getAll(kw);
      setCategories(data);
    } catch (err) {
      setErrorMsg('Failed to load categories: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    fetchCategories(keyword);
  };

  const openCreateModal = () => {
    setEditingCategory(null);
    setFormData({
      categoryName: '',
      categoryDesciption: '',
      parentCategoryId: '',
      isActive: true,
    });
    setIsModalOpen(true);
    setErrorMsg(null);
  };

  const openEditModal = (cat) => {
    setEditingCategory(cat);
    setFormData({
      categoryName: cat.categoryName,
      categoryDesciption: cat.categoryDesciption || cat.categoryDescription || '',
      parentCategoryId: cat.parentCategoryId || '',
      isActive: cat.isActive !== false,
    });
    setIsModalOpen(true);
    setErrorMsg(null);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setErrorMsg(null);
    try {
      const payload = {
        categoryName: formData.categoryName.trim(),
        categoryDesciption: formData.categoryDesciption.trim(),
        parentCategoryId: formData.parentCategoryId ? Number(formData.parentCategoryId) : null,
        isActive: Boolean(formData.isActive),
      };

      if (editingCategory) {
        await categoryApi.update(editingCategory.categoryId, payload);
        setAlertMsg(`Category #${editingCategory.categoryId} updated successfully.`);
      } else {
        await categoryApi.create(payload);
        setAlertMsg('New category created successfully.');
      }
      setIsModalOpen(false);
      fetchCategories();
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Failed to save category.');
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;
    setErrorMsg(null);
    setAlertMsg(null);
    try {
      await categoryApi.delete(deleteTarget.categoryId);
      setAlertMsg(`Category '${deleteTarget.categoryName}' deleted successfully.`);
      fetchCategories();
    } catch (err) {
      const msg = err.response?.data?.message || 'Delete failed.';
      setErrorMsg(`Cannot delete category: ${msg}`);
    } finally {
      setDeleteTarget(null);
    }
  };

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1 className="page-title">Category Management (Staff)</h1>
          <p className="page-description">
            Organize news by categories and sub-categories. Categories currently assigned to news articles cannot be deleted.
          </p>
        </div>
        <button className="btn btn-primary" onClick={openCreateModal}>
          Add New Category
        </button>
      </div>

      {alertMsg && <div className="alert alert-success">{alertMsg}</div>}
      {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}

      {/* Search Bar */}
      <form className="search-filter-bar" onSubmit={handleSearch}>
        <input 
          type="text" 
          className="form-control" 
          placeholder="Search categories by name..."
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
              fetchCategories('');
            }}
          >
            Clear
          </button>
        )}
      </form>

      {/* Category Table */}
      {loading ? (
        <div className="loading-state">Loading categories...</div>
      ) : (
        <div className="table-card">
          <table className="data-table">
            <thead>
              <tr>
                <th style={{ width: '80px' }}>ID</th>
                <th>Category Name</th>
                <th>Description</th>
                <th>Parent Category</th>
                <th>Status</th>
                <th style={{ width: '160px', textAlign: 'center' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {categories.length === 0 ? (
                <tr>
                  <td colSpan="6" className="text-center py-4">No categories found.</td>
                </tr>
              ) : (
                categories.map((cat) => {
                  const parent = categories.find(c => c.categoryId === cat.parentCategoryId);
                  return (
                    <tr key={cat.categoryId}>
                      <td>#{cat.categoryId}</td>
                      <td><strong>{cat.categoryName}</strong></td>
                      <td>{cat.categoryDesciption || cat.categoryDescription || '-'}</td>
                      <td>
                        {parent ? (
                          <span className="badge badge-subtle">{parent.categoryName} (#{parent.categoryId})</span>
                        ) : (
                          <span className="text-muted">None (Root)</span>
                        )}
                      </td>
                      <td>
                        <span className={`status-pill ${cat.isActive ? 'status-active' : 'status-inactive'}`}>
                          {cat.isActive ? 'Active (1)' : 'Inactive (0)'}
                        </span>
                      </td>
                      <td className="text-center">
                        <div className="action-buttons-group">
                          <button 
                            className="btn btn-sm btn-outline-primary"
                            onClick={() => openEditModal(cat)}
                          >
                            Edit
                          </button>
                          <button 
                            className="btn btn-sm btn-outline-danger"
                            onClick={() => setDeleteTarget(cat)}
                          >
                            Delete
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>
      )}

      {/* Create / Edit Popup Dialog */}
      <Modal 
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingCategory ? `Edit Category #${editingCategory.categoryId}` : 'Add New Category'}
      >
        <form onSubmit={handleSave}>
          <div className="form-group">
            <label className="form-label">Category Name *</label>
            <input 
              type="text" 
              className="form-control" 
              required
              value={formData.categoryName}
              onChange={(e) => setFormData({ ...formData, categoryName: e.target.value })}
              placeholder="e.g. Campus Events"
            />
          </div>

          <div className="form-group">
            <label className="form-label">Category Description</label>
            <textarea 
              className="form-control" 
              rows="3"
              value={formData.categoryDesciption}
              onChange={(e) => setFormData({ ...formData, categoryDesciption: e.target.value })}
              placeholder="Brief description of articles in this category..."
            />
          </div>

          <div className="form-group">
            <label className="form-label">Parent Category (Optional)</label>
            <select 
              className="form-control"
              value={formData.parentCategoryId}
              onChange={(e) => setFormData({ ...formData, parentCategoryId: e.target.value })}
            >
              <option value="">None (Top-Level Category)</option>
              {categories
                .filter(c => !editingCategory || c.categoryId !== editingCategory.categoryId)
                .map((c) => (
                  <option key={c.categoryId} value={c.categoryId}>
                    {c.categoryName} (#{c.categoryId})
                  </option>
                ))}
            </select>
          </div>

          <div className="form-group checkbox-group">
            <label className="checkbox-label">
              <input 
                type="checkbox" 
                checked={formData.isActive}
                onChange={(e) => setFormData({ ...formData, isActive: e.target.checked })}
              />
              <span>Is Active (Available for public news when published)</span>
            </label>
          </div>

          <div className="modal-actions">
            <button type="button" className="btn btn-secondary" onClick={() => setIsModalOpen(false)}>
              Cancel
            </button>
            <button type="submit" className="btn btn-primary">
              {editingCategory ? 'Save Changes' : 'Create Category'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Delete Confirmation Dialog */}
      <ConfirmDialog 
        isOpen={!!deleteTarget}
        onClose={() => setDeleteTarget(null)}
        onConfirm={handleDelete}
        title="Confirm Category Deletion"
        message={`Are you sure you want to delete category "${deleteTarget?.categoryName}"? If this category is already referenced by any news articles or sub-categories, deletion will be rejected.`}
      />
    </div>
  );
};

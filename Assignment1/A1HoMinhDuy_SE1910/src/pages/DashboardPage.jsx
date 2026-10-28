import React from 'react';
import { STATUS_LABELS } from '../services/newsService.js';

export default function DashboardPage({ data }) {
  const categories = data?.categories || [];
  const news = data?.news || [];
  const users = data?.users || [];

  const activeCategories = categories.filter((c) => c.status === 1).length;
  const activeNews = news.filter((n) => n.status === 1).length;
  const adminUsers = users.filter((u) => u.role === 1).length;

  return (
    <div className="page-container">
      <div className="page-header">
        <h1 className="page-title">Dashboard Overview</h1>
      </div>

      <div className="dashboard-grid">
        <div className="stat-card">
          <div className="stat-label">Categories</div>
          <div className="stat-value">{categories.length}</div>
          <div className="stat-detail">
            {activeCategories} Active &bull; {categories.length - activeCategories} Inactive
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-label">News Articles</div>
          <div className="stat-value">{news.length}</div>
          <div className="stat-detail">
            {activeNews} Active &bull; {news.length - activeNews} Inactive
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-label">System Accounts</div>
          <div className="stat-value">{users.length}</div>
          <div className="stat-detail">
            {adminUsers} Admin &bull; {users.length - adminUsers} Staff
          </div>
        </div>
      </div>

      <div style={{ marginTop: '32px' }}>
        <h2 style={{ fontSize: '1.15rem', marginBottom: '14px', color: '#333333' }}>
          Recent News Publications
        </h2>
        {news.length === 0 ? (
          <div className="empty-state">No news articles found in the system.</div>
        ) : (
          <div className="data-table-container">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Category</th>
                  <th>Author</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {news.slice(0, 5).map((item) => {
                  const cat = categories.find((c) => c.id === item.categoryId);
                  const author = users.find((u) => u.id === item.createdBy);
                  return (
                    <tr key={item.id}>
                      <td><strong>{item.title}</strong></td>
                      <td>{cat ? cat.name : 'Unknown Category'}</td>
                      <td>{author ? author.username : item.createdBy}</td>
                      <td>
                        <span className={`badge ${item.status === 1 ? 'badge-active' : 'badge-inactive'}`}>
                          {STATUS_LABELS[item.status] || 'Unknown'}
                        </span>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

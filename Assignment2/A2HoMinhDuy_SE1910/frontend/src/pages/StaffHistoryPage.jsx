import React, { useState, useEffect } from 'react';
import { newsApi } from '../api/newsApi';
import { useAuth } from '../context/AuthContext';

export const StaffHistoryPage = () => {
  const { user } = useAuth();
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchHistory();
  }, [user]);

  const fetchHistory = async () => {
    setLoading(true);
    try {
      const data = await newsApi.getHistory();
      setHistory(data);
    } catch (err) {
      setError('Failed to fetch author history: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1 className="page-title">My News History</h1>
          <p className="page-description">
            Articles authored and published by you (Account #{user?.accountId}: {user?.accountName}).
          </p>
        </div>
        <div className="stat-badge">
          Total Authored: <strong>{history.length}</strong>
        </div>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      {loading ? (
        <div className="loading-state">Loading your article history...</div>
      ) : history.length === 0 ? (
        <div className="empty-state">
          <p>You have not created any news articles yet.</p>
        </div>
      ) : (
        <div className="table-card">
          <table className="data-table">
            <thead>
              <tr>
                <th style={{ width: '110px' }}>Article ID</th>
                <th>Title</th>
                <th>Category</th>
                <th>Status</th>
                <th>Tags</th>
                <th>Created Date</th>
                <th>Last Modified</th>
              </tr>
            </thead>
            <tbody>
              {history.map((art) => (
                <tr key={art.newsArticleId}>
                  <td><code>{art.newsArticleId}</code></td>
                  <td>
                    <strong>{art.newsTitle}</strong>
                    {art.headline && <div className="text-muted text-sm">{art.headline}</div>}
                  </td>
                  <td>
                    <span className="category-pill">
                      {art.category?.categoryName || '-'}
                    </span>
                  </td>
                  <td>
                    <span className={`status-pill ${art.newsStatus ? 'status-active' : 'status-inactive'}`}>
                      {art.newsStatus ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                  <td>
                    <div className="table-tags-group">
                      {art.tags?.map(t => (
                        <span key={t.tagId} className="tag-chip tag-chip-sm">
                          #{t.tagName}
                        </span>
                      ))}
                    </div>
                  </td>
                  <td>{art.createdDate ? new Date(art.createdDate).toLocaleString() : '-'}</td>
                  <td>{art.modifiedDate ? new Date(art.modifiedDate).toLocaleString() : 'Never'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

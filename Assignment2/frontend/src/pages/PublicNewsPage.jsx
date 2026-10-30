import React, { useState, useEffect } from 'react';
import { newsApi } from '../api/newsApi';
import { categoryApi } from '../api/categoryApi';
import { Modal } from '../components/Modal';

export const PublicNewsPage = () => {
  const [articles, setArticles] = useState([]);
  const [categories, setCategories] = useState([]);
  const [keyword, setKeyword] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Selected article for reading detail popup
  const [readingArticle, setReadingArticle] = useState(null);

  useEffect(() => {
    loadCategories();
    fetchNews();
  }, []);

  const loadCategories = async () => {
    try {
      const data = await categoryApi.getActive();
      setCategories(data);
    } catch (err) {
      console.error('Failed to load categories', err);
    }
  };

  const fetchNews = async (kw = keyword, catId = selectedCategory) => {
    setLoading(true);
    setError(null);
    try {
      const data = await newsApi.getPublic(kw, catId ? Number(catId) : null);
      setArticles(data);
    } catch (err) {
      setError('Failed to fetch public news articles. Please ensure backend is running.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    fetchNews(keyword, selectedCategory);
  };

  const handleCategoryChange = (e) => {
    const catId = e.target.value;
    setSelectedCategory(catId);
    fetchNews(keyword, catId);
  };

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1 className="page-title">University News & Announcements</h1>
          <p className="page-description">
            Public portal for students, faculty, and visitors. Only published and active articles (NewsStatus = 1) are displayed.
          </p>
        </div>
      </div>

      {/* Filter and Search Bar */}
      <form className="search-filter-bar" onSubmit={handleSearch}>
        <div className="search-input-group">
          <input 
            type="text" 
            className="form-control" 
            placeholder="Search news by title or content..." 
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
        </div>

        <div className="filter-select-group">
          <select 
            className="form-control"
            value={selectedCategory}
            onChange={handleCategoryChange}
          >
            <option value="">All Categories</option>
            {categories.map((c) => (
              <option key={c.categoryId} value={c.categoryId}>
                {c.categoryName}
              </option>
            ))}
          </select>
        </div>

        <button type="submit" className="btn btn-primary">
          🔍 Search
        </button>
        {(keyword || selectedCategory) && (
          <button 
            type="button" 
            className="btn btn-secondary"
            onClick={() => {
              setKeyword('');
              setSelectedCategory('');
              fetchNews('', '');
            }}
          >
            Reset
          </button>
        )}
      </form>

      {error && <div className="alert alert-danger">{error}</div>}

      {/* Articles Grid */}
      {loading ? (
        <div className="loading-state">Loading published news...</div>
      ) : articles.length === 0 ? (
        <div className="empty-state">
          <p>No active news articles found matching your criteria.</p>
        </div>
      ) : (
        <div className="news-cards-grid">
          {articles.map((item) => (
            <article key={item.newsArticleId} className="news-card">
              <div className="news-card-header">
                <span className="category-pill">
                  {item.category?.categoryName || 'General'}
                </span>
                <span className="news-date">
                  {item.createdDate ? new Date(item.createdDate).toLocaleDateString() : 'Recent'}
                </span>
              </div>

              <h3 className="news-card-title">{item.newsTitle}</h3>
              {item.headline && <p className="news-card-headline">{item.headline}</p>}

              <p className="news-card-excerpt">
                {item.newsContent?.length > 160 
                  ? item.newsContent.substring(0, 160) + '...' 
                  : item.newsContent}
              </p>

              {/* Tags */}
              {item.tags && item.tags.length > 0 && (
                <div className="tags-container">
                  {item.tags.map((t) => (
                    <span key={t.tagId} className="tag-chip">
                      #{t.tagName}
                    </span>
                  ))}
                </div>
              )}

              <div className="news-card-footer">
                <span className="news-author">
                  ✍️ {item.createdBy?.accountName || 'Staff'}
                </span>
                <button 
                  className="btn btn-outline-primary btn-sm"
                  onClick={() => setReadingArticle(item)}
                >
                  Read Article →
                </button>
              </div>
            </article>
          ))}
        </div>
      )}

      {/* Reading Article Modal */}
      <Modal 
        isOpen={!!readingArticle} 
        onClose={() => setReadingArticle(null)}
        title={readingArticle?.newsTitle || 'Article Details'}
        maxWidth="750px"
      >
        {readingArticle && (
          <div className="article-detail-view">
            <div className="article-meta-bar">
              <span className="category-pill">{readingArticle.category?.categoryName}</span>
              <span>📅 {new Date(readingArticle.createdDate).toLocaleString()}</span>
              <span>👤 Author: {readingArticle.createdBy?.accountName}</span>
              {readingArticle.newsSource && <span>📰 Source: {readingArticle.newsSource}</span>}
            </div>

            {readingArticle.headline && (
              <blockquote className="article-headline-quote">
                {readingArticle.headline}
              </blockquote>
            )}

            <div className="article-full-content">
              {readingArticle.newsContent}
            </div>

            {readingArticle.tags && readingArticle.tags.length > 0 && (
              <div className="article-tags-footer">
                <strong>Tags:</strong>
                {readingArticle.tags.map((t) => (
                  <span key={t.tagId} className="tag-chip">
                    #{t.tagName}
                  </span>
                ))}
              </div>
            )}
          </div>
        )}
      </Modal>
    </div>
  );
};

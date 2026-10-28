import React from 'react';

const MENU_ITEMS = [
  { key: 'dashboard', label: 'Dashboard' },
  { key: 'categories', label: 'Category' },
  { key: 'news', label: 'News' },
  { key: 'users', label: 'Users' },
  { key: 'settings', label: 'Settings' },
];

export default function Sidebar({ activeTab, onSelectTab }) {
  return (
    <aside className="admin-sidebar">
      <ul className="sidebar-menu">
        {MENU_ITEMS.map((item) => (
          <li key={item.key} className="sidebar-menu-item">
            <button
              type="button"
              className={`sidebar-link ${activeTab === item.key ? 'active' : ''}`}
              onClick={() => onSelectTab(item.key)}
            >
              {item.label}
            </button>
          </li>
        ))}
      </ul>
    </aside>
  );
}

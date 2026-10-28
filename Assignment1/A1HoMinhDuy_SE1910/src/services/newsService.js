export const DATA_KEY = 'funews.assignment01.data.v1';
export const ROLE_LABELS = { 1: 'Admin', 2: 'Staff' };
export const STATUS_LABELS = { 0: 'Inactive', 1: 'Active' };

function requireText(value, label) {
  const text = String(value ?? '').trim();
  if (!text) throw new Error(`${label} is required.`);
  return text;
}

function requireChoice(value, choices, label) {
  if (value === '' || value === null || value === undefined || !choices.includes(Number(value))) {
    throw new Error(`${label} is invalid.`);
  }
  return Number(value);
}

export function validateLogin(username, password) {
  if (!username.trim() || !password.trim()) return 'Username and password are required.';
  if (username !== 'Admin' || password !== 'Admin') return 'Incorrect username or password.';
  return '';
}

export function searchRecords(records, field, keyword) {
  const query = keyword.trim().toLowerCase();
  return records.filter((record) => String(record[field] ?? '').toLowerCase().includes(query));
}

// Return a new data object: React state and the source arrays are never mutated.
export function saveRecord(data, entity, values, id, currentUserId = 'user-1') {
  if (!['categories', 'news', 'users'].includes(entity)) throw new Error('Unknown entity.');
  const existing = id ? data[entity].find((item) => item.id === id) : null;
  if (id && !existing) throw new Error('Record no longer exists.');
  const status = requireChoice(values.status, [0, 1], 'Status');
  let fields;
  if (entity === 'categories') {
    fields = { name: requireText(values.name, 'Name'), status };
  } else if (entity === 'news') {
    const title = requireText(values.title, 'Title');
    const content = requireText(values.content, 'Content');
    if (!data.categories.some((category) => category.id === values.categoryId)) {
      throw new Error('Please select an existing category.');
    }
    const createdBy = existing?.createdBy ?? currentUserId;
    if (!data.users.some((user) => user.id === createdBy)) throw new Error('Author no longer exists.');
    fields = { title, content, categoryId: values.categoryId, createdBy, status };
  } else {
    const username = requireText(values.username, 'Username');
    if (data.users.some((user) => user.id !== id && user.username.toLowerCase() === username.toLowerCase())) {
      throw new Error('Username already exists.');
    }
    const role = requireChoice(values.role, [1, 2], 'Role');
    // Empty password on Edit means keep the existing mock password.
    const password = values.password === '' && existing ? existing.password : String(values.password ?? '');
    requireText(password, 'Password');
    if (id === 'user-1' && (username !== 'Admin' || password !== 'Admin' || role !== 1 || status !== 1)) {
      throw new Error('The required Admin/Admin account must remain active with the Admin role.');
    }
    fields = { username, password, role, status };
  }
  const record = { id: existing?.id ?? crypto.randomUUID(), ...fields };
  return {
    ...data,
    [entity]: existing
      ? data[entity].map((item) => item.id === id ? record : item)
      : [...data[entity], record],
  };
}

export function deleteRecord(data, entity, id) {
  if (!['categories', 'news', 'users'].includes(entity)) throw new Error('Unknown entity.');
  if (!data[entity].some((item) => item.id === id)) throw new Error('Record no longer exists.');
  if (entity === 'categories' && data.news.some((article) => article.categoryId === id)) {
    throw new Error('This category is used by news. Move or delete that news first.');
  }
  if (entity === 'users' && id === 'user-1') throw new Error('The required Admin account cannot be deleted.');
  if (entity === 'users' && data.news.some((article) => article.createdBy === id)) {
    throw new Error('This user is the author of news. Delete that news first.');
  }
  return { ...data, [entity]: data[entity].filter((item) => item.id !== id) };
}

// Validate stored data before it reaches forms/tables, including references.
export function isValidData(data) {
  if (!data || !['categories', 'news', 'users'].every((key) => Array.isArray(data[key]))) return false;
  const text = (value) => typeof value === 'string' && value.trim().length > 0;
  const common = (list) => list.every((item) => item && text(item.id) && [0, 1].includes(item.status))
    && new Set(list.map((item) => item.id)).size === list.length;
  if (![data.categories, data.news, data.users].every(common)) return false;
  if (!data.categories.every((item) => text(item.name))) return false;
  if (!data.users.every((item) => text(item.username) && text(item.password) && [1, 2].includes(item.role))) return false;
  if (new Set(data.users.map((item) => item.username.toLowerCase())).size !== data.users.length) return false;
  if (!data.users.some((item) => item.id === 'user-1' && item.username === 'Admin' && item.password === 'Admin' && item.role === 1 && item.status === 1)) return false;
  return data.news.every((item) => text(item.title) && text(item.content)
    && data.categories.some((category) => category.id === item.categoryId)
    && data.users.some((user) => user.id === item.createdBy));
}

export function loadData(storage, seedFactory) {
  try {
    const raw = storage.getItem(DATA_KEY);
    if (raw === null) return { data: seedFactory(), warning: '' };
    const data = JSON.parse(raw);
    if (!isValidData(data)) throw new Error('Invalid data');
    return { data, warning: '' };
  } catch {
    return { data: seedFactory(), warning: 'Saved data could not be read. Sample data is loaded in memory; changes will replace the saved data.' };
  }
}

export function persistData(storage, data) {
  // Throw on failure so the UI does not claim that data has been saved.
  storage.setItem(DATA_KEY, JSON.stringify(data));
}

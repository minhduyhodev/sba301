import test from 'node:test';
import assert from 'node:assert/strict';
import { createSeedData } from '../src/data/seed.js';
import { DATA_KEY, validateLogin, searchRecords, saveRecord, deleteRecord, loadData, persistData, isValidData } from '../src/services/newsService.js';

test('Login: empty, wrong user, wrong password, case-sensitive Admin/Admin', () => {
  for (const [username, password] of [['', ''], ['Wrong', 'Admin'], ['Admin', 'Wrong'], ['admin', 'Admin']]) {
    assert.ok(validateLogin(username, password));
  }
  assert.equal(validateLogin('Admin', 'Admin'), '');
});

for (const [entity, field, values] of [
  ['categories', 'name', { name: ' Science ', status: '1' }],
  ['news', 'title', { title: ' Science ', content: 'News body', categoryId: 'category-1', status: '1' }],
  ['users', 'username', { username: ' Science ', password: 'mock123', role: '2', status: '1' }],
]) {
  test(`${entity}: create/edit/delete by id without mutating source; search normalizes and preserves list`, () => {
    const original = createSeedData();
    const before = structuredClone(original);
    const created = saveRecord(original, entity, values);
    const record = created[entity].at(-1);
    assert.equal(record[field], 'Science');
    assert.equal(created[entity].length, original[entity].length + 1);
    assert.deepEqual(original, before);
    assert.deepEqual(searchRecords(created[entity], field, ' SCIENCE '), [record]);
    assert.deepEqual(searchRecords(created[entity], field, 'zzzzzz'), []);
    assert.equal(searchRecords(created[entity], field, '').length, created[entity].length);
    const updated = saveRecord(created, entity, { ...record, [field]: 'Updated' }, record.id);
    assert.equal(updated[entity].at(-1)[field], 'Updated');
    assert.equal(created[entity].at(-1)[field], 'Science');
    assert.deepEqual(updated[entity].slice(0, -1), original[entity]);
    const removed = deleteRecord(updated, entity, record.id);
    assert.deepEqual(removed, original);
  });
  test(`${entity}: rejects missing required text and invalid status`, () => {
    const data = createSeedData();
    assert.throws(() => saveRecord(data, entity, { ...values, [field]: '  ' }), /required/);
    assert.throws(() => saveRecord(data, entity, { ...values, status: 3 }), /invalid/);
    assert.throws(() => saveRecord(data, entity, { ...values, status: '' }), /invalid/);
    assert.throws(() => saveRecord(data, entity, values, 'missing'), /no longer exists/);
  });
}

test('News requires content and an existing category; edit retains author', () => {
  const data = createSeedData();
  assert.throws(() => saveRecord(data, 'news', { ...data.news[0], content: '' }), /Content/);
  assert.throws(() => saveRecord(data, 'news', { ...data.news[0], categoryId: 'missing' }), /category/);
  const updated = saveRecord(data, 'news', { ...data.news[1], createdBy: 'user-1' }, 'news-2');
  assert.equal(updated.news[1].createdBy, 'user-2');
});

test('Users: unique username, required password, valid role, blank edit password retains value', () => {
  const data = createSeedData();
  assert.throws(() => saveRecord(data, 'users', { username: ' admin ', password: 'abc', role: 2, status: 1 }), /already exists/);
  assert.throws(() => saveRecord(data, 'users', { username: 'New', password: ' ', role: 2, status: 1 }), /Password/);
  assert.throws(() => saveRecord(data, 'users', { username: 'New', password: 'abc', role: 3, status: 1 }), /Role/);
  assert.equal(saveRecord(data, 'users', { ...data.users[1], password: '' }, 'user-2').users[1].password, 'Staff');
});

test('References and fixed Admin are protected when deleting/editing', () => {
  const data = createSeedData();
  assert.throws(() => deleteRecord(data, 'categories', 'category-1'), /used by news/);
  assert.throws(() => deleteRecord(data, 'users', 'user-2'), /author/);
  assert.throws(() => deleteRecord(data, 'users', 'user-1'), /cannot be deleted/);
  assert.throws(() => saveRecord(data, 'users', { ...data.users[0], status: 0 }, 'user-1'), /must remain/);
});

test('Delete first, middle, last, and only unreferenced records', () => {
  for (const id of ['a', 'b', 'c']) {
    const data = { ...createSeedData(), news: [], categories: ['a', 'b', 'c'].map((id) => ({ id, name: id, status: 1 })) };
    assert.deepEqual(deleteRecord(data, 'categories', id).categories.map((item) => item.id), ['a', 'b', 'c'].filter((item) => item !== id));
  }
  const data = { ...createSeedData(), news: [], categories: [{ id: 'only', name: 'Only', status: 1 }] };
  assert.deepEqual(deleteRecord(data, 'categories', 'only').categories, []);
});

test('Storage first-run, round trip, malformed JSON, invalid shape/reference and write failure', () => {
  const map = new Map();
  const storage = { getItem: (key) => map.get(key) ?? null, setItem: (key, value) => map.set(key, value) };
  assert.deepEqual(loadData(storage, createSeedData).data, createSeedData());
  const data = saveRecord(createSeedData(), 'categories', { name: 'Saved', status: 1 });
  persistData(storage, data);
  assert.deepEqual(loadData(storage, createSeedData), { data, warning: '' });
  for (const raw of ['{broken', '{}', 'null', JSON.stringify({ ...data, categories: [] })]) {
    map.set(DATA_KEY, raw);
    assert.ok(loadData(storage, createSeedData).warning);
  }
  assert.equal(isValidData(createSeedData()), true);
  assert.equal(isValidData({ ...data, users: [] }), false);
  assert.throws(() => persistData({ setItem: () => { throw new Error('Quota exceeded'); } }, data), /Quota/);
});

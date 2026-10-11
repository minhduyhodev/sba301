// Assignment 01 uses mock data only. These passwords are demonstration values.
export function createSeedData() {
  return {
    categories: [
      { id: 'category-1', name: 'University', status: 1 },
      { id: 'category-2', name: 'Student activities', status: 1 },
      { id: 'category-3', name: 'Other', status: 0 },
    ],
    news: [
      { id: 'news-1', title: 'Welcome to the new semester', content: 'Students can view the new semester schedule at the academic office.', categoryId: 'category-1', createdBy: 'user-1', status: 1 },
      { id: 'news-2', title: 'Student club registration', content: 'Registration for student clubs opens this week.', categoryId: 'category-2', createdBy: 'user-2', status: 0 },
    ],
    users: [
      { id: 'user-1', username: 'Admin', password: 'Admin', role: 1, status: 1 },
      { id: 'user-2', username: 'Staff', password: 'Staff', role: 2, status: 1 },
    ],
  };
}

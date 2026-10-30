import axios from 'axios';

const axiosClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosClient.interceptors.request.use((config) => {
  const userJson = localStorage.getItem('currentUser');
  if (userJson) {
    try {
      const user = JSON.parse(userJson);
      if (user.accountId) {
        config.headers['X-User-Id'] = user.accountId;
      }
      if (user.accountRole) {
        config.headers['X-User-Role'] = user.accountRole;
      }
    } catch (e) {
      console.error('Failed to parse current user', e);
    }
  }
  return config;
});

export default axiosClient;

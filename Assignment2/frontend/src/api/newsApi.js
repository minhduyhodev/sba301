import axiosClient from './axiosClient';

export const newsApi = {
  getPublic: async (keyword, categoryId) => {
    const params = {};
    if (keyword) params.keyword = keyword;
    if (categoryId) params.categoryId = categoryId;
    const res = await axiosClient.get('/news/public', { params });
    return res.data;
  },
  getPublicById: async (id) => {
    const res = await axiosClient.get(`/news/public/${id}`);
    return res.data;
  },
  getAllForStaff: async (keyword, categoryId) => {
    const params = {};
    if (keyword) params.keyword = keyword;
    if (categoryId) params.categoryId = categoryId;
    const res = await axiosClient.get('/news', { params });
    return res.data;
  },
  getHistory: async () => {
    const res = await axiosClient.get('/news/history');
    return res.data;
  },
  getById: async (id) => {
    const res = await axiosClient.get(`/news/${id}`);
    return res.data;
  },
  create: async (data) => {
    const res = await axiosClient.post('/news', data);
    return res.data;
  },
  update: async (id, data) => {
    const res = await axiosClient.put(`/news/${id}`, data);
    return res.data;
  },
  delete: async (id) => {
    const res = await axiosClient.delete(`/news/${id}`);
    return res.data;
  },
};

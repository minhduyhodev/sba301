import axiosClient from './axiosClient';

export const categoryApi = {
  getAll: async (keyword) => {
    const params = keyword ? { keyword } : {};
    const res = await axiosClient.get('/categories', { params });
    return res.data;
  },
  getActive: async () => {
    const res = await axiosClient.get('/categories/active');
    return res.data;
  },
  getById: async (id) => {
    const res = await axiosClient.get(`/categories/${id}`);
    return res.data;
  },
  create: async (data) => {
    const res = await axiosClient.post('/categories', data);
    return res.data;
  },
  update: async (id, data) => {
    const res = await axiosClient.put(`/categories/${id}`, data);
    return res.data;
  },
  delete: async (id) => {
    const res = await axiosClient.delete(`/categories/${id}`);
    return res.data;
  },
};

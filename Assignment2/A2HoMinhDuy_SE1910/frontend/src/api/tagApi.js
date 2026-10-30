import axiosClient from './axiosClient';

export const tagApi = {
  getAll: async (keyword) => {
    const params = keyword ? { keyword } : {};
    const res = await axiosClient.get('/tags', { params });
    return res.data;
  },
  create: async (data) => {
    const res = await axiosClient.post('/tags', data);
    return res.data;
  },
  update: async (id, data) => {
    const res = await axiosClient.put(`/tags/${id}`, data);
    return res.data;
  },
  delete: async (id) => {
    const res = await axiosClient.delete(`/tags/${id}`);
    return res.data;
  },
};

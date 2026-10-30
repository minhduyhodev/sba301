import axiosClient from './axiosClient';

export const accountApi = {
  getAll: async (keyword) => {
    const params = keyword ? { keyword } : {};
    const res = await axiosClient.get('/accounts', { params });
    return res.data;
  },
  getById: async (id) => {
    const res = await axiosClient.get(`/accounts/${id}`);
    return res.data;
  },
  create: async (data) => {
    const res = await axiosClient.post('/accounts', data);
    return res.data;
  },
  update: async (id, data) => {
    const res = await axiosClient.put(`/accounts/${id}`, data);
    return res.data;
  },
  delete: async (id) => {
    const res = await axiosClient.delete(`/accounts/${id}`);
    return res.data;
  },
};

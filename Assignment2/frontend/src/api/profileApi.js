import axiosClient from './axiosClient';

export const profileApi = {
  get: async (id) => {
    const res = await axiosClient.get(`/profile/${id}`);
    return res.data;
  },
  update: async (id, data) => {
    const res = await axiosClient.put(`/profile/${id}`, data);
    return res.data;
  },
};

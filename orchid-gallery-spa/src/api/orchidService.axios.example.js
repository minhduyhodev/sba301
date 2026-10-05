// src/api/orchidService.axios.example.js
import { apiClient } from './apiClient';

/**
 * Phiên bản tham khảo sử dụng Axios để so sánh với Fetch API:
 * 1. Axios tự động parse JSON và trả về trong response.data (Fetch cần await response.json()).
 * 2. Axios tự động reject Promise khi status code nằm ngoài dải 2xx (Fetch cần kiểm tra response.ok).
 * 3. Axios hỗ trợ interceptors, timeout cấu hình tập trung.
 */
export async function getOrchidsByAxios() {
  const response = await apiClient.get('/orchids.json');
  return response.data;
}

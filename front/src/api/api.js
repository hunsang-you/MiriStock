import axios from 'axios';

const BASE_URL = 'http://192.168.31.160:80';
const token =
  'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb2d5ZW9tMkBrYWthby5jb20iLCJyb2xlIjoiTUVNQkVSIiwiZXhwIjoxNjc1Mzg3Mzc5fQ.oNnTWUfpoj5gQRrkORvGJ4HP6p-NrCHue2PgdT1t-cs';
export const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  },
});

export default api;

export const memberAPI = {
  test: () => api.get(`/login/oauth2/code/kakao`),
};

export const rankAPI = {
  increase: () => api.get(`/stockdata/rate/increase/${20210112}`),
  decrease: () => api.get(`/stockdata/rate/decrease/${20210112}`),
  capital: () => api.get(`/stockdata/amount/top`),
};

export const stockAPI = {
  stockDetail: (stockCode, startDate, endDate) =>
    api.get(`stockdata/detail`, {
      params: { stockCode: stockCode, startDate: startDate, endDate: endDate },
    }),
};

import axios from 'axios';

const BASE_URL = process.env.REACT_APP_BASE_UR;
const accessToken = localStorage.getItem('accessToken');
const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    Authorization: `Bearer ${accessToken}`,
  },
});

export default api;

export const memberAPI = {
  asset: () => api.get(`/asset`),
};

export const rankAPI = {
  increase: () => api.get(`/stockdata/rate/increase/${20210112}`),
  decrease: () => api.get(`/stockdata/rate/decrease/${20210112}`),
  capital: () => api.get(`/stockdata/amount/top`), //백에서 미완성
};

export const stockAPI = {
  stockDetail: (stockCode, startDate, endDate) =>
    api.get(`stockdata/detail`, {
      params: { stockCode: stockCode, startDate: startDate, endDate: endDate },
    }),
};

export const searchAPI = {
  serachStock: (stock) =>
    api.get(`stock/search`, { params: { stockName: stock } }),
};

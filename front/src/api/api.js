import axios from 'axios';

const BASE_URL = process.env.REACT_APP_BASE_UR;
// const accessToken = localStorage.getItem('accessToken');
const accessToken =
  'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMmRidGtkQG5hdmVyLmNvbSIsInJvbGUiOiJNRU1CRVIiLCJleHAiOjE2NzU3Njc0OTl9.i5Ufnu5sEoW5MXpWfrWkgOWirtcgalccwd9E-G9W2Sk';
export const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    Authorization: `Bearer ${accessToken}`,
  },
  withCredentials: true,
});

export default api;

export const memberAPI = {
  asset: () => api.get(`/asset`),
};

export const rankAPI = {
  increase: (date) => api.get(`/stockdata/rate/increase/${date}`),
  decrease: (date) => api.get(`/stockdata/rate/decrease/${date}`),
  todayTop: (date) => api.get(`/stockdata/amount/top/${date}`), //백에서 미완성
};

export const stockAPI = {
  financialStatement: (stockCode) =>
    api.get(`/info/financialstatement/${stockCode}`),
  stockDetail: (stockCode, startDate, endDate) =>
    api.get(`/stockdata/detail`, {
      params: { stockCode: stockCode, startDate: startDate, endDate: endDate },
    }),
};

export const searchAPI = {
  serachStock: (stock) =>
    api.get(`/stock/search`, { params: { keyword: stock } }),
};

export const communityAPI = {};

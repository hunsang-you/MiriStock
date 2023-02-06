import axios from 'axios';

const BASE_URL = process.env.REACT_APP_BASE_UR;
// const accessToken = localStorage.getItem('accessToken');
const accessToken =
  'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0a2RndXNkbDYzQG5hdmVyLmNvbSIsInJvbGUiOiJNRU1CRVIiLCJuaWNrbmFtZSI6IuyCvOyEsSDqsKTrn63si5wg7KKL7JWE7JqUIiwiZXhwIjoxNjc1NzMyMTM1fQ.vzdbQSMm6wlyg3PuGCqvx53oMg8DN4Cqbz-xWDc0AvQ';
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

export const communityAPI = {
  getCom: () => api.get(`/qna`),
  createCom: (title, content) =>
    api.post(`/qna`, { articleTitle: title, articleContent: content }),

  createComment: (articleId, content) =>
    api.post(`/qna/comment`, { articleNo: articleId, commentContent: content }),
  getComment: (articleId) => api.get(`/qna/comment`, { articleNo: articleId }),
};

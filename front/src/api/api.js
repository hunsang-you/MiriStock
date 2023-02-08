import axios from 'axios';

const BASE_URL = process.env.REACT_APP_BASE_UR;
const accessToken = localStorage.getItem('accessToken');
console.log(accessToken);
export const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    Authorization: `Bearer ${accessToken}`,
  },
  withCredentials: true,
});

api.interceptors.response.use(function (response) {
  console.log(response);
  return response;
});

export default api;

export const memberAPI = {
  asset: () => api.get(`/asset`),
  stocks: () => api.get(`/asset/memberstock`),
  intersetStocks: (date) => api.get(`/asset/intereststock/${date}`), //확인안됨
};

export const rankAPI = {
  increase: (date) => api.get(`/stockdata/rate/increase/${date}`),
  decrease: (date) => api.get(`/stockdata/rate/decrease/${date}`),
  todayTop: (date) => api.get(`/stockdata/amount/top/${date}`),
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
  createSearchHis: (stockName, stockCode) =>
    api.post(`/searchrecord`, { stockName: stockName, stockCode: stockCode }),
  getSearchHis: () => api.get(`/searchrecord/list`),
  deleteSearchHis: (searchNo) => api.delete(`/searchrecord/${searchNo}`),
};

export const communityAPI = {
  getArticle: () => api.get(`/qna`),
  createArticle: (title, content) =>
    api.post(`/qna`, { articleTitle: title, articleContent: content }),
  deleteArticle: (articleId) =>
    api.delete(`/qna`, { params: { articleNo: articleId } }),
  createComment: (articleId, content) =>
    api.post(`/qna/comment`, { articleNo: articleId, commentContent: content }),
  getComment: (articleId) => api.get(`/qna/comment`, { articleNo: articleId }),
};

export const tradeAPI = {
  getAllTrades: (types) =>
    api.get(`/stockdeal`, { params: { stockdealtype: types } }),
  checkTrades: () => api.get(`/limitpriceorder`),
  buyStock: (
    stockCode,
    stockName,
    memberNo,
    limitPriceOrderPrice,
    limitPriceOrderAmount,
    type,
  ) =>
    api.post(`/limitpriceorder`, {
      stockCode: stockCode,
      stockName: stockName,
      memberNo: memberNo,
      limitPriceOrderPrice: limitPriceOrderPrice,
      limitPriceOrderAmount: limitPriceOrderAmount,
      limitPriceOrderType: type,
    }),
};

export const newsAPI = {
  getNews: (stockCode, today) =>
    api.post(`/info/news`, {
      searchKeyword: stockCode,
      startDate: today,
      endDate: today + 1,
    }),
};

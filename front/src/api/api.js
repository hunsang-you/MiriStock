import axios from 'axios';

const BASE_URL = 'https://miristockserverurl/api';
// const BASE_URL = process.env.REACT_APP_BASE_UR;
const accessToken = localStorage.getItem('accessToken');

export const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(function (config) {
  const token = localStorage.getItem('accessToken');
  config.headers.Authorization = token;

  return config;
});

//release 빌드
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (err) => {
    const originalReq = err.config;
    if (err.response.headers.authorization) {
      localStorage.setItem('accessToken', err.response.headers.authorization);
      originalReq.headers['Authorization'] = err.response.headers.authorization;
      return axios(originalReq);
    } else if (err.response.status === 401) {
      return window.location.replace(`https://miristockserverurl/login`);
    }
    return err;
  },
);

export default api;

export const memberAPI = {
  asset: () => api.get(`/asset`),
  stocks: (type) => api.get(`/asset/memberstock`, { params: { type: type } }),
  stock: (stockcode) => api.get(`/asset/memberstock/${stockcode}`),
  intersetStocks: (date) => api.get(`/asset/intereststock/${date}`),
  addIntersetStocks: (stockcode) =>
    api.post(`/asset/intereststock`, {}, { params: { stockcode: stockcode } }),
  deleteIntersetStocks: (stockcode) =>
    api.delete(`/asset/intereststock`, { params: { stockcode: stockcode } }),
  assetChanged: () => api.get(`/asset/memberstock/main`),
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
    api.get(`/stockdata/search`, { params: { keyword: stock } }),
  createSearchHis: (stockName, stockCode) =>
    api.post(`/searchrecord`, { stockName: stockName, stockCode: stockCode }),
  getSearchHis: () => api.get(`/searchrecord/list`),
  deleteSearchHis: (stockCode) => api.delete(`/searchrecord/${stockCode}`),
};

export const communityAPI = {
  getArticle: () => api.get(`/qna`),
  createArticle: (title, content) =>
    api.post(`/qna`, { articleTitle: title, articleContent: content }),
  deleteArticle: (articleId) => api.delete(`/qna/${articleId}`),
  createComment: (articleId, content) =>
    api.post(`/qna/comment`, { articleNo: articleId, commentContent: content }),
  getComment: (articleId) => api.get(`/qna/comment`, { articleNo: articleId }),
  searchArticle: (title, content) =>
    api.get(`/qna/search`, {
      params: { keyword: title, content },
    }),
  updateArticle: (articleNo, title, content) =>
    api.put(`/qna`, {
      articleNo: articleNo,
      articleTitle: title,
      articleContent: content,
    }),
  deleteComment: (commentNo) => api.delete(`/qna/comment/${commentNo}`),
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
  sellStock: (
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
  updateOrder: (
    limitPriceOrderNo,
    stockCode,
    stockName,
    memberNo,
    limitPriceOrderPrice,
    limitPriceOrderAmount,
    limitPriceOrderType,
  ) =>
    api.put(`/limitpriceorder`, {
      limitPriceOrderNo: limitPriceOrderNo,
      stockCode: stockCode,
      stockName: stockName,
      memberNo: memberNo,
      limitPriceOrderPrice: limitPriceOrderPrice,
      limitPriceOrderAmount: limitPriceOrderAmount,
      limitPriceOrderType: limitPriceOrderType,
    }),
  deleteStockOrder: (limitPriceOrderNo) =>
    api.delete(`/limitpriceorder/${limitPriceOrderNo}`),
  stockData: (stockCode) => api.get(`/asset/memberstock/${stockCode}`),
};

export const newsAPI = {
  getNews: (stockCode, today) =>
    api.post(`/info/news`, {
      searchKeyword: stockCode,
      startDate: today,
      endDate: today + 1,
    }),
};

export const simulAPI = {
  changeDate: (targetDate) => api.put(`/simulation/member/time/${targetDate}`),
  currentDate: () => api.get(`/simulation/member/time`),
  restart: () => api.post(`/simulation/restart`), // 시뮬재시작
  theEnd: () => api.post(`/simulation/end`), //종료 및 포트폴리오
};

// 회원정보
export const profileAPI = {
  nicknameCheck: (memberEmail) =>
    api.post(`/member/nicknamecheck`, {
      memberEmail: memberEmail,
    }),
  nicknameChange: (email, nickname) =>
    api.put(`/member/nickname`, {
      memberEmail: email,
      memberNickname: nickname,
    }),
  deleteMember: (memberEmail) =>
    api.delete(`/member`, { params: { memberEmail: memberEmail } }),
};


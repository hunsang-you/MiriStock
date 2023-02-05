import axios from 'axios';

const BASE_URL = 'http://59.27.27.137:21111/';
const token =
  'RSPgx-GPDXxB9T7kOBzo-s6i3_I-1LiBWp3tVowE5Om9A4H_goLQN5sCtPugj90_FlfNDgo9cpgAAAGGISaxuw&state=7a8j2jTc09hOBhtfIjwFF7fxrNp9w1YPR7y6xxGpPdg%3D';
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

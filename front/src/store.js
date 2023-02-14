import { create } from 'zustand';
import { persist } from 'zustand/middleware';

const navStore = create(
  persist(
    (set) => ({
      page: '',
      setPage: (url) => {
        set((state) => ({ page: url }));
      },
    }),
    { name: 'navStore' },
  ),
);

//멤버정보 저장
const userStore = create(
  persist(
    (set) => ({
      user: {},
      setUser: (userInfo) => {
        set((state) => ({ user: userInfo }));
      },
    }),
    { name: 'userStore' },
  ),
);
//Datestore
const dateStore = create(
  persist(
    (set) => ({
      date: [],
      setDate: (userDate) => {
        set((state) => ({ date: userDate }));
      },
    }),
    { name: 'dateStore' },
  ),
);
//보유주식 저장

// 유저 정보
const memberStore = create(
  persist(
    (set) => ({
      info: [],
      setInfo: (memberInfo) => {
        set((state) => ({ info: memberInfo }));
      },
    }),
    { name: 'memberStore' },
  ),
);

const favoriteStore = create(
  persist(
    (set) => ({
      favoriteStocks: [],
      setFavoriteStocks: (favoriteStocks) => {
        set((state) => ({ favoriteStocks: favoriteStocks }));
      },
    }),
    { name: 'favoriteStore' },
  ),
);

export { navStore, userStore, dateStore, memberStore, favoriteStore };

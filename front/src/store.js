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

//검색기록 스토리지용
const insertWatchData = (history, data) => {
  history.push(data);
  const set = new Set(history);
  const result = [...set];
  return result;
};

const searchStore = create(
  persist(
    (set) => ({
      searchHistory: [],
      setSearchHistory: (data) => {
        set((state) => ({
          searchHistory: insertWatchData(state.searchHistory, data),
        }));
      },
    }),
    { name: 'searchStore' },
  ),
);

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

export { navStore, searchStore, userStore, dateStore, memberStore };

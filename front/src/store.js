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

export { navStore, searchStore, userStore };

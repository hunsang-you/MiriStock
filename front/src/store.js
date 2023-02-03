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

const searchHistoryStore = create(
  persist(
    (set) => ({
      searchHistory: [],
      setsearchHistory: (data) => {
        set((state) => ({ searchHistory: insertWatchData(state, data) }));
      },
    }),
    { name: 'watchDataStore' },
  ),
);
const insertWatchData = (state, data) => {
  console.log(state);
  console.log(data);
};

export { navStore, searchHistoryStore };

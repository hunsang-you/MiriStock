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

const insertWatchData = (history, data) => {
  history.push(data);
  const set = new Set(history);
  const result = [...set];
  return result;
};

// const searchHistoryStore = create(
//   persist(
//     (set) => ({
//       searchHistory: [],
//       setsearchHistory: (data) => {
//         set((state) => ({
//           searchHistory: insertWatchData(state.searchHistory, data),
//         }));
//       },
//     }),
//     { name: 'watchDataStore' },
//   ),
// );

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

export { navStore, searchStore };

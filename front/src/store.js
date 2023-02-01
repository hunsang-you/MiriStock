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

// const useStore = create(
//   persist(
//     (set) => ({
//       watchList: [],
//       setWatchList: (select) => {
//         set((state) => ({ ...state, watchList: select }));
//       },
//     }),
//     { name: 'watchStore' },
//   ),
// );

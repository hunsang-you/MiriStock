import { create } from 'zustand';
import { persist } from 'zustand/middleware';


const testStore = create(
    persist(
        set => ({
            datas : [],
            insertData : (request) => {
                set(state => ({ datas : insertDatas(state.datas , request)}))
            }
        }),
        { name : "dataStore"}
    )
);

const insertDatas = (datas, request) => {
    console.log(request);
    const newDatas = [...datas, ...request];
    console.log(newDatas.length)
    return newDatas
}

export {testStore}
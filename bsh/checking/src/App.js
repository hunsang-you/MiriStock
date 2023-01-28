
import './App.css';
import { testStore } from './store.js';
import { movieAPI } from './api.js';
import { useState } from 'react';

function App() {
  const { datas, insertData} = testStore(state => state);
  let [ searchMovies, setSearchMovies ] = useState([]);

  return (
    <div className="App">
      <div className="black-nav">
        <h4 style= { { color : 'blue'}}>서버통신테스트용</h4>
      </div>
      <input type="text" onChange={(e) => {
        movieAPI.getSearch(e.target.value)
        .then((request)=>{
          setSearchMovies(request.data);
        })
        .catch((err)=>{
          console.log(err);
        })
      }}></input>
      <p>{
        searchMovies.length !== 0 ? searchMovies.map((data,i)=>{
          return (
            <div key={i}>
              <h3>{data.title}</h3>
            </div>
          )
        }) : '데이터없음'
      }</p>
      <button>
        영화디테일정보
      </button>
      <button onClick={()=>{
        movieAPI.getMovies()
        .then((request)=>{
          insertData(request.data);
        })
        .catch((err) => {
          console.log(err);
        })
      }}>정보받아오기</button>
        {
          datas.length !== 0 ? datas.map((data,i)=>{
            return (
              <div key={i}>
                <p>{i}</p>
                <p>{data.title},,,{data.id}</p>
              </div>
            )
          }) : 'no-data'
        }
    </div>
  );
}

export default App;

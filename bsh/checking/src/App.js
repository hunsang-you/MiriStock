
import './App.css';
import { testStore } from './store.js';
import axios from 'axios';

function App() {
  const { datas, insertData} = testStore(state => state);
  const number = 0;
  return (
    <div className="App">
      <div className="black-nav">
        <h4 style= { { color : 'blue'}}>서버통신테스트용</h4>
      </div>
      <button onClick={()=>{
        axios.get('http://127.0.0.1:8000/movies/')
        .then((request)=>{
          insertData(request.data);
        })
        .catch((err) => {
          console.log(err);
        })
      }}>정보받아오기</button>
      <div>
        {
          datas.length !== 0 ? datas.map((data,i)=>{
            return (
              <div key={i}>
                <p>{i}</p>
                <p>{data.title}</p>
              </div>
            )
          }) : 'no-data'
        }
      </div>
    </div>
  );
}

export default App;

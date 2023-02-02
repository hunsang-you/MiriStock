import { TextField } from '@mui/material';
import { AiOutlineSearch } from 'react-icons/ai';
import SearchView from './SearchView';
import History from './History';
import { searchAPI } from '../../api/api';
import { useState } from 'react';

// import { useStore } from '../../store';

import './css/SearchBar.css';

// 키워드, 결과값들, 업데이트필드를 전달받는다
const SearchBar = ({ keyword, results, updateField }) => {
  //
  const [searchResult, setSearchResult] = useState([]);
  const updateText = (text) => {
    //console.log('update text', text);
    updateField('keyword', text, false);
    updateField('results', []);
  };

  let renderResults;
  let InputStk = [];
  if (InputStk) {
    // InputStk 에 검색어에 대한 결과가 담기면, SearchView 호출
    renderResults = InputStk.map((stock, i) => {
      return (
        <div key={i}>
          <SearchView
            updateText={updateText}
            name={stock.stockName}
            code={stock.stockCode}
            key={stock.stockCode}
          />
        </div>
      );
    });
  }

  // 검색 입력이 없으면 최근 조회한 항목 표시
  const Recent = () => {
    if ((keyword === '') | (results.length === 0)) {
      return (
        <div>
          <History />
        </div>
      );
    }
  };

  // const { watchData, setWatchData } = useStore(); // zustand 전역변수

  // onChange를 사용하여 글자를 입력할때마다 updateField호출, renderResults 렌더링.
  return (
    <div className="search-bar">
      <div className="search-top">
        <AiOutlineSearch size={40} />
        <div className="text-field">
          <TextField
            sx={{ width: { xs: 300, sm: 540, md: 720, lg: 960, xl: 1140 } }}
            id="search-bar"
            placeholder="종목명 또는 종목코드 입력"
            variant="standard"
            value={keyword}
            onChange={(e) => {
              searchAPI
                .serachStock(e.target.value)
                .then((request) => {
                  console.log(request.data);
                  setSearchResult(request.data);
                })
                .catch((err) => console.log(err));
            }}
          />
        </div>
      </div>
      {searchResult.map((stock, i) => {
        return (
          <div key={i}>
            <SearchView
              updateText={updateText}
              name={stock.stockName}
              code={stock.stockCode}
              key={stock.stockCode}
            />
          </div>
        );
      })}
      <div className="search-stocks">{renderResults}</div>
      <div className="search-list">{Recent()}</div>
    </div>
  );
};

export default SearchBar;

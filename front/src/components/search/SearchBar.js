import { TextField } from '@mui/material';
import { AiOutlineSearch } from 'react-icons/ai';
import SearchView from './SearchView';
import History from './History';
import { searchAPI } from '../../api/api';
import { useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
// import { useStore } from '../../store';

import './css/SearchBar.css';

// 키워드, 결과값들, 업데이트필드를 전달받는다
const SearchBar = () => {
  //
  const [searchResult, setSearchResult] = useState([]);
  let infiScroll = [];
  let start = 0;
  const getData = () => {
    if (searchResult.length < start + 10) {
      infiScroll = searchResult.slice(start, searchResult.length);
    } else {
      infiScroll = searchResult.slice(start, start + 10);
    }
  };
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
            onChange={(e) => {
              const test = async () => {
                await searchAPI
                  .serachStock(e.target.value)
                  .then((request) => {
                    setSearchResult(request.data);
                  })
                  .catch((err) => console.log(err));
              };
              test();
            }}
          />
        </div>
      </div>
      {searchResult.map((stock, i) => {
        return (
          <div key={i}>
            <SearchView
              name={stock.stockName}
              code={stock.stockCode}
              key={stock.stockCode}
            />
          </div>
        );
      })}
      {/* <div className="search-stocks">{renderResults}</div>
      <div className="search-list">{Recent()}</div> */}
    </div>
  );
};

export default SearchBar;

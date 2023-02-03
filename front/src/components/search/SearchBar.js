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
const SearchBar = ({ keyword, updateField }) => {
  //
  const [searchResult, setSearchResult] = useState([]);
  const updateText = (text) => {
    //console.log('update text', text);
    updateField('keyword', text, false);
    updateField('results', []);
  };

  // 검색 입력이 없으면 최근 조회한 항목 표시

  // const { watchData, setWatchData } = useStore();
  const [watchData, setWatchData] = useState([]);

  // 하단 조건부렌더링 bool 체크
  const [isCheck, setIsCheck] = useState(true);

  // isCheck == true(검색결과가 0개라면) 최근 조회목록 출력
  const HistoryView = () => {
    if (isCheck === true) {
      return <History watchData={watchData} />;
    }
  };
  // onChange를 사용하여 글자를 입력할때마다 updateField호출, renderResults 렌더링.
  return (
    <div className="search-bar">
      <div className="search-top">
        <AiOutlineSearch size={40} />
        <div className="text-field">
          {/* 검색 textfield */}
          <TextField
            sx={{ width: { xs: 300, sm: 540, md: 720, lg: 960, xl: 1140 } }}
            id="search-bar"
            placeholder="종목명 또는 종목코드 입력"
            variant="standard"
            onChange={(e) => {
              searchAPI
                .serachStock(e.target.value)
                .then((request) => {
                  // 검색어O -> 결과 출력, 검색어X 검색 결과 초기화
                  if (e.target.value.length > 0) {
                    setIsCheck(false);
                    setSearchResult(request.data);
                  } else {
                    setIsCheck(true);
                    setSearchResult([]);
                  }
                })
                .catch((err) => console.log(err));
            }}
          />
        </div>
      </div>

      {/* 종목 검색 결과 */}
      <div className="search-title"></div>
      {searchResult.map((stock, i) => {
        return (
          <div
            key={i}
            onClick={(e) => {
              setWatchData([
                ...watchData,
                { name: stock.stockName, code: stock.stockCode },
              ]);
              // console.log(watchData);
            }}
          >
            <SearchView
              name={stock.stockName}
              code={stock.stockCode}
              key={stock.stockCode}
            />
          </div>
        );
      })}

      {/* 최근 조회 목록 */}
      <div className="search-list">{HistoryView()}</div>
    </div>
  );
};

export default SearchBar;

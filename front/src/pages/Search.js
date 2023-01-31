import '../App.css';
import '../pages/css/search.css';
import { TextField } from '@mui/material';
import { useState } from 'react';
import { AiOutlineSearch } from 'react-icons/ai'; // 돋보기
import SearchList from '../components/search/SearchList';
import axios from 'axios';

const Search = () => {
  const data = [
    { name: '키움증권', code: '000000' },
    { name: '삼성전자', code: '000002' },
    { name: 'LG전자', code: '000003' },
    { name: '스튜디오드래곤', code: '000004' },
    { name: '영호화학', code: '000005' },
    { name: '씨젠', code: '000006' },
    { name: 'LG화학', code: '000007' },
    { name: 'DL', code: '000008' },
    { name: '오뚜기', code: '000009' },
    { name: '삼성카드', code: '000010' },
  ];
  // 검색어
  const [keyword, setKeyword] = useState();

  // 결과
  const [results, setResult] = useState([]);

  // 업데이트
  const updateField = (field, value, update = true) => {
    if (update) onSearch(value);
    if (field === 'keyword') {
      setKeyword(value);
    }
    if (field === 'results') {
      setResult(value);
    }
  };

  // 입력된 텍스트로 data에서 찾아 매칭되는 결과들을 저장
  const onSearch = (text) => {
    const results = data.filter((item) => true === matchName(item.name, text));
    setResult({ results });
  };

  // 검색해야할 문자열을 키워드와 비교하여 매칭이 되는지 체크
  const matchName = (name, keyword) => {
    const keyLen = keyword.length;
    name = name.toLowerCase().substring(0, keyLen);
    if (keyword === '') return false;
    return name === keyword.toString().toLowerCase();
  };

  return (
    <div className="App">
      <SearchList
        keyword={keyword}
        results={results}
        updateField={updateField}
      ></SearchList>
    </div>
  );
};
export default Search;

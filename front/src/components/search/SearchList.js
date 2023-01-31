import { TextField } from '@mui/material';
import { AiOutlineSearch } from 'react-icons/ai';

// 키워드, 결과값들, 업데이트필드를 전달받는다
const SearchList = ({ keyword, results, updateField }) => {
  //
  const updateText = (text) => {
    //console.log('update text', text);
    updateField('keyword', text, false);
    updateField('results', []);
  };

  let renderResults;
  const arr = results['results'];
  if (arr) {
    // arr 에 검색어에 대한 결과가 담기면, SearchView 호출
    renderResults = arr.map((item) => {
      return (
        <SearchView
          updateText={updateText}
          name={item.name}
          code={item.code}
          key={item.code}
        />
      );
    });
  }
  // onChange를 사용하여 글자를 입력할때마다 updateField호출하고 renderResults를 그린다.
  return (
    <div className="auto">
      <AiOutlineSearch size={40} />
      <TextField
        sx={{ width: { xs: 288, sm: 400, md: 500, lg: 700, xl: 1300 } }}
        id="search-bar"
        placeholder="종목명"
        variant="standard"
        value={keyword}
        onChange={(e) => updateField('keyword', e.target.value)}
      />
      <div className="search-results">{renderResults}</div>
    </div>
  );
};

// 검색된 아이템 "name" "code" 출력

const SearchView = ({ name, code }) => {
  //console.log('search view:', name);

  return (
    <div className="stock-list">
      <p className="name">{name}</p>
      <p className="code">{code}</p>
    </div>
  );
};
export default SearchList;

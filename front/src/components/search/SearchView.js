import './css/SearchView.css';

// 검색된 아이템 "name" "code" 출력
const SearchView = ({ name, code }) => {
  //console.log('search view:', name);

  return (
    <div className="search-list">
      <div className="stock-list">
        <span id="name">{name}</span>
        <span id="code">{code}</span>
      </div>
    </div>
  );
};

export default SearchView;

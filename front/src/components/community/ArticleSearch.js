import { TextField } from '@mui/material';
import { useState } from 'react';
import ArticleItem from './ArticleItem';

const FilterArticle = (props) => {
  const articles = props.articles;

  const [search, setSearch] = useState('');
  const [state, setState] = useState(false);

  let word;
  const test = () =>
    articles.map((data, i) => {
      //   console.log(data.articleTitle.split(' '));

      word = data.articleTitle.split(' ');
      if (word.includes(search)) {
        return (
          <div key={i}>
            <ArticleItem article={data} />
          </div>
        );
      }
    });

  return (
    <div>
      <TextField
        sx={{ width: { xs: 300, sm: 540, md: 720, lg: 960, xl: 1140 } }}
        id="search-bar"
        placeholder="검색어를 입력하세요"
        variant="standard"
        onChange={(e) => {
          setSearch(e.target.value);
          test();
        }}
      />
      {test()}
    </div>
  );
};

export default FilterArticle;

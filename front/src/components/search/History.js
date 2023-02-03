import './css/History.css';

const History = (props) => {
  const watchData = props.watchData;

  return (
    <div className="watch-list">
      <div className="watch-title">
        <h3> 최근 조회한 종목</h3>
      </div>
      <div className="watch-items">
        {watchData.map((data, i) => {
          return (
            <div className="watch-item" key={i}>
              <div>{data.name}</div>
              <div>{data.code}</div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default History;

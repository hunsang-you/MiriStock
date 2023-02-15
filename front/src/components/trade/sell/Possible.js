const SellPossible = (props) => {
  return (
    <div className="possible-body">
      <div className="possible-content">
        <div>구매가능 주식수</div>
        <div>
          {props.maxCount === undefined ? (
            <div className="text-right">0 주</div>
          ) : (
            <div className="text-right">
              {props.maxCount.toLocaleString()} 주
            </div>
          )}
        </div>
      </div>
      <div className="possible-content">
        <div>수수료</div>
        <div>
          {props.hopeTax === undefined ? (
            <div className="text-right">0 원</div>
          ) : (
            <div className="text-right">
              {props.hopeTax.toLocaleString()} 원
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default SellPossible;

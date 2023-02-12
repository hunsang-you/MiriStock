const Possible = (props) => {
  return (
    <div className="possible-body">
      <div className="possible-content">
        <div>구매가능 주식수</div>
        <div className="text-right">{props.maxCount.toLocaleString()} 주</div>
      </div>
      <div className="possible-content">
        <div>수수료</div>
        <div className="text-right">{props.hopeTax} 원</div>
      </div>
    </div>
  );
};

export default Possible;

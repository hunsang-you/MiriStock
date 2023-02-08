const HopePrice = (props) => {
  return (
    <div className="hope-body">
      <div className="hope-title">1주당 구매 희망가</div>
      <div className="hope-input">
        <div className="hope-money">{props.hopePrice}</div>
        <div>원</div>
      </div>
    </div>
  );
};

export default HopePrice;

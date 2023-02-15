const SellHopeCount = (props) => {
  return (
    <div className={props.hopeInputID === 1 ? 'hope-body' : 'disabled-body'}>
      <div
        className={props.hopeInputID === 1 ? 'hope-title' : 'disabled-title'}
      >
        판매할 주식수
      </div>
      <div
        className="hope-input"
        onTouchStart={() => {
          props.inputID(1);
        }}
      >
        <div className="hope-money">{props.hopeCount.toLocaleString()}</div>
        <div>주</div>
      </div>
    </div>
  );
};

export default SellHopeCount;

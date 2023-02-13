const Test = () => {
  return (
    <div>
      조건A ? ("실행문1") : 조건B ? ("실행문2") : 조건C ? ("실행문3") :
      ("실행문4");
      {props.hopePrice === 0 ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="grey"
          disableElevation
        >
          <div className="trade-nonbtn">구매 가격을 입력해주세요</div>
        </Button>
      ) : props.hopePrice < props.stockPrice * 0.7 ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="grey"
          disableElevation
        >
          <div className="trade-nonbtn">구매 가격이 낮습니다</div>
        </Button>
      ) : props.hopePrice > props.stockPrice * 1.3 ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="grey"
          disableElevation
        >
          <div className="trade-nonbtn">구매 가격이 높습니다</div>
        </Button>
      ) : props.stockPrice * 0.7 <=
        props.hopePrice <=
        props.stockPrice * 1.3 ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="primary"
          disableElevation
          onClick={() => {
            props.inputID(1);
          }}
        >
          <div className="trade-nonbtn">입력 완료</div>
        </Button>
      ) : props.hopeCount == 0 ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="grey"
          disableElevation
        >
          <div className="trade-nonbtn">구매 수량을 입력해주세요</div>
        </Button>
      ) : props.hopeCount > Math.floor(props.maxCount) ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="grey"
          disableElevation
        >
          <div className="trade-nonbtn">구매 수량을 확인하세요</div>
        </Button>
      ) : (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="primary"
          disableElevation
          onClick={() => {
            finalCheck();
            console.log('거래완뇨');
          }}
        >
          <div className="trade-btn">
            <div>{hopeTotal.toLocaleString()}원</div>
          </div>
        </Button>
      )}
    </div>
  );
};

export default Test;

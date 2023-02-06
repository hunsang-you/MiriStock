import './css/Portfolio.css';

const Result1 = () => {
  return (
    <div className="portfolio-asset">
      <div className="portfolio-result1">
        <p id="start-asset">초기자산</p>
        <p>50,000,000원 </p>
        <p id="end-asset">최종자산</p>
        <p> 142,843,350원 </p>
      </div>
      <div className="portfolio-result1">
        <p id="end-profit">수익</p>
        <p>92,843,350원 </p>
        <p id="end-rate">수익률</p>
        <p> 11.42%</p>
      </div>
    </div>
  );
};

export default Result1;

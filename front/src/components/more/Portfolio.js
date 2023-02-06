import Chart from './Chart';
import './css/Portfolio.css';

const Portfolio = () => {
  return (
    <div className="portfolio-page">
      <Chart />
      <div className="portfolio-asset">
        <div className="portfolio-result1">
          <div id="portfolio-stats">
            <span id="start-asset"> 초기 50,000,000원 </span>
            <span id="end-rate">수익률 11.42%</span>
          </div>
          <span id="end-asset"> 최종 142,843,350원 </span>
          <span id="end-profit"> 수익 92,843,350원 </span>
        </div>
      </div>
      <div className="portfolio-result2">
        <div>
          <p> 가장 큰 수익을 낸 종목</p>
          <div className="portfolio-best">
            <span> 삼성전자 </span>
            <span id="port-revenue"> 48,512,200 원</span>
          </div>
        </div>
        <hr id="result-lines" />
        <div>
          <p> 가장 큰 손실을 낸 종목</p>
          <div className="portfolio-worst">
            <span> 카카오뱅크 </span>
            <span id="port-loss"> 4,214,250원 </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Portfolio;

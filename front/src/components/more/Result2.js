import './css/Portfolio.css';

const Result2 = () => {
  return (
    <div className="portfolio-result2">
      <div>
        <p> 종료까지 가장 등락률이 높았던 종목</p>
        <div className="portfolio-best">
          <span> 재윤전자 </span>
          <span id="port-revenue"> 11.111%</span>
        </div>
        <div className="portfolio-best">
          <span> 효관전자 </span>
          <span id="port-revenue"> 11.11%</span>
        </div>
        <div className="portfolio-best">
          <span> 도겸전자 </span>
          <span id="port-revenue"> 11.11%</span>
        </div>
        <div className="portfolio-best">
          <span> 상현전자 </span>
          <span id="port-revenue"> 11.11%</span>
        </div>
        <div className="portfolio-best">
          <span> 헌상전자1111111 </span>
          <span id="port-revenue"> 11.11%</span>
        </div>
      </div>
      <hr id="result-lines" />
      <div>
        <p> 종료까지 가장 등락률이 낮았던 종목</p>
        <div className="portfolio-worst">
          <span> 카카오뱅크 </span>
          <span id="port-loss"> -13.081% </span>
        </div>
        <div className="portfolio-worst">
          <span> 종목2 </span>
          <span id="port-loss"> -11.1111% </span>
        </div>
        <div className="portfolio-worst">
          <span> 종목3333 </span>
          <span id="port-loss"> -11.11% </span>
        </div>
        <div className="portfolio-worst">
          <span> 종목4444444 </span>
          <span id="port-loss"> -11.11% </span>
        </div>
        <div className="portfolio-worst">
          <span> 종목5 </span>
          <span id="port-loss"> -11.11% </span>
        </div>
      </div>
    </div>
  );
};

export default Result2;

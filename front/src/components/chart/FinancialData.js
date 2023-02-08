import { useEffect } from 'react';
import { stockAPI } from '../../api/api'; // api 통신

const FinancialData = (props) => {
  // financialYear={financialYear}
  // setFinancialYear={setFinancialYear}
  // salesRevenue={salesRevenue}
  // setSalesRevenue={setSalesRevenue}
  // operatingProfit={operatingProfit}
  // setOperatingProfit={setOperatingProfit}
  // newIncome={newIncome}
  // setNewIncome={setNewIncome}
  useEffect(() => {
    const getValueData = async (stockCode) => {
      let newYear = [];
      let salesData = [];
      let operatingData = [];
      let incomeData = [];
      await stockAPI
        .financialStatement(stockCode)
        .then((request) => {
          const reqData = request;
          for (let i = 0; i < props.idx; i++) {
            newYear.push(reqData.data[i].year);
            salesData.push(reqData.data[i].salesRevenue);
            operatingData.push(reqData.data[i].operatingProfit);
            incomeData.push(reqData.data[i].newIncome);
          }
          // console.log(newYear);
          props.setFinancialYear(newYear);
          props.setSalesRevenue(salesData);
          props.setOperatingProfit(operatingData);
          props.setNewIncome(incomeData);

          props.setFinancialState({
            series: [
              {
                name: '매출액',
                data: salesData,
              },
              {
                name: '영업이익',
                data: operatingData,
              },
              {
                name: '순이익',
                data: incomeData,
              },
            ],
            options: {
              chart: {
                type: 'bar',
                toolbar: {
                  show: false,
                }, // 햄버거 메뉴 지우는거 사진저장옵션인데 필요x
              },
              colors: ['#F2A3B3', '#94CAEF', '#9FD9D9'],
              plotOptions: {
                bar: {
                  horizontal: false,
                  columnWidth: '55%',
                  endingShape: 'rounded',
                },
              },
              dataLabels: {
                enabled: false,
              },
              stroke: {
                show: true,
                width: 0,
                colors: ['transparent'],
              },
              xaxis: {
                categories: newYear,
                labels: {
                  formatter: function (val) {
                    return val;
                  },
                },
              },
              yaxis: {
                show: false, // y 축 보여줄지 안보여줄지
                title: {
                  text: '$ (thousands)',
                },
              },
              fill: {
                opacity: 1,
              },
              tooltip: {
                y: {
                  formatter: function (val) {
                    let money;
                    if (val >= 100000000) {
                      money = parseInt(val / 100000000).toLocaleString();
                      return money + ' 억원';
                    } else if (val >= 10000) {
                      money = parseInt(val / 10000).toLocaleString();
                      return money + ' 만원';
                    } else {
                      return val.toLocaleString() + ' 원';
                    }
                  },
                },
              },
            },
          });
        })
        .catch((err) => console.log(err));
      // console.log(reqData.data);
      // console.log(idx);
    };
    getValueData('005930');
  }, [props.idx]);
  return <div></div>;
};

export default FinancialData;

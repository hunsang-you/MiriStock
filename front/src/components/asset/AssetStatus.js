import './css/assetstatus.css';
import ReactApexChart from 'react-apexcharts';

const AssetStatus = () => {
  const money = 555555;
  const money2 = 333333;
  const sumss = money + money2;
  const donutData = {
    series: [555553, 333333],
    options: {
      chart: {
        type: 'donut',
      },
      legend: {
        position: 'top',
        show: false,
      },
      responsive: [
        {
          breakpoint: 480,
          options: {},
        },
      ],
      grid: {
        padding: {
          bottom: -100,
        },
      },
      colors: ['#ccccff', '#ffccff'],
      labels: ['보유주식', '보유현금'],
      plotOptions: {
        pie: {
          startAngle: -90,
          endAngle: 90,
          offsetY: 10,
          donut: {
            labels: {
              show: true,
              name: {
                show: true,
                fontSize: '22px',
                fontWeight: 600,
                offsetY: -40,
              },
              total: {
                showAlways: true,
                show: true,
                label: '총 자산',
                fontSize: '16px',
                formatter: function (val) {
                  let asset = sumss.toLocaleString() + '원';
                  return asset;
                },
              },
              value: {
                offsetY: -30,
                fontSize: '16px',
                show: true,
              },
            },
          },
        },
      },
    },
  };

  return (
    <div className="asset-current">
      <div className="asset-nickname">유저닉네임/의 자산입니다</div>
      <div className="asset-chart">
        <ReactApexChart
          type="donut"
          series={donutData.series}
          options={donutData.options}
        />
      </div>
      <hr id="lines" />
    </div>
  );
};

export default AssetStatus;

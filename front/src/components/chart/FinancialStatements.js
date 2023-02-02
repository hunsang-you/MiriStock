import { useState, useEffect } from 'react';
import ApexCharts from 'apexcharts';
import ReactApexChart from 'react-apexcharts';

const FinancialStatements = () => {
  const state = {
    series: [
      {
        name: 'Net Profit',
        data: [44, 55, 57, 56, 61, 46],
      },
      {
        name: 'Revenue',
        data: [76, 85, 101, 98, 87, 81],
      },
      {
        name: 'Free Cash Flow',
        data: [35, 41, 36, 26, 45, 62],
      },
    ],
    options: {
      chart: {
        type: 'bar',
        height: 350,
      },
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
        categories: [2017, 2018, 2019, 2020, 2021, 2022],
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
            return '$ ' + val + ' thousands';
          },
        },
      },
    },
  };

  return (
    <div id="chart">
      <ReactApexChart
        options={state.options}
        series={state.series}
        type="bar"
        height={350}
      />
    </div>
  );
};

export default FinancialStatements;

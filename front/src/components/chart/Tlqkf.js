import { useState, useEffect } from 'react';
import ApexCharts from 'apexcharts';
import ReactApexChart from 'react-apexcharts';

const TestSample = () => {
  function generateDayWiseTimeSeries(baseval, count, yrange) {
    var i = 0;
    var series = [];
    while (i < count) {
      var x = baseval;
      var y =
        Math.floor(Math.random() * (yrange.max - yrange.min + 1)) + yrange.min;

      series.push([x, y]);
      baseval += 86400000;
      i++;
    }
    return series;
  }

  const state = {
    series: [
      {
        data: generateDayWiseTimeSeries(new Date('11 Feb 2017').getTime(), 20, {
          min: 10,
          max: 60,
        }),
      },
    ],
    options: {
      chart: {
        id: 'fb',
        group: 'social',
        type: 'line',
        height: 160,
      },
      colors: ['#008FFB'],
    },

    seriesLine2: [
      {
        data: generateDayWiseTimeSeries(new Date('11 Feb 2017').getTime(), 20, {
          min: 10,
          max: 30,
        }),
      },
    ],
    optionsLine2: {
      chart: {
        id: 'tw',
        group: 'social',
        type: 'line',
        height: 160,
      },
      colors: ['#546E7A'],
    },

    seriesArea: [
      {
        data: generateDayWiseTimeSeries(new Date('11 Feb 2017').getTime(), 20, {
          min: 10,
          max: 60,
        }),
      },
    ],
    optionsArea: {
      chart: {
        id: 'yt',
        group: 'social',
        type: 'area',
        height: 160,
      },
      colors: ['#00E396'],
    },
  };

  return (
    <div id="wrapper">
      <div id="chart-line">
        <ReactApexChart
          options={state.options}
          series={state.series}
          type="line"
          height={160}
        />
      </div>
      <div id="chart-line2">
        <ReactApexChart
          options={state.optionsLine2}
          series={state.seriesLine2}
          type="line"
          height={160}
        />
      </div>
      <div id="chart-area">
        <ReactApexChart
          options={state.optionsArea}
          series={state.seriesArea}
          type="area"
          height={160}
        />
      </div>
    </div>
  );
};

export default TestSample;

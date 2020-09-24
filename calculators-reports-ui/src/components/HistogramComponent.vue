<template>
  <div>
    <div class="chart-title">{{content.title}}</div>
    <Histogram v-bind:chart-data="chartData" v-bind:options="options" class="histogram-chart"></Histogram>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { Histogram } from '../charts/Histogram'
import { ChartData, ChartOptions } from 'chart.js'
import { HistogramContent, HistogramViewType } from '../types/content/HistogramContent'

@Component({
  components: { Histogram }
})
export default class HistogramComponent extends Vue {
  @Prop({ required: true }) content!: HistogramContent

  options: ChartOptions = {
    maintainAspectRatio: false,
    responsive: true,
    scales: {
      xAxes: [{
        ticks: {
          suggestedMin: 0
        },
        stacked: this.content.viewType === HistogramViewType.Stacked,
        scaleLabel: {
          display: true,
          labelString: this.content.xAxis.title
        }
      }],
      yAxes: [{
        ticks: {
          suggestedMin: 0
        },
        stacked: this.content.viewType === HistogramViewType.Stacked,
        scaleLabel: {
          display: true,
          labelString: this.content.yAxis.title
        }
      }]
    },
    legend: {
      display: this.content.viewType === HistogramViewType.Stacked
    }
  }

  get chartData () {
    return this.content.getChartData()
  }
}
</script>

<style scoped lang="scss">
.histogram-chart {
  height: 300px;
}
</style>

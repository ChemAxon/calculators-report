<template>
  <div>
    <div class="chart-title">{{content.title}}</div>
    <Scatterplot v-bind:chart-data="chartData" v-bind:options="options" class="scatterplot-chart"></Scatterplot>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { Scatterplot } from '../charts/Scatterplot'
import { ChartData, ChartOptions } from 'chart.js'
import { ScatterplotContent } from '../types/content/ScatterplotContent'
import tinycolor from 'tinycolor2'

@Component({
  components: { Scatterplot }
})
export default class ScatterplotComponent extends Vue {
  @Prop({ required: true }) content!: ScatterplotContent

  get options () {
    return this.content.getOptions()
  }

  addDiagonalRangeToDataset (dataset: Chart.ChartDataSets[], range: number, label: string, color: string) {
    let lowestPoint = this.content.xAxis.min
    let highestPoint = this.content.xAxis.max

    if (lowestPoint !== undefined && highestPoint !== undefined) {
      let diagonal = {
        label: label,
        backgroundColor: color,
        borderColor: 'transparent',
        pointRadius: 0,
        showLine: true,
        borderWidth: 1,
        lineTension: 0,
        data: [
          { x: lowestPoint, y: lowestPoint - range },
          { x: highestPoint, y: highestPoint - range },
          { x: highestPoint, y: highestPoint + range },
          { x: lowestPoint, y: lowestPoint + range }
        ]
      }
      dataset.push(diagonal)
    }
  }

  get chartData () {
    let chartDataDataset = this.content.getChartData()

    if (chartDataDataset !== undefined && chartDataDataset.datasets !== undefined) {
      this.addDiagonalRangeToDataset(chartDataDataset.datasets, 0.01, 'Diagonal', '#254b1c')

      if (this.content.diagonalRanges != null) {
        let ranges = this.content.diagonalRanges.getColumnData()
        ranges.values.filter(value => value != null).forEach((range, index) => {
          let hexColor = tinycolor(ranges.colors[index])
          let rgbColor = hexColor.setAlpha(0.20).toRgbString()
          this.addDiagonalRangeToDataset(chartDataDataset.datasets!, range, ranges.labels[index], rgbColor)
        })
      }
    }

    return chartDataDataset
  }
}
</script>

<style scoped lang="scss">
.scatterplot-chart {
  height: 8000px;
  height: 800px;
}
</style>

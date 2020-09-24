<template>
  <div>
    <PieChart v-bind:chart-data="chartData" v-bind:options="options" class="pie-chart"></PieChart>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { PieChart } from '../charts/PieChart'
import { ChartData, ChartOptions } from 'chart.js'
import { PieChartContent } from '../types/content/PieChartContent'

@Component({
  components: { PieChart }
})
export default class PieChartComponent extends Vue {
  @Prop({ required: true }) content!: PieChartContent

  options: ChartOptions = {
    maintainAspectRatio: false,
    responsive: true,
    legend: {
      position: 'right'
    }
  }

  get chartData () {
    return this.content.getChartData()
  }
}
</script>

<style scoped lang="scss">
.pie-chart {
  height: 200px;
  width: 400px;
}
</style>

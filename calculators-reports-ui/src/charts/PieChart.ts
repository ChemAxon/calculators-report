import { Component, Prop, Vue } from 'vue-property-decorator'
import { Pie } from 'vue-chartjs'
import { ChartData, ChartOptions } from 'chart.js'

@Component({
  extends: Pie
})
export class PieChart extends Vue {
  @Prop({ required: true }) chartData!: ChartData
  @Prop({ required: true }) options!: ChartOptions
  renderChart: any

  mounted () {
    this.renderChart(this.chartData, this.options)
  }
}

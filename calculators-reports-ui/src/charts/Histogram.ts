import { Component, Prop, Vue } from 'vue-property-decorator'
import { Bar } from 'vue-chartjs'
import { ChartData, ChartOptions } from 'chart.js'

@Component({
  extends: Bar
})
export class Histogram extends Vue {
  @Prop({ required: true }) chartData!: ChartData
  @Prop({ required: true }) options!: ChartOptions
  renderChart: any

  mounted () {
    this.renderChart(this.chartData, this.options)
  }
}

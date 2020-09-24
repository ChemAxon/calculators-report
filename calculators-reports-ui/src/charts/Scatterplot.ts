import { Component, Prop, Vue } from 'vue-property-decorator'
import { Scatter } from 'vue-chartjs'
import { ChartData, ChartOptions } from 'chart.js'

@Component({
  extends: Scatter
})
export class Scatterplot extends Vue {
  @Prop({ required: true }) chartData!: ChartData
  @Prop({ required: true }) options!: ChartOptions
  renderChart: any

  mounted () {
    this.renderChart(this.chartData, this.options)
  }
}

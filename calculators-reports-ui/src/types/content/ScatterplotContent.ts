import { Content, ContentInterface } from './Content'
import { DataSource } from '../DataSource'
import { ChartOptions, ChartData } from 'chart.js'
import { PieChartContent } from './PieChartContent'

export interface Axis {
  title: string;
  min?: number;
  max?: number;
}

export interface ScatterplotDataset {
  tableId: string;
  xColumnId: string;
  yColumnId: string;
  color: string;
}

export interface ScatterplotInterface extends ContentInterface {
  title: string;
  xAxis: Axis;
  yAxis: Axis;
  dataSets: ScatterplotDataset[];
  diagonalRanges: PieChartContent | null;
}

export class Point {
  x: number;
  y: number;

  constructor (x:number, y:number) {
    this.x = x
    this.y = y
  }
}

export class ScatterplotContent extends Content implements ScatterplotInterface {
  title: string = '';
  xAxis: Axis;
  yAxis: Axis;
  dataSets: ScatterplotDataset[] = [];
  datasource: DataSource;
  diagonalRanges: PieChartContent | null = null;

  constructor (content: ScatterplotInterface, datasource: DataSource) {
    super(content.type)
    this.dataSets = content.dataSets
    this.xAxis = content.xAxis
    this.yAxis = content.yAxis
    this.title = content.title
    this.datasource = datasource
    if (content.diagonalRanges != null) {
      this.diagonalRanges = new PieChartContent(content.diagonalRanges, datasource)
    }
  }

  getChartData (): ChartData {
    return {
      datasets: this.dataSets.map(dataSet => this.getDataSets(dataSet))
    }
  }

  getDataSets (dataSet: ScatterplotDataset) {
    let sourceTable = this.datasource.getTable(dataSet.tableId)
    let points: Point[] = []

    let xColumn = sourceTable.getColumn(dataSet.xColumnId)
    let yColumn = sourceTable.getColumn(dataSet.yColumnId)

    xColumn.rows.forEach((xCoordinate, rowIndex: number) => {
      if (yColumn!.rows[rowIndex] !== undefined) {
        points.push(new Point(xColumn!.rows[rowIndex], yColumn!.rows[rowIndex]))
      }
    })

    return {
      label: this.title,
      data: points,
      backgroundColor: dataSet.color,
      // borderColor: 'white',
      borderWidth: 1
    }
  }

  getOptions () {
    let xAxisTickConfiguration = this.xAxis.min === undefined ? undefined : {
      min: this.xAxis.min,
      max: this.xAxis.max
    }

    let yAxisTickConfiguration = this.yAxis.min === undefined ? undefined : {
      min: this.yAxis.min,
      max: this.yAxis.max
    }

    return {
      scales: {
        xAxes: [{
          type: 'linear',
          position: 'bottom',
          scaleLabel: {
            display: 'auto',
            labelString: this.xAxis.title
          },
          ticks: xAxisTickConfiguration
        }],
        yAxes: [{
          scaleLabel: {
            display: 'auto',
            labelString: this.yAxis.title
          },
          ticks: yAxisTickConfiguration
        }]
      },
      maintainAspectRatio: false,
      responsive: true
    }
  }
}

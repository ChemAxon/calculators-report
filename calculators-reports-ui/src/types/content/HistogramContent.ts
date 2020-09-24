import { Content, ContentInterface } from './Content'
import { DataSource } from '../DataSource'
import { Axis } from './ScatterplotContent'

export enum HistogramViewType {
  Normal = 'NORMAL',
  Stacked = 'STACKED'
}

export interface HistogramDataset {
  yColumnId: string;
  color: string;
}

export interface HistogramInterface extends ContentInterface {
  title: string;
  xAxis: Axis;
  yAxis: Axis;
  viewType: HistogramViewType;
  dataSets: HistogramDataset[];
  tableId: string;
  xColumnId: string;
}

export class HistogramContent extends Content implements HistogramInterface {
  title: string = '';
  xAxis: Axis;
  yAxis: Axis;
  viewType: HistogramViewType;
  dataSets: HistogramDataset[] = [];
  tableId: string;
  xColumnId: string;
  datasource: DataSource;

  constructor (content: HistogramInterface, datasource: DataSource) {
    super(content.type)
    this.dataSets = content.dataSets
    this.xAxis = content.xAxis
    this.yAxis = content.yAxis
    this.title = content.title
    this.viewType = content.viewType
    this.tableId = content.tableId
    this.xColumnId = content.xColumnId
    this.datasource = datasource
  }

  getChartData () {
    let sourceTable = this.datasource.getTable(this.tableId)
    let labelColumn = sourceTable.getColumn(this.xColumnId)
    return {
      labels: labelColumn.rows,
      datasets: this.getDataSets()
    }
  }

  getDataSets () {
    let sourceTable = this.datasource.getTable(this.tableId)

    return this.dataSets.map(dataSet => {
      let dataColumn = sourceTable!.getColumn(dataSet.yColumnId)
      return {
        label: dataColumn.displayName,
        backgroundColor: dataSet.color,
        data: dataColumn.rows
      }
    })
  }
}

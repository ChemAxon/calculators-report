import { Content, ContentInterface } from './Content'
import { DataSource } from '../DataSource'

export interface PieChartInterface extends ContentInterface {
  labelColumnId: string;
  valueColumnId: string;
  colorColumnId: string;
  tableId: string;
}

export class PieChartContent extends Content implements PieChartInterface {
  tableId: string;
  labelColumnId: string;
  valueColumnId: string;
  colorColumnId: string;
  datasource: DataSource;

  constructor (content: PieChartInterface, datasource: DataSource) {
    super(content.type)
    this.tableId = content.tableId
    this.labelColumnId = content.labelColumnId
    this.valueColumnId = content.valueColumnId
    this.colorColumnId = content.colorColumnId
    this.datasource = datasource
  }

  getColumnData () {
    let sourceTable = this.datasource.getTable(this.tableId)
    let labelColumn = sourceTable.getColumn(this.labelColumnId)
    let colorColumn = sourceTable.getColumn(this.colorColumnId)
    let valueColumn = sourceTable.getColumn(this.valueColumnId)

    return {
      labels: labelColumn.rows,
      colors: colorColumn.rows,
      values: valueColumn.rows
    }
  }

  getChartData () {
    let sourceTable = this.datasource.getTable(this.tableId)
    let labelColumn = sourceTable.getColumn(this.labelColumnId)
    let valueColumn = sourceTable.getColumn(this.valueColumnId)
    let colorColumn = sourceTable.getColumn(this.colorColumnId)

    return {
      labels: labelColumn.rows,
      datasets: [{
        backgroundColor: colorColumn.rows,
        data: valueColumn.rows
      }]
    }
  }
}

import { Content, ContentInterface } from './Content'
import { DataSource } from '../DataSource'

export interface TableInterface extends ContentInterface {
  tableId: string;
  title: string;
  columnIds: string[];
}

export class TableContent extends Content implements TableInterface {
  tableId: string;
  columnIds: string[];
  title: string;
  datasource: DataSource;

  constructor (content: TableInterface, datasource: DataSource) {
    super(content.type)
    this.datasource = datasource
    this.tableId = content.tableId
    this.columnIds = content.columnIds
    this.title = content.title
  }

  getHeader () {
    let sourceTable = this.datasource.getTable(this.tableId)
    return this.columnIds.map(columnId => ({ displayName: sourceTable.getColumn(columnId).displayName }))
  }

  getRows (): any[] {
    let sourceTable = this.datasource.getTable(this.tableId)
    let columnCount = this.columnIds.length
    let rows: any[] = []

    if (columnCount > 0) {
      let rowCount = sourceTable.getColumn(this.columnIds[0]).rows.length
      for (let rowIndex = 0; rowIndex < rowCount; rowIndex++) {
        rows.push({
          rowId: rowIndex,
          data: this.columnIds.map(columnId => sourceTable.getColumn(columnId).rows[rowIndex])
        })
      }
    }
    return rows
  }
}

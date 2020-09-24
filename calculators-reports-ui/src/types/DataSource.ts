import uniqueId from 'lodash.uniqueid'

export enum ColumnType {
  String = 'STRING',
  Double = 'DOUBLE',
  Empty = 'EMPTY'
}

export interface TableColumn {
  id: string;
  type: ColumnType;
  displayName: string;
  rows: any[];
}

export interface DataTableInterface {
  id: string;
  columns: TableColumn[];
}

export class DataTable {
  id: string;
  columns: TableColumn[];

  constructor (table:DataTableInterface) {
    this.id = table.id
    this.columns = table.columns
  }

  getColumn (columnId: string): TableColumn {
    let column = this.columns.find((c) => c.id === columnId)
    if (column === undefined) {
      return {
        id: uniqueId('empty_table_column_'),
        type: ColumnType.Empty,
        displayName: '- empty column -',
        rows: []
      }
    } else {
      return column
    }
  }
}

export class DataSource {
  tables: DataTable[]

  constructor (tables: DataTableInterface[]) {
    this.tables = tables.map((table:DataTableInterface) => new DataTable(table))
  }

  getTable (tableId: string): DataTable {
    let table = this.tables.find((t) => t.id === tableId)

    if (table === undefined) {
      // Return an empty mock table with no data
      return new DataTable({
        id: uniqueId('empty_table_'),
        columns: []
      })
    } else {
      return table
    }
  }
}

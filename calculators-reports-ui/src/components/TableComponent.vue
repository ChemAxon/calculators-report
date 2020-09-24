<template>
  <div>
    <div class="table-actions">
      <div class="table-title">{{content.title}}</div>
      <button href v-on:click.prevent="downloadAsCsv()"> Download as CSV</button>
      <button href v-on:click.prevent="toggleExpandedState()"> Show/hide data</button>
    </div>

    <div class="table-container">
      <table class="calc-table" v-show="expanded">
        <thead>
          <tr><th v-for="header in tableHeaders" v-bind:key="header.displayName">{{header.displayName}}</th></tr>
        </thead>
        <tbody>
          <tr v-for="row in getRows" v-bind:key="row.rowId">
            <td v-for="data in row.data" :key="data">{{data}}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { TableContent } from '../types/content/TableContent'
import { saveAs } from 'file-saver'

@Component
export default class TableComponent extends Vue {
  @Prop({ required: true }) content!: TableContent
  expanded: boolean = false

  toggleExpandedState () {
    this.expanded = !this.expanded
  }

  downloadAsCsv () {
    let csvForamttedTable = this.getRows.map(row => row.data.join(',')).join('\n')
    let blobContent = new Blob([csvForamttedTable], { type: 'text/csv;charset=utf-8' })
    saveAs(blobContent, 'raw_data.csv')
  }

  get tableHeaders () {
    return this.content.getHeader()
  }

  get getRows () {
    return this.content.getRows()
  }
}
</script>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.calc-table {
  width: 100%;
  border-collapse: collapse;

  th, td {
    padding: $gutter;
    text-align: left;
    vertical-align: top;
    border-bottom: 1px solid #e7e7e7;
    padding-left: 0;
  }
  td {
    word-break: break-all;
  }
}
.table-container {
  width: 100%;
  overflow: auto;
  max-height: 800px;
}
.table-actions {
  padding: $gutter/2 0;
  margin-bottom: $gutter;
  border-bottom: 1px solid #CCC;
  display: flex;
  justify-content: flex-end;
  .table-title {
    font-weight: bold;
    margin-right: auto;
    font-size: 20px;
  }
  button {
    margin-left: $gutter;
  }
}
</style>

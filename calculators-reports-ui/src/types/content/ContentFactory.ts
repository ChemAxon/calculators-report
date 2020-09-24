import { Content, ContentType } from './Content'
import { MarkdownContent } from './MarkdownContent'
import { ScatterplotContent } from './ScatterplotContent'
import { EmptyContent } from './EmptyContent'
import { DataSource } from '../DataSource'
import { PieChartContent } from './PieChartContent'
import { HistogramContent } from './HistogramContent'
import { TableContent } from './TableContent'

export class ContentFactory {
  datasource: DataSource;

  constructor (datasource: DataSource) {
    this.datasource = datasource
  }

  createContent (content: Content) {
    switch (content.type) {
      case ContentType.MarkDown:
        return new MarkdownContent(content as MarkdownContent)

      case ContentType.Scatterplot:
        return new ScatterplotContent(content as ScatterplotContent, this.datasource)

      case ContentType.PieChart:
        return new PieChartContent(content as PieChartContent, this.datasource)

      case ContentType.Histogram:
        return new HistogramContent(content as HistogramContent, this.datasource)

      case ContentType.Table:
        return new TableContent(content as TableContent, this.datasource)

      default:
        return new EmptyContent(content)
    }
  }
}

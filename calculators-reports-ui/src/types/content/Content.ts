import uniqueId from 'lodash.uniqueid'

export enum ContentType {
  MarkDown = 'MDTEXT',
  Scatterplot = 'SCATTERPLOT',
  PieChart = 'PIE',
  Histogram = 'HISTOGRAM',
  Table = 'TABLE'
}

export interface ContentInterface {
  type: ContentType;
}

export abstract class Content implements ContentInterface {
  type: ContentType;
  id: string;

  constructor (type:ContentType) {
    this.type = type
    // Generate a unique id for each Content that can be used later e.g. in v-for
    this.id = uniqueId('content_')
  }
}

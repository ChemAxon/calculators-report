import { Content, ContentInterface } from './Content'
import marked from 'marked'

export interface MarkdownInterface extends ContentInterface {
  mdtext: string;
}

export class MarkdownContent extends Content implements MarkdownInterface {
  mdtext: string = '';

  constructor (mdcontent: MarkdownInterface) {
    super(mdcontent.type)
    this.mdtext = mdcontent.mdtext
  }

  toHtml (): string {
    return marked(this.mdtext)
  }
}

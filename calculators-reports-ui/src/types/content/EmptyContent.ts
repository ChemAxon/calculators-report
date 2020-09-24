import { ContentInterface, Content } from './Content'

export class EmptyContent extends Content {
  constructor (content:ContentInterface) {
    super(content.type)
  }
}

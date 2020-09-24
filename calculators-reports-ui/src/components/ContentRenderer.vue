<template>
  <component v-bind:is="contentComponent" v-bind:content="content"></component>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import MarkDownComponent from './MarkdownComponent.vue'
import ScatterplotComponent from './ScatterplotComponent.vue'
import EmptyComponent from './EmptyComponent.vue'
import { ContentType, Content } from '../types/content/Content'
import PieChartComponent from './PieChartComponent.vue'
import HistogramComponent from './HistogramComponent.vue'
import TableComponent from './TableComponent.vue'

@Component
export default class ContentRenderer extends Vue {
  @Prop({ required: true }) content!: Content

  get contentComponent () {
    switch (this.content.type) {
      case ContentType.Scatterplot: return ScatterplotComponent
      case ContentType.MarkDown: return MarkDownComponent
      case ContentType.PieChart: return PieChartComponent
      case ContentType.Histogram: return HistogramComponent
      case ContentType.Table: return TableComponent
      default: return EmptyComponent
    }
  }
}
</script>

<style lang="scss">
</style>

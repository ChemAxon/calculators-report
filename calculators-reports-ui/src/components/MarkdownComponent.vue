<template>
  <div v-html="markDownToHtml"></div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { MarkdownContent } from '../types/content/MarkdownContent'

@Component
export default class MarkdownComponent extends Vue {
  @Prop({ required: true }) content!: MarkdownContent

  get markDownToHtml () {
    return this.content.toHtml()
  }
}
</script>

<style lang="scss">
</style>

<template>
  <div id="app">
    <header class="app-header">
      <div class="inner-wrapper">
        <img svg-inline alt="Calculator reports" src="./assets/calc_icon.svg" height="16"> Prediction accuracy report
      </div>
    </header>
    <section class="inner-wrapper">
      <div v-for="content in contents" v-bind:key="content.id" class="content">
        <ContentRenderer v-bind:content="content"></ContentRenderer>
      </div>
    </section>
    <footer class="app-footer">
      <div class="inner-wrapper">
        <img svg-inline alt="ChemAxon" src="./assets/chemaxon-logo.svg" height="40">
        <p>Copyright Ⓒ 1998–2020 ChemAxon Ltd. All rights reserved. <strong> Contact Us</strong>: <a href="mailto:calculations-support@chemaxon.com">calculations-support@chemaxon.com</a></p>
        <p>
          // <a href="https://chemaxon.com/products/calculators-and-predictors" target="_blank">All calculators and predictors</a>
          // <a href="https://disco.chemaxon.com/apps/demos/" target="_blank">Live Demo</a>
          // <a href="https://docs.chemaxon.com/display/docs/Calculator_Plugins_User's_Guide.html" target="_blank">Calculator Plugins User's Guide</a>
          // <a href="https://www.chemicalize.com" target="_blank">Chemicalize</a>
        </p>
      </div>
    </footer>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import ContentRenderer from './components/ContentRenderer.vue'
import exampleJsonShort from './assets/json/output_short.json'
import { DataSource } from './types/DataSource'
import { Content, ContentInterface } from './types/content/Content'
import { ContentFactory } from './types/content/ContentFactory'

@Component({
  components: { ContentRenderer }
})
export default class App extends Vue {
  rawJson:any;
  dataSource: any;
  // TODO: the one below would be prefferable
  // dataSource: DataSource;
  contents: Content[] = [];

  created () {
    this.initiateRawJson()
    this.initiateDataSource()
    this.initiateContents()
  }

  initiateRawJson () {
    this.rawJson = window.jsonReport ? window.jsonReport : exampleJsonShort
  }

  initiateDataSource () {
    this.dataSource = new DataSource(this.rawJson.tables)
  }

  initiateContents () {
    let contentCreator = new ContentFactory(this.dataSource)
    this.contents = this.rawJson.contents.map((content:ContentInterface) => {
      return contentCreator.createContent(content as Content)
    })
  }
}
</script>

<style lang="scss">
@import '~normalize.css';
@import '@/styles/main.scss';
</style>

var HtmlWebpackPlugin = require('html-webpack-plugin');
var HtmlWebpackInlineSourcePlugin = require('html-webpack-inline-source-plugin');

module.exports = {
  css: {
    extract: false
  },
  configureWebpack: {
    plugins: [
      new HtmlWebpackPlugin({
        template: 'public/index.html',
	      filename: 'index.html',
        inlineSource: '.(js|css)$', // embed all javascript and css inline
        jsonReportToken: process.env.NODE_ENV === 'production' ? 'var jsonReport = ${REPORT_JSON};' : ''
      }),
      new HtmlWebpackInlineSourcePlugin()
    ]
  },
  chainWebpack: config => {
		config.module
			.rule("vue")
			.use("vue-svg-inline-loader")
				.loader("vue-svg-inline-loader")
	}
}
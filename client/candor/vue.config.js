const path = require("path");

module.exports = {
  outputDir: path.resolve(__dirname, "../../resources/public/"),
  css: {
    modules: true,
    loaderOptions: {
      scss: {
        data: `@import "~@/variables.scss";`
      },
    }
  }
}
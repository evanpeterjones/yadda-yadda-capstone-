const path = require("path");

module.exports = {
  outputDir: path.resolve(__dirname, "../../resources/public/"),
  devServer: {
    proxy: 'http://localhost:5000/',
  }
}
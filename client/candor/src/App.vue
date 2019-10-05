<template>
<div id="app">
  <NavigationBar />
  <div v-if="!$location">
    <Splash />
  </div>
</div>
</template>

<script>
import NavigationBar from './components/NavigationBar.vue'
import Splash from './components/Splash.vue'

export default {
  title: 'YAPP',
  name: 'App',
  components: { 
    NavigationBar, Splash
  }
}
</script>

<style>
html {
  background-color: #222222;
}

:root {
  --primary: pink;
  --secondary: yellow;
  --tertiary: magenta;
}

#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
  background-color: #222222;
}
</style>

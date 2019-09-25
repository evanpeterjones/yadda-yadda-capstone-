<template>
  <div id="app">
    <NavigationBar />
    <Splash />
  </div>
</template>

<script>
//import HelloWorld from './components/HelloWorld.vue'
import NavigationBar from './components/NavigationBar.vue'
import Splash from './components/Splash.vue'

export default {
  name: 'app',
  components: { 
    NavigationBar, Splash
  },
  created() {
    
  }
}
</script>

<style>
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
}
</style>

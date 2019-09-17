<template>
  <div> 
    <navigation-bar></navigation-bar>
    <div id="app">
      <Login />
    </div>
  </div>
</template>

<script>
//import HelloWorld from './components/HelloWorld.vue'
import NavigationBar from './components/NavigationBar.vue'
import Login from './components/Login.vue'

export default {
  name: 'app',
  components: { 
    NavigationBar, Login
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

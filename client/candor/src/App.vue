<template>
  <div id="app">
    <NavigationBar />
    <h1> {{ location }} </h1>
    <div v-if="!location">
      <Splash />
    </div>
    <div v-if="location">
      <!-- Feed /-->      
    </div>
  </div>
</template>

<script>
import NavigationBar from './components/NavigationBar.vue'
import Splash from './Pages/Splash.vue'
//import Feed from './components/Feed.vue'

export default {
  title: 'YAPP',
  name: 'App',
  components: { 
    NavigationBar, Splash
  }, 
  computed: {
    location: function() {
      return this.$store.getters.location
    }
  },
  watch: {
    'location': function() {

			axios.get("/db").then(response => {
				console.log(response)
			}).catch(error => {
				console.log(error)
			});
			
    }
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

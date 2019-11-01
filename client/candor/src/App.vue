<template>
  <div id="app">
    <NavigationBar />
    <div v-if="!location">
      <Splash />
    </div>
    <div v-else>
      <Feed />
    </div>
  </div>
</template>

<script>
import NavigationBar from './components/NavigationBar.vue'
import Splash from './Pages/Splash.vue'
import Feed from './Pages/Feed.vue'
import BModal from 'bootstrap'

export default {
  title: 'YAPP',
  name: 'App',
  components: { 
    NavigationBar, Splash, Feed, BModal
  },
  computed: {
    location: function() {
      return this.$store.getters.location
    }, 
    posts: function() {
      return this.$store.getters.posts;
    }, 
    mobile: function() {
      return this.$store.getters.isMobile;
    }
  },
  methods: {
    cookies_required: function() {
      this.$bvModal.msgBoxConfirm("Yapp relies on giving you a cookie to identify you so you don't have to give up your personal information like other social media sites.", {
            title: 'Please Enable Cookies',
            size: 'sm',
            buttonSize: 'sm',
            okVariant: 'danger',
            okTitle: 'Okay',
            hideHeaderClose: false,
            centered: true
          })
    }
  },
  mounted() {
    if (!navigator.cookieEnabled) {
      this.cookies_required();
    }
    
    this.$nextTick(() => {
      window.addEventListener('resize', () => {
        this.$store.commit("setIsMobile", window.innerWidth < 960);
      });
    })
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

<template>
  <div id="app">
    <NavigationBar
      @newQRCode="swapDialog('QRCode')"/>
      
    <div v-if="!location">
      <Splash />
    </div>
    <div v-else>
      <Home
        @newPostDialog="swapDialog('newPost')"/>
    </div>

    <component 
      :is="CurrDialog"
      @closeDialog="swapDialog(null)"></component>
  </div>
</template>

<script>
import NavigationBar from './components/NavigationBar.vue'
import NewPost from './components/NewPost.vue'
import QRCode from './components/QRCode.vue'
import Splash from './Pages/Splash.vue'
import Home from './Pages/Home.vue'
import BModal from 'bootstrap'

export default {
  title: 'YAPP',
  name: 'App',
  components: { 
    NavigationBar, Splash, BModal, NewPost, QRCode, Home
  },
  data () {
    return {
      CurrDialog : null,
      components : {
        'replyPost' : NewPost,
        'newPost' : NewPost,
        'QRCode' : QRCode
      }
    }
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
  events: {
    'newReplyDialog' : function(parent_fk) {
      console.log('caught that shit');
      this.$store.commit('setReplyTo', post_id)
      this.swapDialog('newPost', post_id)
    }
  },
  mounted() {
    if (!navigator.cookieEnabled) {
      this.cookies_required();
    }

    this.$eventBus.$on('newReplyPost', post_id => {
      
    });

    this.$el.addEventListener("newReplyPost", (post_id) => {
      this.$store.commit('setReplyTo', post_id);
      this.swapDialog('newPost', post_id)
    });
    
    this.$nextTick(() => {
      window.addEventListener('resize', () => {
        this.$store.commit("setIsMobile", window.innerWidth);
      });
    })
  },
  methods: {
    swapDialog: function(comp, replyTo) {
      this.$store.commit('setReplyTo', replyTo)
      this.CurrDialog = this.components[comp];
    },
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
.right-corner-container {     
  position:fixed;     
  right:20px;     
  bottom:20px;   
}
.button-class {
  height: 62px;
  width: 62px;
  border-radius: 62px;
  box-shadow: 0 0 5px #222222;
}

</style>

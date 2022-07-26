<template>
  <div id="app">
    <NavigationBar
      @myPosts="swapDialog('myPosts')"
      @myAccount="swapDialog('myAccount')"
      @newQRCode="swapDialog('QRCode')"
    />
    <!--
    <div v-if="!location">
      <Splash 
        @newPostDialog="swapDialog('newPost')"/>
    </div>
    <div v-else>
      <Home
        @newPostDialog="swapDialog('newPost')" 
      />
    </div>
    -->
    <Post
      content="Sorry, this fun social-media site was DDOS'd by someone in Korea, so I had to take it down, maybe one day I will revive it."
      :time=time
      />
    <component 
      :is="CurrDialog"
      @closeDialog="swapDialog(null, null)"
    />
  </div>
</template>

<script>
import NavigationBar from './components/NavigationBar.vue'
import NewPost from './components/NewPost.vue'
import Post from './components/Post.vue'
import myPosts from './components/myPosts.vue'
import myAccount from './components/myAccount.vue'
import QRCode from './components/QRCode.vue'
import Splash from './Pages/Splash.vue'
import Home from './Pages/Home.vue'
import BModal from 'bootstrap'

export default {
  title: 'YAPP',
  name: 'App',
  components: { 
    NavigationBar, Splash, BModal, NewPost, QRCode, Home, myPosts, myAccount, Post
  },
  data () {
    return {
      time : (new Date()).toDateString(),
      CurrDialog : null,
      components : {
        'replyPost' : NewPost,
        'newPost' : NewPost,
        'QRCode' : QRCode, 
        'myPosts' : myPosts, 
        'myAccount' : myAccount
      }, 
      itemData : null
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
    }, 
    replyPost: function() {
      return ""
    }
  },
  mounted() {
    if (!navigator.cookieEnabled) {
      this.cookies_required();
    }

    this.$http.get("/itemData").then((result) => { 
      console.log("itemData: " + result.data);
      this.itemData = result.data
    }).catch(error => {
      console.error(error)
    });

    this.$store.watch((state) => state.replyTo, (old, newVal) => {
      if (this.$store.getters.replyTo) {
        this.swapDialog('newPost', this.$store.getters.replyTo)
      }
    })
    
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

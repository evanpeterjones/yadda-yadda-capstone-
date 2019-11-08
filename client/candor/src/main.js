import Vue from 'vue'
import App from './App.vue'
import Vuex from "vuex"
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import VueCookies from 'vue-cookies'
import Axios from 'axios'
import titleMixins from '@/mixins/titleMixin.js'
import routes from '@/components/routes/index.js'

// setup defaults and globals
Vue.config.productionTip = false
Vue.use(BootstrapVue)
Vue.use(VueCookies)
Vue.use(Vuex)

VueCookies.config('7d')
VueCookies.set('theme','default')
VueCookies.set('hover-time','1s')

const store = new Vuex.Store({
  state: {
    location: '',
    posts: [],
    isMobile: window.innerWidth < 500, 
    userId : null
  },
  getters: {
    location: state => state.location,
    posts: state => state.posts,
    isMobile: state => state.isMobile,
    userId: state => state.userId
  },
  mutations: {
    setLocation(state, loc) {
      state.location = loc;
    },
    setPosts(state, newPosts) {
      state.posts.push(newPosts)
    },
    setIsMobile(state, isMobileCheck) {
      state.isMobile = isMobileCheck < 800
    }, 
    setUserId(state, newUser) {
      state.userId = newUser
    },
    deletePost(state, postID) {
      var index;
      for (var i = 0; i < state.posts[0].length; i++) {
        console.log("type ob: "+typeof post.pst_id_pk)
        console.log("type ret: "+typeof postID)
        if (posts[0][i].pst_id_pk == postID) {
          index = i //state.posts[0][i];
        }
      }
      console.log("index: "+index)
      delete state.posts[0][index]
    },
    newPost(state, newPost) {
      state.posts[0].unshift(newPost)
    }, 
    loadPosts(state) {
      
    }
  }
});

const ax = Axios.create({
  crossDomain: true,
});

Vue.prototype.$http = ax;

store.watch((store) => store.location, (newLocation, oldLocation) => {
  console.log("New Location: "+ newLocation)
  
  Vue.prototype.$http.get("/getUserFromSession").then((result) => { 
    console.log("new userid: " + result.data);
    store.commit("setUserId", result.data);
  });

  Vue.prototype.$http.get("/feed").then((result) => {
    store.commit("setPosts", result.data); 
  });
});

new Vue({
  store,
  render: h => h(App),
}).$mount('#app')

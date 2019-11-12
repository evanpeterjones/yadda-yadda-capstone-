import Vue from 'vue'
import App from './App.vue'
import Vuex from "vuex"
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import VueCookies from 'vue-cookies'
import Axios from 'axios'
import titleMixins from '@/mixins/titleMixin.js'
import routes from '@/components/routes/index.js'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faReply, faTrash, faPlus, faArrowRight, faAtom } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

library.add(faReply, faTrash, faPlus, faArrowRight, faAtom)

Vue.component('font-awesome-icon', FontAwesomeIcon)

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
    locationType: 'loc_alias',
    posts: [],
    offset: 0,
    isMobile: window.innerWidth < 500, 
    userId : null
  },
  getters: {
    location: state => state.location,
    locationName: state => state.location[state.locationType],
    posts: state => state.posts,
    isMobile: state => state.isMobile,
    userId: state => state.userId, 
    locationType: state => state.locationType,
    offset: state => state.offset
  },
  mutations: {
    setLocation(state, loc) {
      delete loc.loc_id_pk
      state.location = loc
    },
    setPosts(state, newPosts) {
      state.posts.push(newPosts)
    },
    addPosts(state, newPosts) {
      for (var i = 0; i < newPosts.length; i++) {
        state.posts[0].push(newPosts[i]);
      }
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
        if (state.posts[0][i].pst_id_pk == postID) {
          index = i
        }
      }
      
      //state.offset--;
      delete state.posts[0][index]
    },
    newPost(state, newPost) {
      state.posts[0].unshift(newPost)
      state.offset++;
    },
    nextLocationType(state) {
      let keys = Object.keys(state.location);
      let ind = keys.indexOf(state.locationType);
      state.locationType = keys[(ind+1)%3];
    },
    updateOffset(state) {
      state.offset += 5;
    }
  }
});

const ax = Axios.create({
  crossDomain: true,
});

Vue.prototype.$http = ax;

store.watch((store) => store.location, (newLocation, oldLocation) => {
  console.log(newLocation)
  
  Vue.prototype.$http.get("/getUserFromSession").then((result) => { 
    console.log("UserID: " + result.data);
    store.commit("setUserId", result.data);
  });

  Vue.prototype.$http.get("/feed", {
    params: {
      offset: 0
    }
  }).then((result) => {
    store.commit("setPosts", result.data); 
  });
});

new Vue({
  store,
  render: h => h(App),
}).$mount('#app')

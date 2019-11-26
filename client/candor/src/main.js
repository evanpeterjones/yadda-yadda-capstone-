import Vue from 'vue'
import App from './App.vue'
import Vuex from "vuex"
import VueRouter from 'vue-router';
import VueCookies from 'vue-cookies'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import Axios from 'axios'
//import titleMixins from '@/mixins/titleMixin.js'
import routes from '@/components/routes/index.js'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faReply, faTrash, faPlus, faArrowRight, faAtom, faComment } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { FontAwesomeLayers } from '@fortawesome/vue-fontawesome'



library.add(faReply, faTrash, faPlus, faArrowRight, faAtom, faComment)

Vue.component('font-awesome-icon', FontAwesomeIcon)
Vue.component('font-awesome-layers', FontAwesomeLayers)

// setup defaults and globals
Vue.config.productionTip = false
Vue.use(BootstrapVue)
Vue.use(VueCookies)
Vue.use(VueRouter)
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
    userId : null,
    replyTo : null
  },
  getters: {
    location: state => state.location,
    locationName: state => state.location[state.locationType],
    posts: state => state.posts,
    isMobile: state => state.isMobile,
    userId: state => state.userId, 
    locationType: state => state.locationType,
    offset: state => state.offset, 
    replyTo: state => state.replyTo
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
        state.posts[0].splice(state.posts[0].length, 0, newPosts[i])
      }
    },
    addNewCommentsFeed(state, feed) {
      if (state.posts[1]){
        // this is so obnoxious I hate Vue
        for (var i = 0; i < Math.max(feed.length, state.posts[1].length); i++) {
          if (feed[i] && state.posts[1][i]){ 
            state.posts[1].splice(i, 1, feed[i])
          } else if(feed[i]) {
            state.posts[1].splice(i, 0, feed[i])
          } else if (state.posts[1][i]) {
            state.posts[1].splice(state.posts[1].length-1, 1)
          }
        }
      } else {
        state.posts.splice(1,0, feed)
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

      state.posts[0].splice(index, 1)
      state.offset-=1
    },
    setReplyTo(state, reply) {
      state.replyTo = reply;
    },
    newPost(state, newPost) {
      if (newPost.pst_parent_fk) {
        for (var i = 0; i < state.posts[0].length; i++) {
          if (newPost.pst_parent_fk == state.posts[0][i].pst_id_pk) {
            state.posts[0][i] = Object.assign({}, state.posts[0][i], {'pst_hascomments' : true})
          }
        }
      }
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
    },
  }
});

const ax = Axios.create({
  crossDomain: true,
});

Vue.prototype.$http = ax;

const EventBus = new Vue();
Object.defineProperties(Vue.prototype, {
  $bus: {
    get() {
        return EventBus;
    },
  },
});

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

const router = new VueRouter({ routes })

new Vue({
  store,
  router,
  render: h => h(App),
}).$mount('#app')

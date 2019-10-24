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
    posts: []
  },
  getters: {
    location: state => state.location,
    posts: state => state.posts
  },
  mutations: {
    setLocation(state, loc) {
      state.location = loc;
    },
    setPosts(state, newPosts) {
      state.posts.push(newPosts)
    }
  }
});

const ax = Axios.create({
  crossDomain: true,
  //baseURL: process.env.NODE_ENV === "development" ? "localhost:5000" : "https://www.internetizens.com"
});
Vue.prototype.$http = ax;

store.watch((store) => store.location, (newLocation, oldLocation) => {
  console.log("New Location: "+ newLocation)
  // TODO: change this query ?
  Vue.prototype.$http.get("/db").then((result) => { store.commit("setPosts", result); })
});

new Vue({
  store,
  render: h => h(App),
}).$mount('#app')

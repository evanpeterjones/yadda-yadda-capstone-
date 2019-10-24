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
    loggedIn: false,
    location: '',
    posts: null
  },
  getters: {
    location: state => state.location
  },
  mutations: {
    setLocation(state, loc) {
      state.location = loc;
    }
  }
});

Axios.defaults.baseURL = process.env.API_ENDPOINT
Vue.prototype.$http = Axios

store.watch((store) => store.location, (newLocation, oldLocation) => {
  console.log("New Location: "+ newLocation)
  Axios.get("/db").then((result) => { console.log(result) })
});

//store.watch((store) => store.loggedIn, (new))

new Vue({
  store,
  render: h => h(App),
}).$mount('#app')

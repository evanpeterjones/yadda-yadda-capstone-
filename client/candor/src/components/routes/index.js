import Vue from 'vue'
import Router from 'vue-router'
import Splash from '@/Pages/Splash.vue'
import App from '@/App.vue'

Vue.use(Router)

export const routes = new Router({
    mode: 'history',
    routes: [
	{
	    path: '/',
	    name: 'Login',
	    component: Splash,
	    title: 'YAPP'
	},
	{
	    path: '/*',
	    name: 'Yikes',
	    title: 'Yikes',
	    component: App
	},	
    ]
})

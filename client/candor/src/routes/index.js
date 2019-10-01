import Vue from 'vue'
import Router from 'vue-router'
import Splash from '@/components/Splash'
import App from '@/App'

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

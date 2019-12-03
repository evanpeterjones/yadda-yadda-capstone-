import Router from 'vue-router'
import Vue from 'vue'
import Splash from '@/Pages/Splash.vue'
import Feed from '@/Pages/Feed.vue'

Vue.use(Router)

export const routes = new Router({
	routes : [
		{
			path: '/*',
			name: 'Yapp',
			title: 'Yapp',
			component: Feed
		},
		{
			path: '/splash',
			name: 'Login',
			title: 'YAPP',
			component: Splash
		}
	]
});
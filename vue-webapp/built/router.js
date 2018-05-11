import Vue from 'vue';
import VueRouter from 'vue-router';
import login from './components/login.vue';
import home from './components/home.vue';
Vue.use(VueRouter);
var Router = new VueRouter({
    mode: 'history',
    routes: [
        {
            path: '/',
            component: login,
        },
        {
            path: '/home',
            component: home,
        }
    ]
});
export default Router;
//# sourceMappingURL=router.js.map
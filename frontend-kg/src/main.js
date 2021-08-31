import Vue from 'vue';
import App from './App.vue';
import store from './store';
import global from "./utils/global";
import router from './router';
import '@/assets/css/global.scss';
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import VueContextMenu from 'vue-contextmenu';
import Contextmenu from "vue-contextmenujs";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import { Message, MessageBox } from 'element-ui';
import './assets/fonts/iconfont.css'
import VueParticles from 'vue-particles'
Vue.use(VueParticles)

import VueAlertLoading from 'vue-alert-loading';
Vue.use(VueAlertLoading);


// 挂载到$message上
Vue.prototype.$message = Message
Vue.prototype.$confirm=MessageBox.confirm;
Vue.config.productionTip = false
Vue.prototype.$store = store
Vue.prototype.$global = global

Vue.use(VueContextMenu)
Vue.use(Contextmenu)
Vue.use(ElementUI);
Vue.use(Antd);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

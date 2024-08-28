import Vue from "vue";
import App from "./App.vue";
import store from "@/store";
import router from "@/router";
import vuetify from "@/plugins/vuetify";

import VueRouter from "vue-router";
Vue.use(VueRouter);

import VTooltip from "v-tooltip";
Vue.use(VTooltip);

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount("#app");

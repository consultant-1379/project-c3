<template>
  <v-app dark>
    <v-content>
      <Sidebar v-if="currentRouteName != 'login'" />
      <router-view />
    </v-content>
  </v-app>
</template>

<script>
import Sidebar from "@/components/Sidebar";
import { mapActions } from "vuex";
import api from "@/utils/api.js";

export default {
  name: "App",

  components: {
    Sidebar,
  },

  methods: {
    ...mapActions(["setRepos"]),
  },

  computed: {
    currentRouteName() {
      return this.$route.name;
    },
  },

  async created() {
    let data = (await api.getRepos()).data;
    this.setRepos(data);
    console.log(data)
  },

  data: () => ({
    //
  }),
};
</script>

<style lang="scss">
@import "@/scss/custom.scss";

* {
  padding: 0;
  margin: 0;
  font-family: "Poppins";
}

.swal2-styled.swal2-confirm {
  background-color: var(--v-accent-base) !important;
}

html,
body {
  height: 100% !important;
}

v-container,
.container {
  height: 100%;
}

.brand {
  font-family: "Orbitron", "Poppins" !important;
}
</style>

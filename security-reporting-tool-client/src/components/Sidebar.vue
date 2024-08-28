<template>
  <v-navigation-drawer
    app
    fixed
    color="accent"
    dark
    tile
    :width="230"
    v-model="drawer"
    height="100%"
  >
    <v-list>
      <v-subheader class="d-flex">
        <div class="text-title text-center font-weight-bold">
          <v-btn text color="white">
            <v-icon>mdi-shield-outline</v-icon>
            <div class="ml-1 font-weight-bold text-h5 brand">fino</div>
          </v-btn>
        </div>
        <EricssonLogo />
      </v-subheader>

      <v-list-item-group class="mt-5">
        <v-list-item @click="this.$router.push('/home')">
          <v-list-item-icon class="mx-2"> </v-list-item-icon>
          <v-list-item-content>
            Home
          </v-list-item-content>
        </v-list-item>
      </v-list-item-group>
    </v-list>

    <v-footer
      dark
      color="rgba(0,0,0,0)"
      class="justify-center"
      inset
      app
      style="left: 0px !important;"
    >
      <div>2021 — v1.0.0 — <strong class="brand">FINO</strong></div>
    </v-footer>
  </v-navigation-drawer>
</template>

<script>
import EricssonLogo from "@/components/EricssonLogo.vue";
export default {
  components: { EricssonLogo },
  data: () => ({
    drawer: true,
    group: null,
    dialog: false,
    loggedIn: true,
  }),
  computed: {},
  watch: {
    group() {
      this.drawer = true;
    },
  },
};
</script>

<style scoped lang="scss">
v-navigation-drawer,
.v-navigation-drawer {
  --background: #015871 !important;
  --background: linear-gradient(
    90deg,
    rgba(1, 88, 113, 1) 0%,
    rgba(27, 88, 106, 1) 35%,
    rgba(105, 145, 157, 1) 100%
  ) !important;
}
</style>

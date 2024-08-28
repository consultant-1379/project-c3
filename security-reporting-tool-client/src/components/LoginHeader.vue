<template>
  <v-row>
    <v-col>
      <v-btn text color="accent">
        <v-icon>mdi-shield-outline</v-icon>
        <div class="ml-1 font-weight-bold text-h5 brand">fino</div>
      </v-btn>
    </v-col>

    <v-col align="right" align-self="center">
      <router-link to="https://internal.ericsson.com/">
        <div class="ericsson-logo">
          <div class="line line1"></div>
          <div class="line line2"></div>
          <div class="line line2"></div>
        </div>
      </router-link>
    </v-col>
  </v-row>
</template>

<script>
export default {};
</script>

<style lang="scss" scoped>
.ericsson-logo {
  div.line {
    width: 30px;
    border-radius: 5px;
    height: 7px;
    background-color: black;
    margin: 3.5px;
    transform: rotate(-20deg);
    box-shadow: 0 0 1px transparent;
  }
}
</style>

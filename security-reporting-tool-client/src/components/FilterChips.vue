<template>
  <div class="text-center">
    <v-btn
      v-if="!chip1 && !chip2 && !chip3 && !chip4"
      close
      color="primary"
      dark
      outlined
      @click="(chip1 = true), (chip2 = true), (chip3 = true), (chip4 = true)"
    >
      Reset Filters
    </v-btn>

    <v-chip v-if="chip1" class="ma-2" close label @click:close="chip1 = false">
      CXP
    </v-chip>

    <v-chip
      v-if="chip2"
      class="ma-2"
      close
      color="red"
      text-color="white"
      label
      @click:close="chip2 = false"
    >
      CSA
    </v-chip>

    <v-chip
      v-if="chip3"
      class="ma-2"
      close
      color="green"
      label
      @click:close="chip3 = false"
    >
      CRA
    </v-chip>

    <v-chip
      v-if="chip4"
      class="ma-2"
      close
      color="orange"
      label
      @click:close="chip4 = false"
    >
      CNA
    </v-chip>
  </div>
</template>

<script>
export default {
  data() {
    return {
      chip1: true,
      chip2: true,
      chip3: true,
      chip4: true,
    };
  },
};
</script>

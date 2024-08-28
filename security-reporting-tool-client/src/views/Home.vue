<template>
  <v-container class="pa-0">
    <v-row>
      <v-col class="pa-0">
        <FiltersHeader />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <SecurityDataTable />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import SecurityDataTable from "@/components/SecurityDataTable.vue";
import FiltersHeader from "@/components/FiltersHeader.vue";

export default {
  name: "Home",

  components: {
    SecurityDataTable,
    FiltersHeader,
  },
};
</script>

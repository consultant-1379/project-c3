<template>
  <v-toolbar tile color="accentLighten2" class="ma-1">
    <v-row class="my-auto">
      <v-col align-self="center">
        <v-card class="d-flex" color="rgba(0,0,0,0)" elevation="0">
          <v-text-field
            prepend-inner-icon="mdi-magnify"
            dense
            label="Repo Name"
            outlined
            color="accent"
            class="mx-2"
            v-model="searchedRepoName"
            @input="searchRepo"
          ></v-text-field>

          <v-select
            prepend-inner-icon="mdi-form-select"
            :items="reportType"
            label="Security Report Type"
            dense
            outlined
            class="mx-2"
            color="accent"
            v-model="reportTypeSelected"
          ></v-select>

          <!-- FILTERS -->
          <v-dialog transition="dialog-top-transition" max-width="600">
            <!-- DIALOG ACTIVATOR -->
            <template v-slot:activator="{ on, attrs }">
              <v-btn color="accent" v-bind="attrs" v-on="on" class="mx-2">
                <v-icon>mdi-filter</v-icon>
                Filters
              </v-btn>
            </template>

            <template v-slot:default="dialog">
              <v-card>
                <v-toolbar color="accent" dark>Filters</v-toolbar>
                <v-card-text>
                  <div class="my-5">
                    <v-autocomplete
                      :items="csa"
                      chips
                      label="CSA"
                      clearable
                      deletable-chips
                      filled
                      multiple
                      outlined
                      rounded
                      color="accent"
                    ></v-autocomplete>
                    <v-divider></v-divider>
                  </div>

                  <div class="my-5">
                    <v-autocomplete
                      :items="cra"
                      chips
                      label="CRA"
                      clearable
                      deletable-chips
                      filled
                      multiple
                      rounded
                      outlined
                      color="accent"
                    ></v-autocomplete>
                    <v-divider></v-divider>
                  </div>

                  <div class="my-5">
                    <v-autocomplete
                      :items="cna"
                      chips
                      label="CNA"
                      clearable
                      deletable-chips
                      filled
                      multiple
                      outlined
                      rounded
                      color="accent"
                    ></v-autocomplete>
                    <v-divider></v-divider>
                  </div>
                </v-card-text>
                <v-card-actions class="justify-end">
                  <v-btn color="success" @click="dialog.value = false"
                    >Save</v-btn
                  >
                </v-card-actions>
              </v-card>
            </template>
          </v-dialog>

          <v-menu bottom min-width="200px" rounded offset-y>
            <template v-slot:activator="{ on }">
              <v-btn text v-on="on">
                <v-avatar color="accent" size="36" rounded>
                  <span class="white--text text-h5" width="36">{{
                    user.initials
                  }}</span>
                </v-avatar>
              </v-btn>
            </template>

            <v-card>
              <v-list>
                <v-list-subheader>
                  <div class="text-h6 text-center font-weight-bold">
                    {{ user.fullName }}
                  </div>
                  <p class="text-caption mt-1 px-2">
                    {{ user.email }}
                  </p>
                </v-list-subheader>

                <v-list-item-group>
                  <v-divider class="my-3"></v-divider>
                  <v-list-item class="pa-0" @click="logout">
                    <v-list-item-content class="justify-center">
                      Logout
                    </v-list-item-content>
                  </v-list-item>
                </v-list-item-group>
              </v-list>
            </v-card>
          </v-menu>
        </v-card>
      </v-col>
    </v-row>
  </v-toolbar>
</template>

<script>
// import FilterChips from "./FilterChips.vue";;
import cna from "@/data/json/cna.json";
import csa from "@/data/json/csa.json";
import cra from "@/data/json/cra.json";
import { mapActions, mapGetters } from "vuex";
export default {
  name: "Filters",
  created() {},
  props: {},
  components: {},
  methods: {
    ...mapActions(["setSearchedRepoName", "setRepos", "setFilteredRepos"]),
    logout() {
      this.$router.push("/login");
    },
    searchRepo(repoName) {
      let repos =
        this.getRepos.length < this.getFilteredRepos.length
          ? this.getFilteredRepos
          : this.getRepos;

      //set backup
      if (!this.getFilteredRepos.length > 0) {
        this.setFilteredRepos(repos);
      }

      const filteredRepos = repos.filter((repo) =>
        repo.name.toLowerCase().includes(repoName.toLowerCase())
      );

      this.setRepos(filteredRepos);

      if (repoName === "") {
        const backup = this.getFilteredRepos;
        this.setRepos(backup);
      }
    },
  },
  computed: {
    ...mapGetters(["getSearchedRepoName", "getRepos", "getFilteredRepos"]),
  },
  data: () => {
    return {
      filters: ["CRA", "CSA", "CNA", "CXP"],
      reportType: ["OWASP"], // TODO "SANS (TODO)", "CERT (TODO)", "CWE (TODO)"
      reportTypeSelected: "OWASP",
      csa: csa,
      cra: cra,
      cna: cna,
      user: {
        initials: "IG",
        fullName: "Ibraheem Ghaffar",
        email: "ibraheem.ghaffar@ericsson.com",
      },
      searchedRepoName: "",
    };
  },
};
</script>

<style scoped lang="scss"></style>

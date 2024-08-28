const state = {
  user: null,
  searchedRepoName: "",
  repos: [],
  filteredRepos: [],
};

const getters = {
  user: (state) => state.user,
  userName: (state) => state.user.display_name,
  getSearchedRepoName: (state) => state.searchedRepoName,
  getFilteredRepos: (state) => state.filteredRepos,
  getRepos: (state) => state.repos,
};

const actions = {
  async setAuthUser({ commit }, payload) {
    commit("setAuthUser", payload);
  },
  async setSearchedRepoName({ commit }, payload) {
    commit("setSearchedRepoName", payload);
  },
  async setRepos({ commit }, payload) {
    commit("setRepos", payload);
  },
  async setFilteredRepos({ commit }, payload) {
    commit("setFilteredRepos", payload);
  },
};

const mutations = {
  setAuthUser: (state, user) => (state.user = user),
  setSearchedRepoName: (state, searchedRepoName) =>
    (state.searchedRepoName = searchedRepoName),
  setRepos: (state, repos) => (state.repos = repos),
  setFilteredRepos: (state, filteredRepos) =>
    (state.filteredRepos = filteredRepos),
};

export default {
  state,
  getters,
  actions,
  mutations,
};

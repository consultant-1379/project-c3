let axios = require("axios");

var axiosInstance = axios.create({
  baseURL: process.env.VUE_APP_SPRING_API_URL,
});

module.exports = axiosInstance;

module.exports.getRepos = async function() {
  return axiosInstance.get("/repos");
};

module.exports.login = async function(user) {
  return axiosInstance.post("/login", user);
};

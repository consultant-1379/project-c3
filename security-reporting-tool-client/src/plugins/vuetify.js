import Vue from "vue";
import Vuetify from "vuetify/lib/framework";

Vue.use(Vuetify, {
  options: {
    customProperties: true,
  },
});

export default new Vuetify({
  theme: {
    options: { customProperties: true },
    themes: {
      dark: {
        primary: "#040404",
        secondary: "#282828",
      },
      light: {
        primaryDark: "#040404",
        secondaryDark: "#0c0c0c",
        teritaryDark: "#121212",
        primaryGray: "#282828",
        accent: "#015871",
        accentLighten1: "#BFD0D5",
        accentLighten2: "#D9E3E6",
      },
    },
  },
});

<template>
  <v-container>
    <div class="ocean">
      <div class="wave"></div>
      <div class="wave"></div>
    </div>
    <LoginHeader />

    <v-row no-gutters align="center" justify="center" class="my-16">
      <!-- Security at your fingertips section -->
      <v-col md="6" lg="7" xl="6" align="center" class="mb-5">
        <v-row>
          <v-col>
            <div class="text-center text-h4 font-weight-bold">
              Security Reports
            </div>
            <div class="text-center text-h4 font-weight-bold">
              At your
              <div class="d-inline-block fingertips">fingertips.</div>
            </div>
          </v-col>
        </v-row>

        <v-row>
          <v-col>
            <v-img
              src="@/assets/svgs/data-report-animate.svg"
              width="500"
            ></v-img>
          </v-col>
        </v-row>
      </v-col>

      <!-- login card -->
      <v-col md="6" lg="5" xl="4" align="center">
        <v-card color="accentLighten2" elevation="0">
          <v-row justify="center">
            <v-col cols="8" class="py-10">
              <v-card-title class="px-0 mb-4 text-h5 font-weight-bold"
                >Sign In today.</v-card-title
              >

              <v-card-subtitle></v-card-subtitle>

              <v-text-field
                v-model="username"
                prepend-inner-icon="mdi-key"
                color="accent"
                :rules="[rules.required]"
                label="Ericsson Signum"
                outlined
              ></v-text-field>

              <v-text-field
                v-model="password"
                prepend-inner-icon="mdi-lock"
                color="accent"
                :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                :rules="[rules.required]"
                :type="showPassword ? 'text' : 'password'"
                name="input-10-2"
                label="Password"
                outlined
                hint=""
                class="input-group--focused"
                @click:append="showPassword = !showPassword"
              ></v-text-field>

              <v-checkbox
                class="ma-0"
                v-model="rememberMe"
                label="Remember me"
                color="accent"
              ></v-checkbox>

              <v-card-actions>
                <v-btn
                  color="accent"
                  large
                  block
                  class="pa-2"
                  :loading="loggingIn"
                  @click="login"
                  >Sign In</v-btn
                >
              </v-card-actions>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import LoginHeader from "@/components/LoginHeader.vue";
import api from "@/utils/api.js";
import { mapActions } from "vuex";
import Swal from "sweetalert2";

export default {
  components: { LoginHeader },

  methods: {
    ...mapActions(["setAuthUser"]),
    async login() {
      this.loggingIn = true;

      const user = {
        username: this.username,
        password: this.password,
      };

      this.setAuthUser(user);

      api
        .login(user)
        .then((res) => {
          console.log(res);
          this.$router.push("/home");
        })
        .catch((error) => {
          this.loggingIn = false;
          Swal.fire({
            title: "Incorrect Signum or Password!",
            text: "Please try again",
            icon: "error",
            confirmButtonText: "OK",
          });
          return;
        });
    },
  },
  data() {
    return {
      username: "",
      password: "",
      showPassword: false,
      loggingIn: false,
      rememberMe: false,
      rules: {
        required: (value) => !!value || "Required.",
        min: (v) => v.length >= 8 || "Min 8 characters",
        emailMatch: () => `The email and password you entered don't match`,
      },
    };
  },
};
</script>

<style lang="scss" scoped>
.v-btn {
  padding: 1.5rem !important;
  border-radius: 6px;
}

.v-card {
  border-radius: 20px !important;
}

.fingertips {
  color: var(--v-accent-base);
}

body {
  background: radial-gradient(
    ellipse at center,
    rgba(255, 254, 234, 1) 0%,
    rgba(255, 254, 234, 1) 35%,
    #b7e8eb 100%
  );
  overflow: hidden;
}

.ocean {
  height: 5%;
  width: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
  background: #015871;
}

.wave {
  background: url(https://s3-us-west-2.amazonaws.com/s.cdpn.io/85486/wave.svg)
    repeat-x;
  position: absolute;
  top: -198px;
  width: 6400px;
  height: 198px;
  animation: wave 12s cubic-bezier(0.36, 0.45, 0.63, 0.53) infinite;
  transform: translate3d(0, 0, 0);
}

.wave:nth-of-type(2) {
  top: -175px;
  animation: wave 12s cubic-bezier(0.36, 0.45, 0.63, 0.53) -0.125s infinite,
    swell 7s ease -1.25s infinite;
  opacity: 1;
}

@keyframes wave {
  0% {
    margin-left: 0;
  }
  100% {
    margin-left: -1600px;
  }
}

@keyframes swell {
  0%,
  100% {
    transform: translate3d(0, -25px, 0);
  }
  50% {
    transform: translate3d(0, 5px, 0);
  }
}
</style>

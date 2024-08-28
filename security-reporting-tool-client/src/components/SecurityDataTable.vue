/* eslint-disable */

<template>
  <v-row>
    <v-col>
      <v-row>
        <v-col>
          <v-expansion-panels>
            <v-expansion-panel v-for="(repo, index) in repos" :key="index">
              <v-expansion-panel-header>
                <v-row>
                  <v-col>
                    <span>{{ repo.name }} </span>
                  </v-col>

                  <v-col>
                    <span>{{ repo.cxp }}</span>
                  </v-col>

                  <v-col>
                    <span>Vulnerabilities:</span>
                    {{
                      getTotalVulnerabilityCount(
                        repo.securityReportTotals.owaspReportDataList
                      )
                    }}
                  </v-col>
                </v-row>
              </v-expansion-panel-header>

              <v-expansion-panel-content>
                <v-alert text outlined color="accent" icon="mdi-info">
                  <v-row>
                    <v-col>
                      <!-- Report Type -->
                      <div class="font-weight-bold my-1">
                        Issues Breakdown by OWASP Category and Severity
                      </div>
                    </v-col>

                    <!-- Date and Time -->
                    <v-col align="right">
                      <div class="text--secondary text-body2">
                        {{ getTodaysDataAndTime }}
                      </div>
                    </v-col>
                  </v-row>
                  <v-divider></v-divider>

                  <v-row align="center" class="my-1" dense>
                    <div class="font-weight-light">
                      {{ repo.name }}
                    </div>
                    <v-btn
                      v-tooltip="'Open Gerrit link in new tab'"
                      small
                      icon
                      @click="openGerritInNewTab(repo.name)"
                    >
                      <v-icon dark> mdi-source-repository </v-icon>
                    </v-btn>
                  </v-row>

                  <v-row>
                    <v-col align="left" class="px-0">
                      <v-btn text color="accent" class="pl-2">
                        <div>CSA: {{ repo.filters["csa"] }}</div>
                      </v-btn>
                    </v-col>

                    <v-col align="center" class="px-0">
                      <v-btn text color="accent">
                        <div>CRA: {{ repo.filters["cra"] }}</div>
                      </v-btn>
                    </v-col>

                    <v-col align="right" class="px-0">
                      <v-btn text color="accent">
                        <div>CNA: {{ repo.filters["cna"] }}</div>
                      </v-btn>
                    </v-col>
                  </v-row>
                </v-alert>
                <v-data-table
                  :headers="headers"
                  :items="repo.securityReportTotals.owaspReportDataList"
                  :hide-default-footer="true"
                  class="elevation-0"
                >
                  <template
                    v-for="(h, index) in headers"
                    v-slot:[`header.${h.value}`]="{ header }"
                  >
                    {{ header.text }}

                    <template>
                      <div :key="index">
                        <v-row v-if="header.severity == 'blocker'">
                          <v-col>
                            <v-img
                              v-tooltip="'Blocker'"
                              width="20"
                              src="@/assets/svgs/sonarqube/icons/blocker-severity-icon.svg"
                            ></v-img>
                          </v-col>
                        </v-row>

                        <v-row v-if="header.severity == 'critical'">
                          <v-col>
                            <v-img
                              v-tooltip="'Critical'"
                              width="20"
                              src="@/assets/svgs/sonarqube/icons/critical-severity-icon.svg"
                            ></v-img>
                          </v-col>
                        </v-row>

                        <v-row v-if="header.severity == 'major'">
                          <v-col>
                            <v-img
                              v-tooltip="'Major'"
                              width="20"
                              src="@/assets/svgs/sonarqube/icons/major-severity-icon.svg"
                            ></v-img>
                          </v-col>
                        </v-row>

                        <v-row v-if="header.severity == 'minor'">
                          <v-col>
                            <v-img
                              v-tooltip="'Minor'"
                              width="20"
                              src="@/assets/svgs/sonarqube/icons/minor-severity-icon.svg"
                            ></v-img>
                          </v-col>
                        </v-row>

                        <v-row v-if="header.severity == 'info'">
                          <v-col>
                            <v-img
                              v-tooltip="'Info'"
                              width="20"
                              src="@/assets/svgs/sonarqube/icons/info-severity-icon.svg"
                            ></v-img>
                          </v-col>
                        </v-row>
                      </div>
                    </template>
                  </template>

                  <template v-slot:body="{ items }">
                    <tbody>
                      <tr v-for="item in items" :key="item.id">
                        <td>
                          <div
                            class="
                              font-weight-bold
                              text--secondary text-capitalize
                            "
                          >
                            {{ prefixCategoryDescriptions(item.category) }}
                          </div>
                        </td>

                        <td>
                          <v-chip
                            dark
                            :color="
                              getColor(calculateSecurityRating(item.severities))
                            "
                          >
                            {{ calculateSecurityRating(item.severities) }}
                          </v-chip>
                        </td>

                        <td>
                          <div dark>
                            {{ item.inReviewSecurityHotspots }}
                          </div>
                        </td>

                        <td>
                          <div dark>
                            {{ item.vulnerabilities }}
                          </div>
                        </td>

                        <td>
                          <div dark>
                            {{
                              item["severities"] !== undefined
                                ? item.severities[0].count
                                : "-"
                            }}
                          </div>
                        </td>

                        <td>
                          <div dark>
                            {{
                              item["severities"] !== undefined
                                ? item.severities[1].count
                                : "-"
                            }}
                          </div>
                        </td>

                        <td>
                          <div dark>
                            {{
                              item["severities"] !== undefined
                                ? item.severities[2].count
                                : "-"
                            }}
                          </div>
                        </td>

                        <td>
                          <div dark>
                            {{
                              item["severities"] !== undefined
                                ? item.severities[3].count
                                : "-"
                            }}
                          </div>
                        </td>

                        <td>
                          <div dark>
                            {{
                              item["severities"] !== undefined
                                ? item.severities[4].count
                                : "-"
                            }}
                          </div>
                        </td>
                      </tr>
                    </tbody>
                      <!-- <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td>{{calculate(repo)}}</td>
                        <td class="title">Totals</td>
                        <td class="title">Totals</td>
                        <td class="title">Totals</td>
                        <td class="title">Totals</td>
                      </tr> -->
                  </template>
                </v-data-table>
              </v-expansion-panel-content>
            </v-expansion-panel>
          </v-expansion-panels>
        </v-col>
      </v-row>
    </v-col>
  </v-row>
</template>

<script>
import moment from "moment";
import { mapGetters, mapState } from "vuex";

export default {
  data() {
    return {
      headers: [
        {
          text: "Category",
          align: "start",
          value: "category",
        },
        { text: "Rating", value: "rating" },
        { text: "Hotspots", value: "hotspots" },
        { text: "Vulnerabilities", value: "vulnerabilities" },
        {
          value: "blockerSeverityCount",
          severity: "blocker",
        },
        {
          value: "criticalSeverityCount",
          severity: "critical",
        },
        {
          value: "majorSeverityCount",
          severity: "major",
        },
        {
          value: "minorSeverityCount",
          severity: "minor",
        },
        {
          value: "infoSeverityCount",
          severity: "info",
        },
      ],
      totalNumberOfVulnerabilities: 0,
      localRepos: [],
    };
  },
  methods: {
    calculate(repo){
      //console.log(repo.securityReportTotals.owaspReportDataList)
      let sum = repo.securityReportTotals.owaspReportDataList.reduce((a,b) => a["severities"][0].count + b["severities"][0].count, 0)
      return 5;
    },
    getColor(rating) {
      switch (rating) {
        case "B":
          return "#A5D802";
        case "C":
          return "yellow";
        case "D":
          return "#FD7500";
        case "E":
          return "#FF0000";
        default:
          return "green";
      }
    },
    prefixCategoryDescriptions(category) {
      switch (category.toLowerCase()) {
        case "a1":
          return `${category} - Injection`;
        case "a2":
          return `${category} - Broken Authentication`;
        case "a3":
          return `${category} - Sensistive Data Exposure`;
        case "a4":
          return `${category} - XML External Entities (XXE)`;
        case "a5":
          return `${category} - Broken Access Control`;
        case "a6":
          return `${category} - Security Misconfiguration`;
        case "a7":
          return `${category} - Cross-Site Scripting (XSS)`;
        case "a8":
          return `${category} - Insecure Deserialization`;
        case "a9":
          return `${category} - Using Components with Known Vulnerabilities`;
        case "a10":
          return `${category} - Insufficient Loging & Monitoring`;

        default:
          return category;
      }
    },
    getTotalVulnerabilityCount(repos) {
      return repos.reduce(
        (count, { vulnerabilities }) => count + vulnerabilities,
        0
      );
    },
    calculateSecurityRating(severities) {
      for (let severity of severities) {
        if (severity.severity === "MINOR" && severity.count >= 1) {
          return "B";
        }

        if (severity.severity === "MAJOR" && severity.count >= 1) {
          return "C";
        }
        if (severity.severity === "CRITICAL" && severity.count >= 1) {
          return "D";
        }
        if (severity.severity === "BLOCKER" && severity.count >= 1) {
          return "E";
        }
      }

      return "A";
    },
    openGerritInNewTab(repoName) {
      window.open(
        `https://gerrit.ericsson.se/#/admin/projects/OSS/${repoName}`,
        "_blank"
      );
    },
  },

  computed: {
    ...mapGetters(["getRepos"]),
    ...mapState({
      repos: (state) => state.auth.repos,
    }),
    getTodaysDataAndTime() {
      return moment().format("MMMM Do YYYY, h:mma");
    },
  },
};
</script>

<style lang="scss" scoped></style>

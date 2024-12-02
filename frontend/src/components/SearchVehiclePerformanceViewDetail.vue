<template>

    <v-card outlined>
        <v-card-title>
            SearchVehiclePerformance # {{$route.params.id }}
        </v-card-title>

        <v-card-text>
            <div>
                <Date label="RegistrationDate" v-model="item.registrationDate" :editMode="editMode" @change="change" />
            </div>
            <Period offline label="Period" v-model="item.period" :editMode="false" :key="false" @change="change" />
        </v-card-text>
    </v-card>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'SearchVehiclePerformanceViewDetail',
    props: {
      value: Object,
    },
    data: () => ({
        item : [],
    }),
    async created() {
      var params = this.$route.params;
      var temp = await axios.get(axios.fixUrl('/searchVehiclePerformances/' + params.id))

      this.item = temp.data;

    },
    methods: {
    }
  }
</script>


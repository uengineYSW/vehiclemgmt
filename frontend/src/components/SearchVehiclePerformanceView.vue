<template>

    <v-data-table
        :headers="headers"
        :items="searchVehiclePerformance"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'SearchVehiclePerformanceView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
                { text: "registrationDate", value: "registrationDate" },
                { text: "period", value: "period" },
            ],
            searchVehiclePerformance : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/searchVehiclePerformances'))

            temp.data._embedded.searchVehiclePerformances.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.searchVehiclePerformance = temp.data._embedded.searchVehiclePerformances;
        },
        methods: {
        }
    }
</script>


<template>

    <v-data-table
        :headers="headers"
        :items="searchCarAssignment"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'SearchCarAssignmentView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
                { text: "approvalDate", value: "approvalDate" },
                { text: "requestDate", value: "requestDate" },
                { text: "period", value: "period" },
            ],
            searchCarAssignment : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/searchCarAssignments'))

            temp.data._embedded.searchCarAssignments.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.searchCarAssignment = temp.data._embedded.searchCarAssignments;
        },
        methods: {
        }
    }
</script>


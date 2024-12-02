<template>

    <v-card outlined>
        <v-card-title>
            SearchCarAssignment # {{$route.params.id }}
        </v-card-title>

        <v-card-text>
            <div>
                <Date label="ApprovalDate" v-model="item.approvalDate" :editMode="editMode" @change="change" />
            </div>
            <div>
                <Date label="RequestDate" v-model="item.requestDate" :editMode="editMode" @change="change" />
            </div>
            <Period offline label="Period" v-model="item.period" :editMode="false" :key="false" @change="change" />
        </v-card-text>
    </v-card>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'SearchCarAssignmentViewDetail',
    props: {
      value: Object,
    },
    data: () => ({
        item : [],
    }),
    async created() {
      var params = this.$route.params;
      var temp = await axios.get(axios.fixUrl('/searchCarAssignments/' + params.id))

      this.item = temp.data;

    },
    methods: {
    }
  }
</script>


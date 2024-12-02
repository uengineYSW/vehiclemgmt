<template>
    <v-card outlined>
        <v-card-title>
            DistanceCalculation
        </v-card-title>

        <v-card-text>
            <String label="RegistrationId" v-model="value.registrationId" :editMode="editMode"/>
            <int offline label="AccumulatedDistanceBefore" v-model="value.accumulatedDistanceBefore" :editMode="editMode" @change="change"/>
            <int offline label="AccumulatedDistanceAfter" v-model="value.accumulatedDistanceAfter" :editMode="editMode" @change="change"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="primary"
                    text
                    @click="distanceCalculation"
            >
                DistanceCalculation
            </v-btn>
            
            <v-btn
                    color="primary"
                    text
                    @click="close"
            >
                Close
            </v-btn>
        </v-card-actions>
    </v-card>

</template>

<script>
   
    export default {
        name: 'DistanceCalculationCommand',
        components:{},
        props: {},
        data: () => ({
            editMode: true,
            value: {},
        }),
        created() {
            this.value.registrationId = '';
            this.value.accumulatedDistanceBefore = {};
            this.value.accumulatedDistanceAfter = {};
        },
        watch: {
        },
        methods: {
            distanceCalculation() {
                this.$emit('distanceCalculation', this.value);
            },
            close() {
                this.$emit('closeDialog');
            },
            change() {
                this.$emit('input', this.value);
            },
        }
    }
</script>


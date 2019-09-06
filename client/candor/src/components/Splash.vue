<template>
    <div style="width:80%;margin:auto;max-width:500px;">
        <br/>
        <p>Please Login or enter a location to view posts</p>
        <form>
            <b-form-input v-on:keyup="formSubmit(zip)" :state="valid" v-model="zip" placeholder="zip code"></b-form-input>
            <br/>
            <b-button pill variant="outline-primary">Search</b-button>
        </form>
    </div>
</template>

<script>
import YButton from './wrapped/YButton.vue'
//import DBResource from '../resources/dbresource.js';

export default {
    name: "Splash",
    components: [YButton],
    methods: {
        formSubmit: function (z) {
            var v = z.length == 5 && /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(z);

            if (v) {
                this.$data.valid = v;
                /*var db = new DBResource();
                db.createSession(); */
            } else { 
                this.$data.valid = null;
            }
        }
    },
    data() {
        return {
            zip: null,
            valid: null
        }
    }    
}
</script>
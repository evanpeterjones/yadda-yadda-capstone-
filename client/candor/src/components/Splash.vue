<template>
  <div style="width:80%;margin:auto;max-width:500px;">
    <br>
    <p>Please enter a location to view posts</p>
    <form>
      <b-form-input
        v-model="zip"
        :state="valid"
        placeholder="zip code"
        @keyup="formSubmit(zip)"
      />
      <br>
      <b-button
        pill
        variant="outline-primary"
      >
        Search
      </b-button>
    </form>
  </div>
</template>

<style scoped>
p { 
  color: white;
}
</style>

<script>
import YButton from './wrapped/YButton.vue'
const axios = require('axios')

export default {
    name: "Splash",
    components: [YButton],
    data() {
        return {
            zip: null,
            valid: null
        }
    },
    methods: {
        formSubmit: function (z) {
            var v = z.length == 5 && /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(z);

            if (v) {
                this.$data.valid = v;
                
                /* request posts
                
                var db = new DBResource();
                db.createSession();*/
            } else { 
                this.$data.valid = null;
            }
        }
    }    
}
</script>
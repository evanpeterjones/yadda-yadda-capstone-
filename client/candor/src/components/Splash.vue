<template>
  <div style="width:80%;margin:auto;max-width:500px;">
    <br>
    <p>Please enter a location to view posts</p>
    <form>
      <b-form-input
        v-model="zip"
        :state="valid"
        placeholder="zip code"
      />
      <br>
      <b-button
        pill
        @click="formSubmit(zip)"
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
.btn-default {
  background-color: gold;
  color: gold;
  border-radius: 25px;
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
          axios.get("/db").then(response => { console.log(response); })
            .error(error => { console.log(error); });

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
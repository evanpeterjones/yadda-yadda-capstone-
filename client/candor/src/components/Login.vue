<template>
  <div>
    <div>
      <span> 
        <h1>{{ appname }}</h1>
      </span>
    </div>
    <div style="width:80%;margin:auto;max-width:500px;">
      <br>
      <p>Please enter a location to view posts</p>
      <b-form @submit="submit(zip)">
        <b-form-input
          v-model="zip"
          :state="valid"
          placeholder="zip code"
          v-on:keyup="checkPost"
        />
        <br>
        <b-button
          pill
          variant="outline-primary"
          type="submit"
        >
          Search
        </b-button>
      </b-form>
    </div>
  </div>
</template>

<script>
//const axios = require('axios')

export default {
    name: "Login",
    data() {
        return {
            appname: 'yapp',
            zip: null,
            valid: null,
            detectedLocation: {
              type: String, 
              default: 'Location Services Not Enabled'
            }
        }
    },
    methods: {
        checkPost: function(event) {
          if (event.key == 'Enter') {
            console.log('enter was pressed')
          } else {
            console.log(event.key.toString())
          }
        },

        formSubmit: function (z) {
            var v = z.length == 5 && /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(z);

            if (v) {
                this.$data.valid = v;
            } else { 
                this.$data.valid = null;
            }
        },

        getLocation: function() {
          if (navigator.geolocation) {
            this.detectedLocation = navigator.geolocation.getCurrentPosition(showPosition);
          } else {
            console.log("Ensure that HTML5 Location Services are enabled for your browser")
          }
        },
    }    
}
</script>

<style scoped>
p { 
  color: gold;
}
</style>
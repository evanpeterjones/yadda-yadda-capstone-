<template>
  <div style="width:80%;margin:auto;max-width:500px;">
    <br>
    <p>Please enter a location to view posts</p>
    <form>
      <b-form-input
        v-model="zip"
        :state="valid"
        placeholder="zip code"
        onSubmit="return false;"
        @submit="formSubmit"
        @keyup="checkKey"
      />
      <br>
      <b-button
        pill
        @click="formSubmit"
      >
        Search
      </b-button>
    </form>
    <post v-for="post in posts" postData="${post}"></post>
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
import { Post } from "./Post";
const axios = require('axios')

export default {
  name: "Splash",
  components: [YButton],
  data() {
      return {
          zip: null,
          valid: null,
          posts: {
            type: Array,
            default: []
          }
      }
  },
  methods: {
    checkKey: function(event) {
      if (event.key == "Enter") {
        //console.log('enter was pressed')
        formSubmit();
      } 
    },

    formSubmit: function() {

      if (zip == null) {
        zip = getLocation();
      }

      axios.get("/db").then(response => { console.log(response); this.posts = response.data; })
        .error(error => { console.log(error); });

        var v = z.length == 5 && /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(zip);

        if (v) {
            this.$data.valid = v;
            
            // request session
            
            db.createSession();
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
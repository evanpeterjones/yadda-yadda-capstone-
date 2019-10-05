<template>
<div style="width:80%;margin:auto;max-width:500px;">
  <br>
  <p>Please enter a location to view posts</p>
  <form>
    <b-form-input
      v-model="zip"
      :state="valid"
      placeholder="zip code"
      on-submit="return false;"
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
</div>
</template>

<style scoped>
p { 
    color: white;
}

.btn-default {
    background-color: gold;
    border-radius: 25px;
}
</style>

<script>
import { Post } from "./Post";
const axios = require('axios')

export default {
    name: "Splash",
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
    watch: {
	'zip': function () {
	    this.valid = (this.zip.length == 5 && /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(this.zip)) ? true : null;
	}
    },
    methods: {
	checkKey: function(event) {
	    if (event.key == "Enter") {
		formSubmit();
	    } 
	},
	
	formSubmit: function() {
	    
	    if (this.zip == null) {
		this.zip = this.getLocation();
	    }
	    
	    axios.get("/db").then(response => { console.log(response); this.posts = response.data; })
		.error(error => { console.log(error); });
	    // replace with post when server endpoint is setup
	    // this POST needs to return a cookie from the server that generates a key
	    // which will be used to identify the user
	},
	
	getLocation: function() {
	    
	    var options = {
		enableHighAccuracy: true,
		timeout: 5000,
		maximumAge: 0
	    };
	    
	    function success(pos) {
		var crd = pos.coords;
		this.zip = crd.latitude;
		
		console.log('Your current position is:');
		console.log(`Latitude : ${crd.latitude}`);
		console.log(`Longitude: ${crd.longitude}`);
		console.log(`More or less ${crd.accuracy} meters.`);
	    }
	    
	    function error(err) {
		console.warn(`ERROR(${err.code}): ${err.message}`);
	    }
	    
	    navigator.geolocation.getCurrentPosition(success, error, options);
	    /*
	    var theZip = null;
	    if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(response) {
		    theZip = response;
			console.log(response);
		    return theZip;
		}, function(error) {
		    console.error(error);
		}, {
		    enableHighAccuracy: false,
		    timeout: 5000,
		    maximumAge: 0
		});
	    } else {
		console.log("Ensure that HTML5 Location Services are enabled for your browser")
		return null
	    }

		if (theZip == null) { console.error("big yikes"); }
		*/
	},
  },
    mounted() {
	this.getLocation();
    }
}
</script>

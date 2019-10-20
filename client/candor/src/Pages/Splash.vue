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
        @click="formSubmit
		"
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
const axios = require('axios')

export default {
    name: "Splash",
    data() {
		return {
			zip: null,
			valid: null
		}
    },
    watch: {
		'zip': function () {
			this.valid = (this.zip.length == 5 && /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(this.zip)) ? true : null;
		}, 
		'location': function () {
			
		}
    },
    mounted() {
		this.getLocation();		
    },
    methods: {
		
		checkKey: function(event) {
			if (event.key == "Enter") {
				formSubmit();
			} 
		},
		
		formSubmit: function() {

			if (!this.zip) {
				this.zip = this.getLocation();
			}

			if (!this.zip) { 
				alert('you must provide a zip code or enable location services to continue');
			}

			// else we post latitude and longitude to the server, which associates our cookie
			// with a location

		},

		setLocation: function(loc) {
			if (loc) {
				this.$commit('setLocation', loc)
			}
		},
		
		getLocation: function() {

			var options = {
				enableHighAccuracy: true,
				timeout: 5000,
				maximumAge: 0
			};
			
			navigator.geolocation.getCurrentPosition(pos => {
				console.log(pos)
				var crd = pos.coords;
					
				console.log('Your current position is:');
				console.log(`Latitude : ${crd.latitude}`);
				console.log(`Longitude: ${crd.longitude}`);
				console.log(`More or less ${crd.accuracy} meters.`);
				
				axios.get('/getzip', {
					withCredentials: true,
					params: {
						lat : crd.latitude, 
						long: crd.longitude
					}
				})
				.then(response => {
					console.log(response.data)
					this.$store.commit('setLocation', response.data)
					console.log(res)
				}).catch(error => {
					console.log(error);
				});
			}, err => { console.warn(`ERROR(${err.code}): ${err.message}`); }, options);
		},
	}
}
</script>

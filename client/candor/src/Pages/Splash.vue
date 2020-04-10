<template>
  <div style="width:80%;margin:auto;max-width:500px;">
    <br>
    <p style="text-align:center">
      Please enter a location to view posts
    </p>
    <form>
      <b-form-input
        v-model="zip"
        :state="valid"
        placeholder="zip code"
        on-submit="return false;"
        @submit="flight(zip)"
        @keyup.enter="formSubmit(zip)"
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
    border-radius: 25px;
}
</style>

<script>
export default {
    name: "Splash",
    data() {
		return {
			zip: null,
			valid: false
		}
    },
    watch: {
		'zip': function () {
			this.valid = (this.zip.length == 5 || /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(this.zip))
		}
    },
    mounted() {
		this.getLocation();		
    },
    methods: {

		flight: function() {
			console.log("asdf")
			this.$emit("newPostDialog")
		},
		
		formSubmit: function(zipcode) {
			if (zipcode) {
				this.$http.get("/setUserLocation", {
					params: {
						zip: zipcode, 
						ses: this.$cookies.get('yapp-session')
					}
				}).then((response) => {
					console.log(response);
					this.$store.commit('setLocation', response.data[0]);
				}).catch((error) => {
					console.log(error);
				});
			} else {
				this.zip = this.getLocation();
			}

			if (!this.zip) { 
				alert('you must provide a zip code or enable location services to continue');
			}
		},

		getLocation: function() {
			var options = {
				enableHighAccuracy: true,
				timeout: 5000,
				maximumAge: 0
			};
			navigator.geolocation.getCurrentPosition(pos => {
				var crd = pos.coords;

				if (crd) {

					this.$http.get('/getzip', {
						withCredentials: true,
						params: {
							lat : crd.latitude, 
							long: crd.longitude
						}
					})
					.then(response => {
						this.$store.commit('setLocation', response.data[0])
					}).catch(error => {
						console.log(error);
					});
				} else {
					console.error("No location found")
				}
			}, err => { 
				console.warn(`ERROR(${err.code}): ${err.message}`); 
			}, options);
		},
	}
}
</script>

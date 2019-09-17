<template>
    <div>
        <div>
            <span> 
               <h1>{{appname}}</h1>
            </span>
        </div>
        <div style="width:80%;margin:auto;max-width:500px;">
            <br/>
            <p>Please enter a location to view posts</p>
            <b-form @submit="submit(zip)">
                <b-form-input :state="valid" v-model="zip" placeholder="zip code"></b-form-input>
                <br/>
                <b-button pill variant="outline-primary" type="submit">Search</b-button>
            </b-form>
        </div>
    </div>
</template>

<script>
//const axios = require('axios')

export default {
    name: "Login",
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
    },
    data() {
        return {
            appname: 'yapp',
            zip: null,
            valid: null
        }
    }    
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css?family=Hepta+Slab:900&display=swap');

h1{
    font-family: 'Hepta Slab', serif;
    text-shadow: 1px 0 5px transparent;
    text-shadow: -1px -1px 0 var(--primary), 1px -1px 0 var(--primary), -1px 1px 0 var(--primary), 1px 1px 0 var(--primary);
    color: white;
}

h1 span {
    box-shadow:0 4px 10px 0 rgba(0,0,0,0.2),0 4px 20px 0 rgba(0,0,0,0.19);
    width: 100%;
    height: 20px;
    background-color: var(--primary);
}

</style>
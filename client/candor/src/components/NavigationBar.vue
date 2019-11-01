<template>
  <div>
    <div v-if="!location" class="login">
        <h1>{{ appname[0] }}<span class="logo">{{ appname1 }}</span></h1>
    </div>
    <div class="container" v-else>
        <div class="row">
            <div class="col-xl-4">
                <h1>{{ appname }}</h1>
            </div>
            <div class="col-xl-4">
                <b-button pill @click="newPost">New Yapp</b-button>
            </div>
            <div class="col-xl-4">
                <h4>{{ location }}</h4>
            </div>
        </div>
    </div>
  </div>
</template>
    
<script>
//import { messages } from '../en-us.js'
import NewPost from './NewPost'

export default {
    name: "NavigationBar",
    components: {
        NewPost
    },
    data() {
        return {
            appname : 'YAPP', //'yadda yadda' //²
            appname1 : 'APP' //'yadda yadda' //²
        }
    },
    computed : {
        'location' : function() {
            return this.$store.getters.location;
        }
    },
    methods: {
        newPost: function() {
            
        }
    }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css?family=Hepta+Slab:900&display=swap');

.logo {
    font-family: 'Hepta Slab', serif;
    letter-spacing: 16px;
    color: gold;
    text-shadow: -1px -1px 0 #222222,
                1px -1px 0 #222222,
                -1px 1px 0 #222222,
                1px 1px 0 #222222;
}
h1{
    font-family: 'Hepta Slab', serif;
    letter-spacing: 16px;
    text-shadow: -1px -1px 0 gold,
                1px -1px 0 gold,
                -1px 1px 0 gold,
                1px 1px 0 gold;
    color: #222222
}
h4{
    font-family: 'Hepta Slab', serif;
    letter-spacing: 16px;
    color: gold;
}
h1 span {
    box-shadow:0 4px 10px 0 rgba(0,0,0,0.2),0 4px 20px 0 rgba(0,0,0,0.19);
    width: 100%;
    height: 20px;
    background-color: #222222;
}
</style>
